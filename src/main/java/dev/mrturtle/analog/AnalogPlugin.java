package dev.mrturtle.analog;

import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import dev.mrturtle.analog.util.RadioUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

import java.util.List;

public class AnalogPlugin implements VoicechatPlugin {
	public VoicechatServerApi api;

	public static String RADIO_CATEGORY = "radios";

	@Override
	public void registerEvents(EventRegistration registration) {
		registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
		registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophonePacket);
	}

	private void onServerStarted(VoicechatServerStartedEvent event) {
		api = event.getVoicechat();
		// Register radio volume category
		VolumeCategory radios = api.volumeCategoryBuilder()
				.setId(RADIO_CATEGORY)
				.setName("Radios")
				.setDescription("The volume of all radios")
				.build();
		api.registerVolumeCategory(radios);
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
		List<ServerPlayerEntity> playersInRange = sourcePlayer.getServerWorld().getEntitiesByClass(ServerPlayerEntity.class, Box.of(sourcePlayer.getPos(), 16, 16, 16), (entity) -> true);
		for (ServerPlayerEntity player : playersInRange) {
			List<ItemStack> radios = RadioUtil.getRadios(player);
			for (ItemStack stack : radios) {
				if (!RadioUtil.isRadioEnabled(stack))
					continue;
				if (!RadioUtil.isRadioTransmitting(stack))
					continue;
				int channel = RadioUtil.getRadioChannel(stack);
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
