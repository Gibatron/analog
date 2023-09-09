package dev.mrturtle.analog;

import eu.pb4.polymer.core.api.other.PolymerSoundEvent;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ModSounds {
	public static SoundEvent RADIO_STATIC_EVENT = new PolymerSoundEvent(new Identifier("analog", "radio_static"), 8, true, SoundEvents.ENTITY_GENERIC_BURN);

	public static void initialize() {}
}
