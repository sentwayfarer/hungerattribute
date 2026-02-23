package com.flarelabsmc.hungerattribute;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = HungerAttribute.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GameEvents {
    public static void init() {}

    @SubscribeEvent
    public static void addAttribute(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, HungerAttributes.MAX_HUNGER.get(),20.0D);
        event.add(EntityType.PLAYER, HungerAttributes.MAX_SATURATION.get(),20.0D);
    }
}
