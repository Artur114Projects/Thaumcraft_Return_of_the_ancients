package com.artur114.thaumrota.common.events.eventmanagers;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.common.capabilities.IPlayerTimerCapability;
import com.artur114.thaumrota.common.init.InitCapabilities;
import com.artur114.thaumrota.common.handlers.MiscHandler;
import com.artur114.thaumrota.common.init.InitBiomes;
import com.artur114.thaumrota.common.util.math.UltraMutableBlockPos;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.potions.PotionFluxTaint;

public class PlayerInBiomeManager {
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

    public void tickEventPlayerTickEvent(TickEvent.PlayerTickEvent e) {
        if (!e.player.world.isRemote && e.player.ticksExisted % 8 == 0) {
            IPlayerTimerCapability timer = InitCapabilities.getTimer(e.player);
            if (e.player.isInWater()) {
                byte k = MiscHandler.getBiomeIdOnPos(e.player.world, blockPos.setPos(e.player));
                if (BananaMC.biomeHasType(k, InitBiomes.TAINT_TYPE_L_SEA)) {
                    if (!e.player.isPotionActive(PotionFluxTaint.instance)) {
                        e.player.addPotionEffect(new PotionEffect(PotionFluxTaint.instance, 160, (int) (timer.getTime("poisoning") / 160)));
                    }
                }
            } else {
                timer.delete("poisoning");
            }
        }
    }
}
