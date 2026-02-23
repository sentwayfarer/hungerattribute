package com.flarelabsmc.hungerattribute;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import static com.flarelabsmc.hungerattribute.HungerAttributes.ATTRIBUTES;

@Mod(HungerAttribute.MODID)
public class HungerAttribute
{
    public static final String MODID = "hungerattribute";
    public static final Logger LOGGER = LogUtils.getLogger();

    public HungerAttribute() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ATTRIBUTES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        GameEvents.init();
    }
}
