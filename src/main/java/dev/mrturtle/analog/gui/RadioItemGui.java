package dev.mrturtle.analog.gui;

import dev.mrturtle.analog.ModItems;
import dev.mrturtle.analog.util.RadioUtil;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class RadioItemGui extends SimpleGui {
	private final ItemStack radioStack;

	public RadioItemGui(ServerPlayerEntity player, ItemStack radioStack) {
		super(ScreenHandlerType.GENERIC_3X3, player, false);
		this.radioStack = radioStack;
		createEnableButton();
		createTransmitButton();
		createReceiveButton();
		createChannelText();
		setSlot(0, new GuiElementBuilder(ModItems.RADIO_CHANNEL_DOWN_BUTTON)
				.setName(Text.literal("Channel Down"))
				.setCallback(() -> {
					int currentChannel = RadioUtil.getRadioChannel(radioStack);
					RadioUtil.setRadioChannel(radioStack, Math.max(0, currentChannel - 1));
					createChannelText();
				}).build());
		setSlot(2, new GuiElementBuilder(ModItems.RADIO_CHANNEL_UP_BUTTON)
				.setName(Text.literal("Channel Up"))
				.setCallback(() -> {
					int currentChannel = RadioUtil.getRadioChannel(radioStack);
					RadioUtil.setRadioChannel(radioStack, Math.min(99, currentChannel + 1));
					createChannelText();
				}).build());
	}

	private void createChannelText() {
		int currentChannel = RadioUtil.getRadioChannel(radioStack);
		String channelText = String.valueOf(currentChannel);
		if (channelText.length() == 1)
			channelText = "0" + channelText;
		setTitle(getDefaultTitle().append(Text.literal(channelText)));
	}

	private void createEnableButton() {
		boolean isEnabled = RadioUtil.isRadioEnabled(radioStack);
		setSlot(4, new GuiElementBuilder(isEnabled ? ModItems.RADIO_DISABLE_BUTTON : ModItems.RADIO_ENABLE_BUTTON)
				.setName(Text.literal(isEnabled ? "Turn Off" : "Turn On"))
				.setCallback(() -> {
					RadioUtil.setRadioEnabled(radioStack, !isEnabled);
					createEnableButton();
				}).build());
	}

	private void createTransmitButton() {
		boolean isTransmitting = RadioUtil.isRadioTransmitting(radioStack);
		setSlot(5, new GuiElementBuilder(isTransmitting ? ModItems.RADIO_STOP_TRANSMIT_BUTTON : ModItems.RADIO_START_TRANSMIT_BUTTON)
				.setName(Text.literal(isTransmitting ? "Stop Transmitting" : "Start Transmitting"))
				.setCallback(() -> {
					RadioUtil.setRadioTransmitting(radioStack, !isTransmitting);
					createTransmitButton();
				}).build());
	}

	private void createReceiveButton() {
		boolean isReceiving = RadioUtil.isRadioReceiving(radioStack);
		setSlot(3, new GuiElementBuilder(isReceiving ? ModItems.RADIO_STOP_RECEIVE_BUTTON : ModItems.RADIO_START_RECEIVE_BUTTON)
				.setName(Text.literal(isReceiving ? "Stop Receiving" : "Start Receiving"))
				.setCallback(() -> {
					RadioUtil.setRadioReceiving(radioStack, !isReceiving);
					createReceiveButton();
				}).build());
	}

	public MutableText getDefaultTitle() {
		return Text.literal("abc").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("analog", "radio_gui")));
	}
}
