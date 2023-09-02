package dev.mrturtle.analog.item;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class SimpleModeledPolymerHolderItem extends SimpleModeledPolymerItem {

	public SimpleModeledPolymerHolderItem(Settings settings, Item polymerItem) {
		super(settings, polymerItem);
	}

	@Override
	public void registerModel(Identifier ID) {
		Identifier modelID = new Identifier(ID.getNamespace(), "block/" + ID.getPath().replace("_holder", ""));
		modelData.put(this, PolymerResourcePackUtils.requestModel(this.getPolymerItem(), modelID));
	}
}
