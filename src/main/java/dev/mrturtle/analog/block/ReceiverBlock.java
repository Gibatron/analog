package dev.mrturtle.analog.block;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class ReceiverBlock extends Block implements PolymerBlock {

	public ReceiverBlock(Settings settings) {
		super(settings);
	}

	@Override
	public Block getPolymerBlock(BlockState state) {
		return Blocks.BARRIER;
	}
}
