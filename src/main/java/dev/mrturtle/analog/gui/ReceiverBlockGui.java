package dev.mrturtle.analog.gui;

import dev.mrturtle.analog.ModItems;
import dev.mrturtle.analog.block.ReceiverBlockEntity;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ReceiverBlockGui extends SimpleGui {
	private final ReceiverBlockEntity receiver;

	public ReceiverBlockGui(ServerPlayerEntity player, ReceiverBlockEntity receiver) {
		super(ScreenHandlerType.GENERIC_3X3, player, false);
		this.receiver = receiver;
		createEnableButton();
		createChannelText();
		setSlot(0, new GuiElementBuilder(ModItems.RADIO_CHANNEL_DOWN_BUTTON)
				.setName(Text.translatable("gui.analog.radio.channel_down"))
				.setCallback(() -> {
					int currentChannel = receiver.channel;
					receiver.channel = Math.max(0, currentChannel - 1);
					createChannelText();
				}).build());
		setSlot(2, new GuiElementBuilder(ModItems.RADIO_CHANNEL_UP_BUTTON)
				.setName(Text.translatable("gui.analog.radio.channel_up"))
				.setCallback(() -> {
					int currentChannel = receiver.channel;
					receiver.channel = Math.min(99, currentChannel + 1);
					createChannelText();
				}).build());
	}

	private void createChannelText() {
		int currentChannel = receiver.channel;
		String channelText = String.valueOf(currentChannel);
		if (channelText.length() == 1)
			channelText = "0" + channelText;
		setTitle(getDefaultTitle().append(Text.literal(channelText)));
	}

	private void createEnableButton() {
		boolean isEnabled = receiver.enabled;
		setSlot(4, new GuiElementBuilder(isEnabled ? ModItems.RADIO_DISABLE_BUTTON : ModItems.RADIO_ENABLE_BUTTON)
				.setName(Text.translatable(isEnabled ? "gui.analog.radio.turn_off" : "gui.analog.radio.turn_on"))
				.setCallback(() -> {
					receiver.enabled = !isEnabled;
					createEnableButton();
				}).build());
	}

	public MutableText getDefaultTitle() {
		return Text.literal("aec").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("analog", "radio_gui")));
	}
}