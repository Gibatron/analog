package dev.mrturtle.analog;

import dev.mrturtle.analog.item.component.RadioComponent;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import net.minecraft.component.DataComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModDataComponents {
	public static final DataComponentType<RadioComponent> RADIO = register("radio", DataComponentType.<RadioComponent>builder().codec(RadioComponent.CODEC).build());

	public static void initialize() {}

	private static <T> DataComponentType<T> register(String id, DataComponentType<T> component) {
		PolymerItemUtils.markAsPolymer(component);
		return Registry.register(Registries.DATA_COMPONENT_TYPE, id, component);
	}
}
