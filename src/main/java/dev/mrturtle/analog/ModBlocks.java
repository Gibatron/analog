package dev.mrturtle.analog;

import dev.mrturtle.analog.block.ReceiverBlock;
import dev.mrturtle.analog.block.TransmitterBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
	public static final Block TRANSMITTER_BLOCK = register(new TransmitterBlock(AbstractBlock.Settings.create().pistonBehavior(PistonBehavior.BLOCK).sounds(BlockSoundGroup.WOOD).strength(1.5f).nonOpaque()), "transmitter");
	public static final Block RECEIVER_BLOCK = register(new ReceiverBlock(AbstractBlock.Settings.create().pistonBehavior(PistonBehavior.BLOCK).sounds(BlockSoundGroup.WOOD).strength(1.5f).nonOpaque()), "receiver");

	public static void initialize() {}

	public static <T extends Block> T register(T block, String ID) {
		Identifier blockID = new Identifier("analog", ID);
		return Registry.register(Registries.BLOCK, blockID, block);
	}
}
