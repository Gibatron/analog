package dev.mrturtle.analog;

import dev.mrturtle.analog.item.*;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
	public static final Item RADIO_ITEM = register(new RadioItem(new Item.Settings().maxCount(1)), "radio");
	public static final Item TRANSMITTER_ITEM = register(new SimpleModeledPolymerBlockItem(new Item.Settings(), ModBlocks.TRANSMITTER_BLOCK, Items.PAPER), "transmitter");
	public static final Item RECEIVER_ITEM = register(new SimpleModeledPolymerBlockItem(new Item.Settings(), ModBlocks.RECEIVER_BLOCK, Items.PAPER), "receiver");

	// GUI Items
	public static final Item RADIO_START_TRANSMIT_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_start_transmit_button");
	public static final Item RADIO_STOP_TRANSMIT_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_stop_transmit_button");
	public static final Item RADIO_START_RECEIVE_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_start_receive_button");
	public static final Item RADIO_STOP_RECEIVE_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_stop_receive_button");
	public static final Item RADIO_CHANNEL_UP_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_channel_up_button");
	public static final Item RADIO_CHANNEL_DOWN_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_channel_down_button");
	public static final Item RADIO_SET_CHANNEL_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_set_channel_button");
	public static final Item RADIO_ENABLE_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_enable_button");
	public static final Item RADIO_DISABLE_BUTTON = register(new SimpleGuiPolymerItem(new Item.Settings(), Items.PAPER), "radio_disable_button");

	// Block Holder Items
	public static final Item TRANSMITTER_HOLDER_ITEM = register(new SimpleModeledPolymerHolderItem(new Item.Settings(), Items.PAPER), "transmitter_holder");
	public static final Item RECEIVER_HOLDER_ITEM = register(new SimpleModeledPolymerHolderItem(new Item.Settings(), Items.PAPER), "receiver_holder");

	public static void initialize() {
		PolymerItemGroupUtils.registerPolymerItemGroup(new Identifier("analog", "group"), ItemGroup.create(ItemGroup.Row.BOTTOM, -1)
				.icon(RADIO_ITEM::getDefaultStack)
				.displayName(Text.translatable("itemGroup.analog"))
				.entries(((context, entries) -> {
					entries.add(RADIO_ITEM);
					entries.add(TRANSMITTER_ITEM);
					entries.add(RECEIVER_ITEM);
				}))
				.build()
		);
	}

	public static <T extends Item> T register(T item, String ID) {
		Identifier itemID = new Identifier("analog", ID);
		if (item instanceof ModeledPolymerItem modeledItem) {
			modeledItem.registerModel(itemID);
		}
		return Registry.register(Registries.ITEM, itemID, item);
	}
}
