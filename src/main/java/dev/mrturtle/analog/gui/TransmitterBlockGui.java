package dev.mrturtle.analog.gui;

import dev.mrturtle.analog.ModItems;
import dev.mrturtle.analog.block.TransmitterBlockEntity;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TransmitterBlockGui extends SimpleGui {
	private final TransmitterBlockEntity transmitter;

	public TransmitterBlockGui(ServerPlayerEntity player, TransmitterBlockEntity transmitter) {
		super(ScreenHandlerType.GENERIC_3X3, player, false);
		this.transmitter = transmitter;
		createEnableButton();
		createChannelText();
		setSlot(3, new GuiElementBuilder(ModItems.RADIO_CHANNEL_DOWN_BUTTON)
				.setName(Text.literal("Channel Down"))
				.setCallback(() -> {
					int currentChannel = transmitter.channel;
					transmitter.channel = Math.max(0, currentChannel - 1);
					createChannelText();
				}).build());
		setSlot(5, new GuiElementBuilder(ModItems.RADIO_CHANNEL_UP_BUTTON)
				.setName(Text.literal("Channel Up"))
				.setCallback(() -> {
					int currentChannel = transmitter.channel;
					transmitter.channel = Math.min(99, currentChannel + 1);
					createChannelText();
				}).build());
	}

	private void createChannelText() {
		int currentChannel = transmitter.channel;
		setSlot(4, new GuiElementBuilder(Items.NAME_TAG)
				.setName(Text.literal("Current Channel : " + currentChannel))
				.build());
	}

	private void createEnableButton() {
		boolean isEnabled = transmitter.enabled;
		setSlot(7, new GuiElementBuilder(isEnabled ? ModItems.RADIO_DISABLE_BUTTON : ModItems.RADIO_ENABLE_BUTTON)
				.setName(Text.literal(isEnabled ? "Turn Off" : "Turn On"))
				.setCallback(() -> {
					transmitter.enabled = !isEnabled;
					createEnableButton();
				}).build());
	}
}
