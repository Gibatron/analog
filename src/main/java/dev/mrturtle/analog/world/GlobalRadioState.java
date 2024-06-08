package dev.mrturtle.analog.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
			Optional<BlockPos> pos = NbtHelper.toBlockPos(compound, "pos");
			if (pos.isPresent())
				state.transmitterList.add(pos.get());
		}
		NbtList receiverList = tag.getList("globalReceivers", NbtElement.COMPOUND_TYPE);
		for (NbtElement element : receiverList) {
			NbtCompound compound = (NbtCompound) element;
			Optional<BlockPos> pos = NbtHelper.toBlockPos(compound, "pos");
			if (pos.isPresent())
				state.receiverList.add(pos.get());
		}
		return state;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList transmitterList = new NbtList();
		for (BlockPos pos : this.transmitterList) {
			NbtCompound compound = new NbtCompound();
			compound.put("pos", NbtHelper.fromBlockPos(pos));
			transmitterList.add(compound);
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
