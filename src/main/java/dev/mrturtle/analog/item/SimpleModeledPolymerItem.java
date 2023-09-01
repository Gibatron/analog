package dev.mrturtle.analog.item;

import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import net.minecraft.item.Item;

public class SimpleModeledPolymerItem extends SimplePolymerItem implements ModeledPolymerItem {
	private final Item polymerItem;

	public SimpleModeledPolymerItem(Settings settings, Item polymerItem) {
		super(settings, polymerItem);
		this.polymerItem = polymerItem;
	}

	@Override
	public Item getPolymerItem() {
		return polymerItem;
	}
}
