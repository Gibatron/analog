package dev.mrturtle.analog.mixin;

import dev.mrturtle.analog.ModItems;
import dev.mrturtle.analog.util.RadioUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.server.network.ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandler {
	@Shadow public ServerPlayerEntity player;

	@Inject(method = "onPlayerAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"), cancellable = true)
	public void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
		if (packet.getAction() != PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND)
			return;
		ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
		if (!stack.isOf(ModItems.RADIO_ITEM))
			return;
		boolean isTransmitting = RadioUtil.isRadioTransmitting(stack);
		RadioUtil.setRadioTransmitting(stack, !isTransmitting);
		int channel = RadioUtil.getRadioChannel(stack);
		player.sendMessage(Text.translatable(!isTransmitting ? "gui.analog.radio.started_transmitting" : "gui.analog.radio.stopped_transmitting", channel), true);
		ci.cancel();
	}
}
