package dev.mrturtle.analog.util;

import com.google.common.collect.ImmutableList;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
import dev.mrturtle.analog.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.List;

public class RadioUtil {
	public static int getRadioChannel(ItemStack stack) {
		NbtCompound nbt = stack.getOrCreateNbt();
		int channel = 0;
		if (nbt.contains("channel"))
			channel = nbt.getInt("channel");
		return channel;
	}

	public static boolean isRadioTransmitting(ItemStack stack) {
		NbtCompound nbt = stack.getOrCreateNbt();
		boolean transmitting = false;
		if (nbt.contains("isTransmitting"))
			transmitting = nbt.getBoolean("isTransmitting");
		return transmitting;
	}

	public static boolean isRadioReceiving(ItemStack stack) {
		NbtCompound nbt = stack.getOrCreateNbt();
		boolean receiving = true;
		if (nbt.contains("isReceiving"))
			receiving = nbt.getBoolean("isReceiving");
		return receiving;
	}

	public static boolean isRadioEnabled(ItemStack stack) {
		NbtCompound nbt = stack.getOrCreateNbt();
		boolean receiving = false;
		if (nbt.contains("isEnabled"))
			receiving = nbt.getBoolean("isEnabled");
		return receiving;
	}

	public static void setRadioChannel(ItemStack stack, int channel) {
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.putInt("channel", channel);
		stack.setNbt(nbt);
	}

	public static void setRadioTransmitting(ItemStack stack, boolean transmitting) {
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.putBoolean("isTransmitting", transmitting);
		stack.setNbt(nbt);
	}

	public static void setRadioReceiving(ItemStack stack, boolean receiving) {
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.putBoolean("isReceiving", receiving);
		stack.setNbt(nbt);
	}

	public static void setRadioEnabled(ItemStack stack, boolean enabled) {
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.putBoolean("isEnabled", enabled);
		stack.setNbt(nbt);
	}

	public static void transmitOnChannel(VoicechatServerApi serverApi, MicrophonePacket packet, ServerPlayerEntity sender, int senderChannel) {
		MinecraftServer server = sender.getServer();
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			if (player == sender)
				continue;
			boolean canHear = false;
			List<ItemStack> radios = RadioUtil.getRadios(player);
			for (ItemStack stack : radios) {
				if (!RadioUtil.isRadioEnabled(stack))
					continue;
				if (!RadioUtil.isRadioReceiving(stack))
					continue;
				int channel = RadioUtil.getRadioChannel(stack);
				if (channel != senderChannel)
					continue;
				canHear = true;
			}
			if (!canHear)
				continue;
			// Play voice to nearby players
			List<PlayerEntity> playersInRange = player.getServerWorld().getEntitiesByClass(PlayerEntity.class, Box.of(player.getPos(), 16, 16, 16), (entity) -> true);
			for (PlayerEntity entity : playersInRange) {
				serverApi.sendLocationalSoundPacketTo(serverApi.getConnectionOf(entity.getUuid()), packet.locationalSoundPacketBuilder().position(serverApi.createPosition(player.getX(), player.getY(), player.getZ())).distance(8f).build());
			}
		}
	}

	public static List<ItemStack> getRadios(ServerPlayerEntity player) {
		List<List<ItemStack>> inventories = ImmutableList.of(player.getInventory().main, player.getInventory().offHand);
		List<ItemStack> radios = new ArrayList<>();
		for (List<ItemStack> inventory : inventories) {
			for (ItemStack stack : inventory) {
				if (!stack.isOf(ModItems.RADIO_ITEM))
					continue;
				radios.add(stack);
			}
		}
		return radios;
	}
}
