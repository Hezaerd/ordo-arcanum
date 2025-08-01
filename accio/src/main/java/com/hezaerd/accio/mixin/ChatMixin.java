package com.hezaerd.accio.mixin;

import com.hezaerd.accio.chat.ChatTracker;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ChatMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onChatMessage", at = @At("HEAD"))
    private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        if (player != null && packet.chatMessage() != null) {
            String message = packet.chatMessage();
            // Only process non-command messages
            if (!message.startsWith("/")) {
                ChatTracker.processMessage(player, message);
            }
        }
    }
}