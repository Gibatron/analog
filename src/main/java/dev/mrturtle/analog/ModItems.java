package dev.mrturtle.analog;

import dev.mrturtle.analog.item.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
	public static final Item RADIO_ITEM = register(new RadioItem(new Item.Settings().maxCount(1)), "radio");
	public static final Item TRANSMITTER_ITEM = register(new SimpleModeledPolymerBlockItem(new Item.Settings(), ModBlocks.TRANSMITTER_BLOCK, Items.PAPER), "transmitter");
	public static final Item RECEIVER_ITEM = register(new SimpleModeledPolymerBlockItem(new Item.Settings(), ModBlocks.RECEIVER_BLOCK, Items.PAPER), "receiver");

	// GUI Items
	public static final Item RADIO_TRANSMIT_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_transmit_button");
	public static final Item RADIO_RECEIVE_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_receive_button");
	public static final Item RADIO_CHANNEL_UP_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_channel_up_button");
	public static final Item RADIO_CHANNEL_DOWN_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_channel_down_button");
	public static final Item RADIO_ENABLE_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_enable_button");
	public static final Item RADIO_DISABLE_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_disable_button");

	public static void initialize() {}

	public static <T extends Item> T register(T item, String ID) {
		Identifier itemID = new Identifier("analog", ID);
		if (item instanceof ModeledPolymerItem modeledItem) {
			modeledItem.registerModel(itemID);
		}
		return Registry.register(Registries.ITEM, itemID, item);
	}
}
