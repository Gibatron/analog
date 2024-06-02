package dev.mrturtle.analog.gui;

import dev.mrturtle.analog.ModItems;
import dev.mrturtle.analog.config.ConfigManager;
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
				.setName(Text.translatable("gui.analog.radio.channel_down"))
				.setCallback(() -> {
					int currentChannel = RadioUtil.getRadioChannel(radioStack);
					RadioUtil.setRadioChannel(radioStack, Math.max(0, currentChannel - 1));
					createChannelText();
				}).build());
		setSlot(1, new GuiElementBuilder(ModItems.RADIO_SET_CHANNEL_BUTTON)
				.setName(Text.translatable("gui.analog.radio.set_channel"))
				.setCallback(() -> {
					RadioSelectChannelGui gui = new RadioSelectChannelGui(player, RadioUtil.getRadioChannel(radioStack), (channel) -> {
						RadioUtil.setRadioChannel(radioStack, channel);
						RadioItemGui radioGui = new RadioItemGui(player, radioStack);
						radioGui.open();
					});
					gui.open();
				}).build());
		setSlot(2, new GuiElementBuilder(ModItems.RADIO_CHANNEL_UP_BUTTON)
				.setName(Text.translatable("gui.analog.radio.channel_up"))
				.setCallback(() -> {
					int maxRadioChannel = ConfigManager.config.maxRadioChannels - 1;
					int currentChannel = RadioUtil.getRadioChannel(radioStack);
					RadioUtil.setRadioChannel(radioStack, Math.min(maxRadioChannel, currentChannel + 1));
					createChannelText();
				}).build());
	}

	private void createChannelText() {
		int currentChannel = RadioUtil.getRadioChannel(radioStack);
		String channelText = String.valueOf(currentChannel);
		if (channelText.length() == 1)
			channelText = "0" + channelText;
		String offsetString = "z".repeat(channelText.length() - 2);
		setTitle(Text.literal(offsetString).setStyle(getTitleStyle()).append(getDefaultTitle().append(Text.literal(channelText))));
	}

	private void createEnableButton() {
		boolean isEnabled = RadioUtil.isRadioEnabled(radioStack);
		setSlot(4, new GuiElementBuilder(isEnabled ? ModItems.RADIO_DISABLE_BUTTON : ModItems.RADIO_ENABLE_BUTTON)
				.setName(Text.translatable(isEnabled ? "gui.analog.radio.turn_off" : "gui.analog.radio.turn_on"))
				.setCallback(() -> {
					RadioUtil.setRadioEnabled(radioStack, !isEnabled);
					createEnableButton();
				}).build());
	}

	private void createTransmitButton() {
		boolean isTransmitting = RadioUtil.isRadioTransmitting(radioStack);
		setSlot(5, new GuiElementBuilder(isTransmitting ? ModItems.RADIO_STOP_TRANSMIT_BUTTON : ModItems.RADIO_START_TRANSMIT_BUTTON)
				.setName(Text.translatable(isTransmitting ? "gui.analog.radio.stop_transmitting" : "gui.analog.radio.start_transmitting"))
				.setCallback(() -> {
					RadioUtil.setRadioTransmitting(radioStack, !isTransmitting);
					createTransmitButton();
				}).build());
	}

	private void createReceiveButton() {
		boolean isReceiving = RadioUtil.isRadioReceiving(radioStack);
		setSlot(3, new GuiElementBuilder(isReceiving ? ModItems.RADIO_STOP_RECEIVE_BUTTON : ModItems.RADIO_START_RECEIVE_BUTTON)
				.setName(Text.translatable(isReceiving ? "gui.analog.radio.stop_receiving" : "gui.analog.radio.start_receiving"))
				.setCallback(() -> {
					RadioUtil.setRadioReceiving(radioStack, !isReceiving);
					createReceiveButton();
				}).build());
	}

	public MutableText getDefaultTitle() {
		return Text.literal("abc").setStyle(getTitleStyle());
	}

	public Style getTitleStyle() {
		return Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("analog", "radio_gui"));
	}
}
