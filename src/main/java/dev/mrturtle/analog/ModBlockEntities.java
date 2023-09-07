package dev.mrturtle.analog;

import dev.mrturtle.analog.block.ReceiverBlockEntity;
import dev.mrturtle.analog.block.TransmitterBlockEntity;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
	public static BlockEntityType<TransmitterBlockEntity> TRANSMITTER = register("transmitter", FabricBlockEntityTypeBuilder.create(TransmitterBlockEntity::new).addBlock(ModBlocks.TRANSMITTER_BLOCK));
	public static BlockEntityType<ReceiverBlockEntity> RECEIVER = register("receiver", FabricBlockEntityTypeBuilder.create(ReceiverBlockEntity::new).addBlock(ModBlocks.RECEIVER_BLOCK));

	public static void initialize() {}

	public static <T extends BlockEntity> BlockEntityType<T> register(String path, FabricBlockEntityTypeBuilder<T> builder) {
		BlockEntityType<T> type = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier("analog", path), builder.build());
		PolymerBlockUtils.registerBlockEntity(type);
		return type;
	}
}
