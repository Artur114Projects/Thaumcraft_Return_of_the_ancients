package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.init.InitItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

@Mod.EventBusSubscriber
public class ThaumEventsHandler {
    @SubscribeEvent
    public static void registerAspects(AspectRegistryEvent event) {
        addAspectsPB(event);
        addAspectsSB(event);
    }

    private static void addAspectsPB(AspectRegistryEvent event) {
        ItemStack myItem = new ItemStack(InitItems.PRIMAL_BLADE);

        AspectList aspects = new AspectList();
        aspects.add(Aspect.FIRE, 20);
        aspects.add(Aspect.WATER, 20);
        aspects.add(Aspect.AIR, 20);
        aspects.add(Aspect.EARTH, 20);
        aspects.add(Aspect.AVERSION, 10);

        event.register.registerObjectTag(myItem, aspects);
    }

    private static void addAspectsSB(AspectRegistryEvent event) {
        ItemStack myItem = new ItemStack(InitItems.SOUL_BINDER);

        AspectList aspects = new AspectList();
        aspects.add(Aspect.SOUL, 10);
        aspects.add(Aspect.TRAP, 4);

        event.register.registerObjectTag(myItem, aspects);
    }

}