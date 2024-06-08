package dev.mrturtle.analog;

import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import dev.mrturtle.analog.config.ConfigManager;
import dev.mrturtle.analog.item.component.RadioComponent;
import dev.mrturtle.analog.util.RadioUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

import java.util.List;

public class AnalogPlugin implements VoicechatPlugin {
	public static VoicechatServerApi API;

	public static String RADIO_CATEGORY = "radios";

	@Override
	public void registerEvents(EventRegistration registration) {
		registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
		registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophonePacket);
	}

	private void onServerStarted(VoicechatServerStartedEvent event) {
		API = event.getVoicechat();
		// Register radio volume category
		VolumeCategory radios = API.volumeCategoryBuilder()
				.setId(RADIO_CATEGORY)
				.setName("Radios")
				.setDescription("The volume of all radios")
				.build();
		API.registerVolumeCategory(radios);
	}

	private void onMicrophonePacket(MicrophonePacketEvent event) {
		VoicechatConnection connection = event.getSenderConnection();
		VoicechatServerApi serverApi = event.getVoicechat();
		if (connection == null)
			return;
		if (event.getPacket().getOpusEncodedData().length == 0)
			return;
		ServerPlayerEntity sourcePlayer = (ServerPlayerEntity) connection.getPlayer().getPlayer();
		// Find nearby players that might be carrying radios that could transmit
		int listeningDistance = ConfigManager.config.radioListeningDistance * 2;
		List<ServerPlayerEntity> playersInRange = sourcePlayer.getServerWorld().getEntitiesByClass(ServerPlayerEntity.class, Box.of(sourcePlayer.getPos(), listeningDistance, listeningDistance, listeningDistance), (entity) -> true);
		for (ServerPlayerEntity player : playersInRange) {
			List<ItemStack> radios = RadioUtil.getRadios(player);
			for (ItemStack stack : radios) {
				RadioComponent component = stack.getOrDefault(ModDataComponents.RADIO, RadioComponent.DEFAULT);
				if (!component.enabled())
					continue;
				if (!component.transmit())
					continue;
				int channel = component.channel();
				RadioUtil.transmitOnChannel(serverApi, event.getPacket(), player, channel);
			}
		}
		// Find nearby transmitters and transmit on those too
		RadioUtil.transmitOnNearbyTransmitters(serverApi, event.getPacket(), sourcePlayer);
	}

	@Override
	public String getPluginId() {
		return "analog";
	}
}
