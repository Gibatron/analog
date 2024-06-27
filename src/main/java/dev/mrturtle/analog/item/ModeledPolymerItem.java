package dev.mrturtle.analog.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public interface ModeledPolymerItem extends PolymerItem {
	HashMap<ModeledPolymerItem, PolymerModelData> modelData = new HashMap<>();

	Item getPolymerItem();

	@Override
	default Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
		return getPolymerItem();
	}

	@Override
	default int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
		return modelData.get(this).value();
	}

	default void registerModel(Identifier ID) {
		Identifier modelID = Identifier.of(ID.getNamespace(), "item/" + ID.getPath());
		modelData.put(this, PolymerResourcePackUtils.requestModel(this.getPolymerItem(), modelID));
	}
}
