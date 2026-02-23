package com.flarelabsmc.hungerattribute.mixin;

import com.flarelabsmc.hungerattribute.HungerAttribute;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import com.flarelabsmc.hungerattribute.HungerAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow
    protected abstract Player getCameraPlayer();

    @ModifyExpressionValue(method = "renderPlayerHealth", at = @At(value = "CONSTANT", args = "intValue=10", ordinal = 0))
    private int modifyFoodLevelForDisplay(int original) {
        double food_attribute_value = getCameraPlayer().getAttributeValue(HungerAttributes.MAX_HUNGER.get());
        return (int) (food_attribute_value / 2);
    }

    @ModifyExpressionValue(method = "renderPlayerHealth", at = @At(value = "CONSTANT", args = "intValue=39", ordinal = 0))
    private int modifySpriteHeightArg(int original, @Local(ordinal = 3) int j) {
        int food_attribute_value = (int) getCameraPlayer().getAttributeValue(HungerAttributes.MAX_HUNGER.get());
        int max_rows = Mth.ceil(food_attribute_value / 2.0F / 10.0F);
        int height = Math.max(10 - (max_rows - 2), 3);
        int num_row = j / 10;
        return original - height * num_row;
    }

    @ModifyVariable(method = "renderPlayerHealth", at = @At("STORE"), ordinal = 4)
    private int modifySpriteYPosition(int value, @Local(ordinal = 3) int j) {
        int food_attribute_value = (int) getCameraPlayer().getAttributeValue(HungerAttributes.MAX_HUNGER.get());
        int max_rows = Mth.ceil(food_attribute_value / 2.0F / 10.0F);
        int height = Math.max(10 - (max_rows - 2), 3);
        int num_row = j / 10;
        return value - height * num_row;
    }

    @Expression("? < @(10)")
    @ModifyExpressionValue(
            method = "renderPlayerHealth",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I")
            )
    )
    private int modifyFoodLoopCondition(int original) {
        double attrValue = getCameraPlayer().getAttributeValue(HungerAttributes.MAX_HUNGER.get());
        int result = (int) (attrValue / 2);
        HungerAttribute.LOGGER.debug("max food icon value changed from " + original + " to " + result);
        return result;
    }
}
