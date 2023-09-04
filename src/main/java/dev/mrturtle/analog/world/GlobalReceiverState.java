package dev.mrturtle.analog.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.List;

public class GlobalReceiverState extends PersistentState {
	private final ArrayList<BlockPos> receiverList;

	public GlobalReceiverState() {
		receiverList = new ArrayList<>();
	}

	public void createReceiver(BlockPos pos) {
		receiverList.add(pos);
		markDirty();
	}

	public void removeReceiver(BlockPos pos) {
		receiverList.remove(pos);
		markDirty();
	}

	public List<BlockPos> getReceivers() {
		return receiverList;
	}

	public static GlobalReceiverState fromNbt(NbtCompound tag) {
		GlobalReceiverState state = new GlobalReceiverState();
		NbtList list = tag.getList("globalReceivers", NbtElement.COMPOUND_TYPE);
		for (NbtElement element : list) {
			NbtCompound compound = (NbtCompound) element;
			state.receiverList.add(NbtHelper.toBlockPos(compound));
		}
		return state;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		NbtList list = new NbtList();
		for (BlockPos pos : receiverList) {
			list.add(NbtHelper.fromBlockPos(pos));
		}
		tag.put("globalReceivers", list);
		return tag;
	}
}
