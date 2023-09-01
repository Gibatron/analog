package dev.mrturtle.analog.gui;

import dev.mrturtle.analog.ModItems;
import dev.mrturtle.analog.util.RadioUtil;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class RadioItemGui extends SimpleGui {
	private final ItemStack radioStack;

	public RadioItemGui(ServerPlayerEntity player, ItemStack radioStack) {
		super(ScreenHandlerType.GENERIC_3X3, player, false);
		this.radioStack = radioStack;
		createEnableButton();
		createTransmitButton();
		createReceiveButton();
		createChannelText();
		setSlot(3, new GuiElementBuilder(ModItems.RADIO_CHANNEL_DOWN_BUTTON)
				.setName(Text.literal("Channel Down"))
				.setCallback(() -> {
					int currentChannel = RadioUtil.getRadioChannel(radioStack);
					RadioUtil.setRadioChannel(radioStack, Math.max(0, currentChannel - 1));
					createChannelText();
				}).build());
		setSlot(5, new GuiElementBuilder(ModItems.RADIO_CHANNEL_UP_BUTTON)
				.setName(Text.literal("Channel Up"))
				.setCallback(() -> {
					int currentChannel = RadioUtil.getRadioChannel(radioStack);
					RadioUtil.setRadioChannel(radioStack, Math.min(99, currentChannel + 1));
					createChannelText();
				}).build());
	}

	private void createChannelText() {
		int currentChannel = RadioUtil.getRadioChannel(radioStack);
		setSlot(4, new GuiElementBuilder(Items.NAME_TAG)
				.setName(Text.literal("Current Channel : " + currentChannel))
				.build());
	}

	private void createEnableButton() {
		boolean isEnabled = RadioUtil.isRadioEnabled(radioStack);
		setSlot(7, new GuiElementBuilder(isEnabled ? ModItems.RADIO_DISABLE_BUTTON : ModItems.RADIO_ENABLE_BUTTON)
				.setName(Text.literal(isEnabled ? "Turn Off" : "Turn On"))
				.setCallback(() -> {
					RadioUtil.setRadioEnabled(radioStack, !isEnabled);
					createEnableButton();
				}).build());
	}

	private void createTransmitButton() {
		boolean isTransmitting = RadioUtil.isRadioTransmitting(radioStack);
		setSlot(8, new GuiElementBuilder(ModItems.RADIO_TRANSMIT_BUTTON)
				.setName(Text.literal(isTransmitting ? "Stop Transmitting" : "Start Transmitting"))
				.setCallback(() -> {
					RadioUtil.setRadioTransmitting(radioStack, !isTransmitting);
					createTransmitButton();
				}).build());
	}

	private void createReceiveButton() {
		boolean isReceiving = RadioUtil.isRadioReceiving(radioStack);
		setSlot(6, new GuiElementBuilder(ModItems.RADIO_RECEIVE_BUTTON)
				.setName(Text.literal(isReceiving ? "Stop Receiving" : "Start Receiving"))
				.setCallback(() -> {
					RadioUtil.setRadioReceiving(radioStack, !isReceiving);
					createReceiveButton();
				}).build());
	}
}
