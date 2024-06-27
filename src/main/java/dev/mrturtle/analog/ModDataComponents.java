package dev.mrturtle.analog;

import dev.mrturtle.analog.item.component.RadioComponent;
import eu.pb4.polymer.core.api.other.PolymerComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModDataComponents {
	public static final ComponentType<RadioComponent> RADIO = register("radio", ComponentType.<RadioComponent>builder().codec(RadioComponent.CODEC).build());

	public static void initialize() {}

	private static <T> ComponentType<T> register(String id, ComponentType<T> component) {
		PolymerComponent.registerDataComponent(component);
		return Registry.register(Registries.DATA_COMPONENT_TYPE, id, component);
	}
}
