package dev.mrturtle.analog.util;

import com.google.common.collect.ImmutableList;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.audiochannel.AudioPlayer;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
import dev.mrturtle.analog.AnalogPlugin;
import dev.mrturtle.analog.ModItems;
import dev.mrturtle.analog.block.ReceiverBlockEntity;
import dev.mrturtle.analog.block.TransmitterBlockEntity;
import dev.mrturtle.analog.config.ConfigManager;
import dev.mrturtle.analog.world.GlobalRadioState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class RadioUtil {
	public static HashMap<UUID, OpusDecoder> playerDecoders = new HashMap<>();
	public static HashMap<UUID, OpusEncoder> playerEncoders = new HashMap<>();

	// You could argue these would make more sense as static methods in RadioItem, and you'd probably be right.
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
		ServerWorld world = sender.getServerWorld();
		byte[] encodedData = packet.getOpusEncodedData();
		// Decode data
		OpusDecoder decoder = playerDecoders.getOrDefault(sender.getUuid(), serverApi.createDecoder());
		playerDecoders.putIfAbsent(sender.getUuid(), decoder);
		if (encodedData.length == 0)
			decoder.resetState();
		short[] decodedData = decoder.decode(encodedData);
		// Apply filter
		//RadioFilter.applyFilter(decodedData);
		// Re-Encode data
		OpusEncoder encoder = playerEncoders.getOrDefault(sender.getUuid(), serverApi.createEncoder());
		playerEncoders.putIfAbsent(sender.getUuid(), encoder);
		if (encodedData.length == 0)
			encoder.resetState();
		final byte[] voiceData = encoder.encode(decodedData);
		// Player radios
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			if (player == sender)
				continue;
			if (!isReceivingChannel(player, senderChannel))
				continue;
			// Play voice to nearby players
			int listeningDistance = ConfigManager.config.radioListeningDistance * 2;
			List<PlayerEntity> playersInRange = world.getEntitiesByClass(PlayerEntity.class, Box.of(player.getPos(), listeningDistance, listeningDistance, listeningDistance), (entity) -> true);
			for (PlayerEntity entity : playersInRange) {
				// Prioritize player's handheld radio over another player's radio
				if (entity != player && entity != sender && isReceivingChannel(entity, senderChannel))
					continue;
				serverApi.sendLocationalSoundPacketTo(serverApi.getConnectionOf(entity.getUuid()), packet.locationalSoundPacketBuilder().opusEncodedData(voiceData).position(serverApi.createPosition(player.getX(), player.getY(), player.getZ())).distance(8f).build());
			}
		}
		// Receivers
		server.execute(() -> {
			List<BlockPos> receivers = getGlobalRadioState(world).getReceivers();
			for (BlockPos receiverPos : receivers) {
				if (!world.isChunkLoaded(receiverPos))
					continue;
				ReceiverBlockEntity receiver = (ReceiverBlockEntity) world.getBlockEntity(receiverPos);
				if (receiver == null)
					continue;
				if (!receiver.enabled)
					continue;
				if (receiver.channel != senderChannel)
					continue;
				// Play voice to players nearby receiver
				List<PlayerEntity> playersInRange = world.getEntitiesByClass(PlayerEntity.class, Box.of(receiverPos.toCenterPos(), 64, 64, 64), (entity) -> true);
				for (PlayerEntity entity : playersInRange) {
					// Prioritize player's handheld radio over stationary receiver
					if (entity != sender && isReceivingChannel(entity, senderChannel))
						continue;
					serverApi.sendLocationalSoundPacketTo(serverApi.getConnectionOf(entity.getUuid()), packet.locationalSoundPacketBuilder().opusEncodedData(voiceData).position(serverApi.createPosition(receiverPos.getX(), receiverPos.getY(), receiverPos.getZ())).distance(32f).build());
				}
			}
		});
	}

	public static void transmitDataOnChannel(VoicechatServerApi serverApi, ServerWorld world, short[] audioData, int senderChannel) {
		transmitDataOnChannel(serverApi, world, audioData, senderChannel, null);
	}

	public static void transmitDataOnChannel(VoicechatServerApi serverApi, ServerWorld world, short[] audioData, int senderChannel, Runnable onAudioStopped) {
		MinecraftServer server = world.getServer();
		AtomicBoolean hasSetRunnable = new AtomicBoolean(false);
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			if (!isReceivingChannel(player, senderChannel))
				continue;
			// Play voice to nearby players
			LocationalAudioChannel channel = serverApi.createLocationalAudioChannel(UUID.randomUUID(), serverApi.fromServerLevel(world), serverApi.createPosition(player.getX(), player.getY(), player.getZ()));
			if (channel == null)
				continue;
			channel.setDistance(8f);
			channel.setCategory(AnalogPlugin.RADIO_CATEGORY);
			AudioPlayer audioPlayer = serverApi.createAudioPlayer(channel, serverApi.createEncoder(), audioData);
			if (!hasSetRunnable.get()) {
				audioPlayer.setOnStopped(onAudioStopped);
				hasSetRunnable.set(true);
			}
			audioPlayer.startPlaying();
		}
		// Receivers
		server.execute(() -> {
			List<BlockPos> receivers = getGlobalRadioState(world).getReceivers();
			for (BlockPos receiverPos : receivers) {
				if (!world.isChunkLoaded(receiverPos))
					continue;
				ReceiverBlockEntity receiver = (ReceiverBlockEntity) world.getBlockEntity(receiverPos);
				if (receiver == null)
					continue;
				if (!receiver.enabled)
					continue;
				if (receiver.channel != senderChannel)
					continue;
				LocationalAudioChannel channel = serverApi.createLocationalAudioChannel(UUID.randomUUID(), serverApi.fromServerLevel(world), serverApi.createPosition(receiverPos.toCenterPos().getX(), receiverPos.toCenterPos().getY(), receiverPos.toCenterPos().getZ()));
				if (channel == null)
					continue;
				channel.setDistance(8f);
				channel.setCategory(AnalogPlugin.RADIO_CATEGORY);
				AudioPlayer audioPlayer = serverApi.createAudioPlayer(channel, serverApi.createEncoder(), audioData);
				if (!hasSetRunnable.get()) {
					audioPlayer.setOnStopped(onAudioStopped);
					hasSetRunnable.set(true);
				}
				audioPlayer.startPlaying();
			}
		});
	}

	public static void transmitOnNearbyTransmitters(VoicechatServerApi serverApi, MicrophonePacket packet, ServerPlayerEntity sender) {
		MinecraftServer server = sender.getServer();
		ServerWorld world = sender.getServerWorld();
		List<BlockPos> transmitters = getGlobalRadioState(world).getTransmitters();
		Vec3d pos = sender.getPos();
		server.execute(() -> {
			for (BlockPos transmitterPos : transmitters) {
				if (pos.distanceTo(transmitterPos.toCenterPos()) > ConfigManager.config.radioListeningDistance)
					continue;
				TransmitterBlockEntity transmitter = (TransmitterBlockEntity) world.getBlockEntity(transmitterPos);
				if (transmitter == null)
					continue;
				if (!transmitter.enabled)
					continue;
				transmitOnChannel(serverApi, packet, sender, transmitter.channel);
			}
		});
	}

	public static boolean isReceivingChannel(PlayerEntity player, int channel) {
		List<ItemStack> radios = RadioUtil.getRadios(player);
		for (ItemStack stack : radios) {
			if (!RadioUtil.isRadioEnabled(stack))
				continue;
			if (!RadioUtil.isRadioReceiving(stack))
				continue;
			if (RadioUtil.getRadioChannel(stack) != channel)
				continue;
			return true;
		}
		return false;
	}

	public static List<ItemStack> getRadios(PlayerEntity player) {
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

	public static GlobalRadioState getGlobalRadioState(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(GlobalRadioState::fromNbt, GlobalRadioState::new, "globalRadios");
	}
}
