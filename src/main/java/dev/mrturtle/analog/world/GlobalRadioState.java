package dev.mrturtle.analog.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.List;

public class GlobalRadioState extends PersistentState {
	private final ArrayList<BlockPos> transmitterList;
	private final ArrayList<BlockPos> receiverList;

	public GlobalRadioState() {
		transmitterList = new ArrayList<>();
		receiverList = new ArrayList<>();
	}

	public void createTransmitter(BlockPos pos) {
		transmitterList.add(pos);
		markDirty();
	}

	public void removeTransmitter(BlockPos pos) {
		transmitterList.remove(pos);
		markDirty();
	}

	public void createReceiver(BlockPos pos) {
		receiverList.add(pos);
		markDirty();
	}

	public void removeReceiver(BlockPos pos) {
		receiverList.remove(pos);
		markDirty();
	}

	public List<BlockPos> getTransmitters() {
		return transmitterList;
	}

	public List<BlockPos> getReceivers() {
		return receiverList;
	}

	public static GlobalRadioState fromNbt(NbtCompound tag) {
		GlobalRadioState state = new GlobalRadioState();
		NbtList transmitterList = tag.getList("globalTransmitters", NbtElement.COMPOUND_TYPE);
		for (NbtElement element : transmitterList) {
			NbtCompound compound = (NbtCompound) element;
			state.transmitterList.add(NbtHelper.toBlockPos(compound));
		}
		NbtList receiverList = tag.getList("globalReceivers", NbtElement.COMPOUND_TYPE);
		for (NbtElement element : receiverList) {
			NbtCompound compound = (NbtCompound) element;
			state.receiverList.add(NbtHelper.toBlockPos(compound));
		}
		return state;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		NbtList transmitterList = new NbtList();
		for (BlockPos pos : this.transmitterList) {
			transmitterList.add(NbtHelper.fromBlockPos(pos));
		}
		tag.put("globalTransmitters", transmitterList);
		NbtList receiverList = new NbtList();
		for (BlockPos pos : this.receiverList) {
			receiverList.add(NbtHelper.fromBlockPos(pos));
		}
		tag.put("globalReceivers", receiverList);
		return tag;
	}
}
