package dev.mrturtle.analog.block;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.joml.Matrix4f;

public class BlockElementHolder extends ElementHolder {
	private final ItemDisplayElement mainElement;
	private final World world;

	public BlockElementHolder(World world, Item item) {
		this.world = world;
		mainElement = addElement(new ItemDisplayElement(item));
	}

	public void setDirection(Direction direction) {
		Matrix4f matrix = new Matrix4f();
		matrix.rotateY((float) Math.toRadians(-direction.asRotation()));
		mainElement.setTransformation(matrix);
	}

	@Override
	protected void onTick() {
		// Manually updates the lighting of the main element, because otherwise it doesn't update when the lighting changes.
		int blockLight = world.getLightLevel(LightType.BLOCK, BlockPos.ofFloored(currentPos));
		int skyLight = world.getLightLevel(LightType.SKY, BlockPos.ofFloored(currentPos));
		Brightness newBrightness = new Brightness(blockLight, skyLight);
		if (!newBrightness.equals(mainElement.getBrightness()))
			mainElement.setBrightness(newBrightness);
	}
}
