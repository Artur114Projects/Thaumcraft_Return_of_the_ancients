package com.artur.returnoftheancients.events.eventmanagers;

import com.artur.returnoftheancients.capabilities.IPlayerTimerCapability;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.potions.PotionFluxTaint;

public class PlayerInBiomeManager {
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();


    public void tickEventPlayerTickEvent(TickEvent.PlayerTickEvent e) {
        if (!e.player.world.isRemote && e.player.ticksExisted % 8 == 0) {
            IPlayerTimerCapability timer = TRACapabilities.getTimer(e.player);
            if (e.player.isInWater()) {
                byte k = MiscHandler.getBiomeIdOnPos(e.player.world, blockPos.setPos(e.player));
                if (MiscHandler.arrayContains(InitBiome.TAINT_BIOMES_L_SEA_ID, k)) {
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
