package com.artur114.thaumrota.common.event.managers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import thaumcraft.common.items.armor.ItemBootsTraveller;

public class TCFixEventsManager {
    public void livingFallEvent(LivingFallEvent e) {
        if (e.getDistance() > 6) {
            return;
        }
        boolean flag = false;
        for (ItemStack stack : e.getEntity().getArmorInventoryList()) {
            if (stack.getItem() instanceof ItemBootsTraveller) {
                flag = true; break;
            }
        }
        if (flag) {
            e.setDistance(0);
        }
    }
}
