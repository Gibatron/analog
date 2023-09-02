package dev.mrturtle.analog.block;

import dev.mrturtle.analog.ModItems;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class TransmitterBlock extends Block implements PolymerBlock, BlockWithElementHolder {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	public TransmitterBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public Block getPolymerBlock(BlockState state) {
		return Blocks.BARRIER;
	}

	@Override
	public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
		BlockElementHolder elementHolder = new BlockElementHolder(world, ModItems.TRANSMITTER_HOLDER_ITEM);
		elementHolder.setDirection(initialBlockState.get(FACING));
		return elementHolder;
	}
}
