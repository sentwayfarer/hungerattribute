package com.flarelabsmc.hungerattribute.mixin;

import com.flarelabsmc.hungerattribute.HungerAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ForgeGui.class, remap = false)
public abstract class ForgeGuiMixin {
    @Shadow public abstract Minecraft getMinecraft();
    @Shadow public int rightHeight;

    @Unique
    private int hungeratt$totalFoodRows = 1;

    @ModifyExpressionValue(
            method = "renderFood",
            at = @At(value = "CONSTANT", args = "intValue=10")
    )
    private int unlockLoopLimit(int original) {
        double attrValue = getMinecraft().player.getAttributeValue(HungerAttributes.MAX_HUNGER.get());
        int result = (int) Math.ceil(attrValue / 2.0);
        hungeratt$totalFoodRows = (int) Math.ceil(result / 10.0);
        return result;
    }

    @Inject(
            method = "renderFood",
            at = @At("HEAD")
    )
    private void adjust(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (hungeratt$totalFoodRows > 1) {
            rightHeight += (hungeratt$totalFoodRows - 1) * 10;
        }
    }

    @Inject(
            method = "renderFood",
            at = @At("RETURN")
    )
    private void resetHeight(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (hungeratt$totalFoodRows > 1) {
            rightHeight -= (hungeratt$totalFoodRows - 1) * 10;
        }
    }

    @ModifyArg(
            method = "renderFood",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
                    remap = true
            ),
            index = 1
    )
    private int modifyFoodX(int x, @Local(ordinal = 5) int i) {
        int column = i % 10;
        int screenWidth = getMinecraft().getWindow().getGuiScaledWidth();
        int left = screenWidth / 2 + 91;
        return left - column * 8 - 9;
    }

    @ModifyArg(
            method = "renderFood",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
                    remap = true
            ),
            index = 2
    )
    private int modifyFoodY(int y, @Local(ordinal = 5) int i) {
        if (hungeratt$totalFoodRows <= 1) return y;
        int row = i / 10;
        int rowFromBottom = Math.max(0, hungeratt$totalFoodRows - 1 - row);
        return y + rowFromBottom * 10;
    }
}
