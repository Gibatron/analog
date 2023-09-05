package dev.mrturtle.analog.util;

import java.util.Random;

public class RadioFilter {
	public static short[] applyFilter(short[] data) {
		Random random = new Random();
		float glitchPoint = random.nextFloat() * data.length;
		for (int i = 0; i < data.length; i++) {
			if (Math.abs(glitchPoint - i) > 2)
				continue;
			data[i] = (short) (random.nextGaussian() * Short.MAX_VALUE * 0.3);
		}
		return data;
	}
}
