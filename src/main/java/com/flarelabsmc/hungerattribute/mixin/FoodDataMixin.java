package com.flarelabsmc.hungerattribute.mixin;


import com.flarelabsmc.hungerattribute.HungerConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import com.flarelabsmc.hungerattribute.HungerAttributes;
import com.flarelabsmc.hungerattribute.util.FoodDataExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodData.class)
public abstract class FoodDataMixin implements FoodDataExt {

    @Unique
    private Player hungeratt$player;

    @Override
    public void hungeratt$setPlayer(Player player) {
        this.hungeratt$player = player;
    }

    @ModifyExpressionValue(method = {"eat(IF)V", "tick", "needsFood"}, at = @At(value = "CONSTANT", args = "intValue=20"))
    private int modifyMaxFoodCapInAdd(int value) {
        if (hungeratt$player == null) return value;
        return (int) this.hungeratt$player.getAttributeValue(HungerAttributes.MAX_HUNGER.get());
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "CONSTANT", args = "intValue=18"))
    private int modifyMaxHungerHealValue(int original, @Local(argsOnly = true) Player player) {
        double percentageHealing = HungerConfig.hungerHealPercentage;
        return (int) (player.getAttributeValue(HungerAttributes.MAX_HUNGER.get()) * percentageHealing);
    }

    @ModifyExpressionValue(method = "eat(IF)V", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"))
    private float modifySaturationLevelInAdd(float original_value) {
        if (hungeratt$player == null) return original_value;
        return (float) Math.min(original_value, hungeratt$player.getAttributeValue(HungerAttributes.MAX_SATURATION.get()));
    }
}
