package com.flarelabsmc.hungerattribute;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HungerAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, HungerAttribute.MODID);

    public static final RegistryObject<Attribute> MAX_HUNGER = ATTRIBUTES.register("max_hunger",
            () -> new RangedAttribute("attribute.name.max_hunger",20.0D, 1.0D, 128.0D).setSyncable(true));

    public static final RegistryObject<Attribute> MAX_SATURATION = ATTRIBUTES.register("max_saturation",
            () -> new RangedAttribute("attribute.name.max_saturation", 20.0D, 0.0D, 128.0D).setSyncable(true));

}
