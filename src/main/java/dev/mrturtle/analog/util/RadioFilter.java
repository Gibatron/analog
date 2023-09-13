package dev.mrturtle.analog.util;

import java.util.Random;

public class RadioFilter {
	public static void applyFilter(short[] data) {
		Random random = new Random();
		for (int i = 0; i < data.length; i++) {
			if (random.nextDouble() < 0.005)
				data[i] = (short) (data[i] * random.nextGaussian());
		}
	}
}
