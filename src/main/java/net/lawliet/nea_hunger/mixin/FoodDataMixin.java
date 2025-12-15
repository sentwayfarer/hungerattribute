package net.lawliet.nea_hunger.mixin;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.lawliet.nea_hunger.NeaHungerGameRules;
import net.lawliet.nea_hunger.clientServerSync.ClientPacketListner;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.lawliet.nea_hunger.NeaHungerAttributes;
import net.lawliet.nea_hunger.iterface.duck.FoodDataDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodData.class)
public abstract class FoodDataMixin implements FoodDataDuck {

    @Unique
    private Player nea_hunger$Player;

    @Override
    public void nea_hunger$GetPlayer(Player player) {
        this.nea_hunger$Player = player;
    }

    @Unique
    private int nea_hunger$GetMaxHungerAttributeValue(int original_value) {
        if (nea_hunger$Player == null) return original_value;
        return (int) nea_hunger$Player.getAttributeValue(NeaHungerAttributes.MAX_HUNGER);
    }

    @Unique
    private float nea_hunger$GetMaxSaturationAttributeValue(float original_value) {
        if (nea_hunger$Player == null) return original_value;
        return (float) nea_hunger$Player.getAttributeValue(NeaHungerAttributes.MAX_SATURATION);
    }


    @ModifyExpressionValue(method = {"add", "tick", "needsFood"}, at = @At(value = "CONSTANT", args = "intValue=20"))
    private int modifyMaxFoodCapInAdd(int value) {
        return nea_hunger$GetMaxHungerAttributeValue(value);
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "CONSTANT", args = "intValue=18"))
    private int modifyMaxHungerHealValue(int value, ServerPlayer serverPlayer) {
        float percentageHealing = serverPlayer.level().getGameRules().get(NeaHungerGameRules.RULE_HUNGER_HEALING_PERCENTAGE.get()) / 100F;
        return (int) (nea_hunger$GetMaxHungerAttributeValue(value) * percentageHealing);
    }

    @ModifyExpressionValue(method = "add", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F"))
    private float modifySaturationLevelInAdd(float original_value) {
        return Math.min(original_value, nea_hunger$GetMaxSaturationAttributeValue(original_value));
    }

    @ModifyExpressionValue(method = "hasEnoughFood", at = @At(value = "CONSTANT", args = "floatValue=6.0F"))
    private float changeFoodValueOnSprint(float original_value) {

        int hungerSprintValue = ClientPacketListner.hungerSprintValue;
        int max_hunger_value = (int) nea_hunger$Player.getAttributeValue(NeaHungerAttributes.MAX_HUNGER);
        return (float) Math.min(max_hunger_value - 1,hungerSprintValue);

    }

}
