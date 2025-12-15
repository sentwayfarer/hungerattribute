package net.lawliet.nea_hunger.mixin;

import net.lawliet.nea_hunger.NeaHungerGameRules;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.gamerules.GameRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "onGameRuleChanged", at = @At("HEAD"))
    private <T> void AddGameRuleChangeEvent(GameRule<T> gameRule, T value, CallbackInfo ci) {
        NeaHungerGameRules.GameRuleChangeEvent(gameRule, value,  (MinecraftServer) ((Object) this));
    }
}
