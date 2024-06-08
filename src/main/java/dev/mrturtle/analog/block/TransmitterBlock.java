package dev.mrturtle.analog.block;

import com.mojang.serialization.MapCodec;
import dev.mrturtle.analog.ModItems;
import dev.mrturtle.analog.gui.TransmitterBlockGui;
import dev.mrturtle.analog.util.RadioUtil;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TransmitterBlock extends BlockWithEntity implements PolymerBlock, BlockWithElementHolder {
	public static final MapCodec<TransmitterBlock> CODEC = createCodec(TransmitterBlock::new);
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	public TransmitterBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		TransmitterBlockGui gui = new TransmitterBlockGui((ServerPlayerEntity) player, (TransmitterBlockEntity) world.getBlockEntity(pos));
		gui.open();
		return ActionResult.SUCCESS;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		RadioUtil.getGlobalRadioState((ServerWorld) world).createTransmitter(pos);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		RadioUtil.getGlobalRadioState((ServerWorld) world).removeTransmitter(pos);
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
	public BlockState getPolymerBlockState(BlockState state) {
		return Blocks.BARRIER.getDefaultState();
	}

	@Override
	public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
		BlockElementHolder elementHolder = new BlockElementHolder(world, pos, ModItems.TRANSMITTER_HOLDER_ITEM);
		elementHolder.setDirection(initialBlockState.get(FACING));
		return elementHolder;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TransmitterBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return TransmitterBlockEntity::tick;
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}
}
