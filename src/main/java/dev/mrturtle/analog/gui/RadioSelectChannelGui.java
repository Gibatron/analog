package dev.mrturtle.analog.gui;

import dev.mrturtle.analog.ModItems;
import dev.mrturtle.analog.config.ConfigManager;
import eu.pb4.sgui.api.gui.AnvilInputGui;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class RadioSelectChannelGui extends AnvilInputGui {
	private final Consumer<Integer> finishedConsumer;
	private final int originalChannel;

	public RadioSelectChannelGui(ServerPlayerEntity player, int currentChannel, Consumer<Integer> finishedConsumer) {
		super(player, false);
		this.finishedConsumer = finishedConsumer;
		setDefaultInputValue(String.valueOf(currentChannel));
		originalChannel = currentChannel;
		setTitle(getDefaultTitle());
	}

	@Override
	public void onInput(String input) {
		if (input.isEmpty())
			return;
		try {
			int channel = Integer.parseInt(input);
			if (channel >= ConfigManager.config.maxRadioChannels) {
				setDefaultInputValue(String.valueOf(ConfigManager.config.maxRadioChannels - 1));
				sendGui();
			}
		} catch (NumberFormatException ignored) {
			setDefaultInputValue(input.replaceAll("\\D", ""));
			sendGui();
		}
	}

	@Override
	public void onClose() {
		try {
			int channel = Integer.parseInt(getInput());
			finishedConsumer.accept(channel);
		} catch (NumberFormatException ignored) {
			finishedConsumer.accept(originalChannel);
		}
	}

	@Override
	public void setDefaultInputValue(String input) {
		super.setDefaultInputValue(input);
		ItemStack itemStack = ModItems.RADIO_SET_CHANNEL_BUTTON.getDefaultStack();
		itemStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(input));
		setSlot(0, itemStack, ((index, type1, action, gui) -> {
			reOpen = true;
			sendGui();
		}));
	}

	public MutableText getDefaultTitle() {
		MutableText guiTextureText = Text.literal("gfh").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("analog", "radio_gui")));
		MutableText titleText = Text.translatable("gui.analog.radio.set_channel.title").setStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT_ID).withColor(0x3F3F3F));
		return guiTextureText.append(titleText);
	}
}
