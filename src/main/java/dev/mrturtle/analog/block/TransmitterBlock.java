package dev.mrturtle.analog.block;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class TransmitterBlock extends Block implements PolymerBlock {

	public TransmitterBlock(Settings settings) {
		super(settings);
	}

	@Override
	public Block getPolymerBlock(BlockState state) {
		return Blocks.BARRIER;
	}
}
