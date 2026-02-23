package com.flarelabsmc.hungerattribute;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = HungerAttribute.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HungerConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.DoubleValue HUNGER_HEAL_PERCENTAGE = BUILDER
            .comment("Determines the minimum percentage of hunger required for natural healing to occur.")
            .defineInRange("requiredPercentageForHealing", 0.9d, 0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue HUNGER_MIN_SPRINT_VALUE = BUILDER
            .comment("Determines the minimum amount of hunger that a player will require to be able to sprint.")
            .defineInRange("requiredPercentageForHealing", 6, 0, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double hungerHealPercentage;
    public static int hungerMinSprintValue;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        hungerHealPercentage = HUNGER_HEAL_PERCENTAGE.get();
        hungerMinSprintValue = HUNGER_MIN_SPRINT_VALUE.get();
    }
}
