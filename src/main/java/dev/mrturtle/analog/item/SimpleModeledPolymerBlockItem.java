package dev.mrturtle.analog.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;

// Well this naming convention is getting stupid
public class SimpleModeledPolymerBlockItem extends BlockItem implements ModeledPolymerItem {
	private final Item polymerItem;

	public SimpleModeledPolymerBlockItem(Settings settings, Block block, Item polymerItem) {
		super(block, settings);
		this.polymerItem = polymerItem;
	}

	@Override
	public ActionResult place(ItemPlacementContext context) {
		ActionResult superResult = super.place(context);
		if (superResult == ActionResult.CONSUME)
			return ActionResult.SUCCESS;
		return superResult;
	}

	@Override
	public Item getPolymerItem() {
		return polymerItem;
	}
}
