package dev.mrturtle.analog.item;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class SimpleGuiPolymerItem extends SimpleModeledPolymerItem {

	public SimpleGuiPolymerItem(Settings settings, Item polymerItem) {
		super(settings, polymerItem);
	}

	@Override
	public void registerModel(Identifier ID) {
		Identifier modelID = new Identifier(ID.getNamespace(), "gui/" + ID.getPath());
		modelData.put(this, PolymerResourcePackUtils.requestModel(this.getPolymerItem(), modelID));
	}
}
