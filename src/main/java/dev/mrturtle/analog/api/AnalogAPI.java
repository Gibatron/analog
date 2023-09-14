package dev.mrturtle.analog.api;

import dev.mrturtle.analog.AnalogPlugin;
import dev.mrturtle.analog.util.RadioAudioUtil;
import dev.mrturtle.analog.util.RadioUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.List;

public class AnalogAPI {
	public static void playSoundOverChannel(ServerWorld world, Identifier soundID, int channel) {
		Path path = FabricLoader.getInstance().getModContainer(soundID.getNamespace()).orElseThrow().findPath("radio_sounds/" + soundID.getPath() + ".wav").orElseThrow();
		try {
			short[] audio = RadioAudioUtil.getAudioData(path);
			RadioUtil.transmitDataOnChannel(AnalogPlugin.API, world, audio, channel);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void playSoundsOverChannel(ServerWorld world, List<Identifier> soundIDs, int channel) {
		Identifier soundID = soundIDs.remove(0);
		Path path = FabricLoader.getInstance().getModContainer(soundID.getNamespace()).orElseThrow().findPath("radio_sounds/" + soundID.getPath() + ".wav").orElseThrow();
		try {
			short[] audio = RadioAudioUtil.getAudioData(path);
			RadioUtil.transmitDataOnChannel(AnalogPlugin.API, world, audio, channel, soundIDs.isEmpty() ? null : () -> playSoundsOverChannel(world, soundIDs, channel));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
