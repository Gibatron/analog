package dev.mrturtle.analog.util;

import dev.mrturtle.analog.AnalogPlugin;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class RadioAudioUtil {
	public static AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000F, 16, 1, 2, 48000F, false);

	public static short[] getAudioData(Path path) throws Exception {
		try (InputStream input = Files.newInputStream(path)) {
			AudioInputStream stream = AudioSystem.getAudioInputStream(input);
			return convertStreamToArray(stream);
		}
	}

	public static float getAudioDuration(Path path) {
		return (path.toFile().length() / (FORMAT.getFrameSize() * FORMAT.getFrameRate()));
	}

	public static short[] convertStreamToArray(AudioInputStream stream) throws Exception {
		AudioFormat streamFormat = stream.getFormat();
		AudioFormat arrayFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, streamFormat.getSampleRate(), 16, streamFormat.getChannels(), streamFormat.getChannels() * 2, streamFormat.getSampleRate(), false);
		AudioInputStream stream1 = AudioSystem.getAudioInputStream(arrayFormat, stream);
		AudioInputStream stream2 = AudioSystem.getAudioInputStream(FORMAT, stream1);
		return AnalogPlugin.API.getAudioConverter().bytesToShorts(stream2.readAllBytes());
	}
}
