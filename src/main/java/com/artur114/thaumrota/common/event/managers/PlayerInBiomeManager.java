package com.artur114.thaumrota.common.event.managers;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.common.init.InitBiomes;
import com.artur114.thaumrota.common.init.InitCapabilities;
import com.artur114.thaumrota.common.util.CapUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.potions.PotionFluxTaint;

public class PlayerInBiomeManager {
    public void tickEventPlayerTickEvent(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.START || e.player.world.isRemote) {
            return;
        }

        EntityPlayer player = e.player;
        if (player.ticksExisted % 8 == 0) {
            CapUtils.capability(player, InitCapabilities.TIMER).ifPresent(timer -> {
                if (player.isInWater()) {
                    byte k = BananaMC.biomeIdOnPos(player.world, BananaMath.floor(player.posX), BananaMath.floor(player.posZ));
                    if (BananaMC.biomeHasType(k, InitBiomes.TAINT_TYPE_L_SEA)) {
                        if (!player.isPotionActive(PotionFluxTaint.instance)) {
                            player.addPotionEffect(new PotionEffect(PotionFluxTaint.instance, 160, (int) (timer.getTime("poisoning") / 160)));
                        }
                    }
                } else {
                    timer.delete("poisoning");
                }
            });
        }
    }
}
