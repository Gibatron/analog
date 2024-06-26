package dev.mrturtle.analog.item;

import dev.mrturtle.analog.gui.RadioItemGui;
import dev.mrturtle.analog.util.RadioUtil;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RadioItem extends Item implements PolymerItem {
	private final PolymerModelData[] models;

	public RadioItem(Settings settings) {
		super(settings);
		models = new PolymerModelData[2];
		models[0] = PolymerResourcePackUtils.requestModel(Items.PAPER, new Identifier("analog", "item/radio_off"));
		models[1] = PolymerResourcePackUtils.requestModel(Items.PAPER, new Identifier("analog", "item/radio_on"));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		RadioItemGui gui = new RadioItemGui((ServerPlayerEntity) player, stack);
		gui.open();
		return TypedActionResult.success(stack);
	}

	@Override
	public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
		return Items.PAPER;
	}

	@Override
	public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
		return models[RadioUtil.isRadioEnabled(itemStack) ? 1 : 0].value();
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.analog.radio.tooltip.open", Text.keybind("key.use").formatted(Formatting.GRAY)).formatted(Formatting.DARK_GRAY));
		tooltip.add(Text.translatable("item.analog.radio.tooltip.transmit", Text.keybind("key.swapOffhand").formatted(Formatting.GRAY)).formatted(Formatting.DARK_GRAY));
	}
}
