package dev.mrturtle.analog.block;

import dev.mrturtle.analog.ModBlockEntities;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReceiverBlockEntity extends BlockEntity {
	public boolean enabled = false;
	public int channel = 0;

	public ReceiverBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.RECEIVER, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState blockState, BlockEntity blockEntity) {
		if (!(blockEntity instanceof ReceiverBlockEntity be))
			return;
		BlockElementHolder holder = (BlockElementHolder) BlockBoundAttachment.get(world, pos).holder();
		holder.tick();
	}

	@Override
	public void readNbt(NbtCompound tag) {
		enabled = tag.getBoolean("enabled");
		channel = tag.getInt("channel");
	}

	@Override
	protected void writeNbt(NbtCompound tag) {
		tag.putBoolean("enabled", enabled);
		tag.putInt("channel", channel);
	}
}
