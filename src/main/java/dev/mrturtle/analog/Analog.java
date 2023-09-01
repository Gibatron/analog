package dev.mrturtle.analog;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Analog implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("analog");

	@Override
	public void onInitialize() {
		PolymerResourcePackUtils.addModAssets("analog");
		PolymerResourcePackUtils.markAsRequired();
		ModItems.initialize();
	}
}