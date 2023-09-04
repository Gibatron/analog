package dev.mrturtle.analog.block;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.joml.Matrix4f;

public class BlockElementHolder extends ElementHolder {
	private final ItemDisplayElement mainElement;
	private final World world;
	private final BlockPos pos;

	public BlockElementHolder(World world, BlockPos pos, Item item) {
		this.world = world;
		this.pos = pos;
		mainElement = addElement(new ItemDisplayElement(item));
	}

	public void setDirection(Direction direction) {
		Matrix4f matrix = new Matrix4f();
		matrix.rotateY((float) Math.toRadians(-direction.asRotation()));
		mainElement.setTransformation(matrix);
	}
}
