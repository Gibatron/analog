package dev.mrturtle.analog.gui;

import dev.mrturtle.analog.ModDataComponents;
import dev.mrturtle.analog.ModItems;
import dev.mrturtle.analog.config.ConfigManager;
import dev.mrturtle.analog.item.component.RadioComponent;
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
					RadioComponent component = radioStack.getOrDefault(ModDataComponents.RADIO, RadioComponent.DEFAULT);
					radioStack.set(ModDataComponents.RADIO, component.withChannel(Math.max(0, component.channel() - 1)));
					createChannelText();
				}).build());
		setSlot(1, new GuiElementBuilder(ModItems.RADIO_SET_CHANNEL_BUTTON)
				.setName(Text.translatable("gui.analog.radio.set_channel"))
				.setCallback(() -> {
					RadioComponent component = radioStack.getOrDefault(ModDataComponents.RADIO, RadioComponent.DEFAULT);
					RadioSelectChannelGui gui = new RadioSelectChannelGui(player, component.channel(), (channel) -> {
						radioStack.set(ModDataComponents.RADIO, component.withChannel(channel));
						RadioItemGui radioGui = new RadioItemGui(player, radioStack);
						radioGui.open();
					});
					gui.open();
				}).build());
		setSlot(2, new GuiElementBuilder(ModItems.RADIO_CHANNEL_UP_BUTTON)
				.setName(Text.translatable("gui.analog.radio.channel_up"))
				.setCallback(() -> {
					int maxRadioChannel = ConfigManager.config.maxRadioChannels - 1;
					RadioComponent component = radioStack.getOrDefault(ModDataComponents.RADIO, RadioComponent.DEFAULT);
					radioStack.set(ModDataComponents.RADIO, component.withChannel(Math.min(maxRadioChannel, component.channel() + 1)));
					createChannelText();
				}).build());
	}

	private void createChannelText() {
		RadioComponent component = radioStack.getOrDefault(ModDataComponents.RADIO, RadioComponent.DEFAULT);
		String channelText = String.valueOf(component.channel());
		if (channelText.length() == 1)
			channelText = "0" + channelText;
		String offsetString = "z".repeat(channelText.length() - 2);
		setTitle(Text.literal(offsetString).setStyle(getTitleStyle()).append(getDefaultTitle().append(Text.literal(channelText))));
	}

	private void createEnableButton() {
		RadioComponent component = radioStack.getOrDefault(ModDataComponents.RADIO, RadioComponent.DEFAULT);
		boolean isEnabled = component.enabled();
		setSlot(4, new GuiElementBuilder(isEnabled ? ModItems.RADIO_DISABLE_BUTTON : ModItems.RADIO_ENABLE_BUTTON)
				.setName(Text.translatable(isEnabled ? "gui.analog.radio.turn_off" : "gui.analog.radio.turn_on"))
				.setCallback(() -> {
					radioStack.set(ModDataComponents.RADIO, component.withEnabled(!isEnabled));
					createEnableButton();
				}).build());
	}

	private void createTransmitButton() {
		RadioComponent component = radioStack.getOrDefault(ModDataComponents.RADIO, RadioComponent.DEFAULT);
		boolean isTransmitting = component.transmit();
		setSlot(5, new GuiElementBuilder(isTransmitting ? ModItems.RADIO_STOP_TRANSMIT_BUTTON : ModItems.RADIO_START_TRANSMIT_BUTTON)
				.setName(Text.translatable(isTransmitting ? "gui.analog.radio.stop_transmitting" : "gui.analog.radio.start_transmitting"))
				.setCallback(() -> {
					radioStack.set(ModDataComponents.RADIO, component.withTransmit(!isTransmitting));
					createTransmitButton();
				}).build());
	}

	private void createReceiveButton() {
		RadioComponent component = radioStack.getOrDefault(ModDataComponents.RADIO, RadioComponent.DEFAULT);
		boolean isReceiving = component.receive();
		setSlot(3, new GuiElementBuilder(isReceiving ? ModItems.RADIO_STOP_RECEIVE_BUTTON : ModItems.RADIO_START_RECEIVE_BUTTON)
				.setName(Text.translatable(isReceiving ? "gui.analog.radio.stop_receiving" : "gui.analog.radio.start_receiving"))
				.setCallback(() -> {
					radioStack.set(ModDataComponents.RADIO, component.withReceive(!isReceiving));
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
