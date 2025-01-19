package com.artur.returnoftheancients.handlers.eventmanagers;

import com.artur.returnoftheancients.capabilities.IPlayerTimerCapability;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.api.potions.PotionVisExhaust;

public class PlayerInBiomeManager {
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();


    public void tickEventPlayerTickEvent(TickEvent.PlayerTickEvent e) {
        if (!e.player.world.isRemote && e.player.ticksExisted % 4 == 0) {
            IPlayerTimerCapability timer = TRACapabilities.getTimer(e.player);
            if (e.player.isInWater()) {
                byte k = HandlerR.getBiomeIdOnPos(e.player.world, blockPos.setPos(e.player));
                if ((Biome.getIdForBiome(InitBiome.TAINT_SEA) & 255) == k || (Biome.getIdForBiome(InitBiome.TAINT_SPIRES) & 255) == k) {
                    if (!e.player.isPotionActive(PotionFluxTaint.instance)) {
                        e.player.addPotionEffect(new PotionEffect(PotionFluxTaint.instance, 80, (int) (timer.getTime("poisoning") / 160)));
                    }
                }
            } else {
                timer.delete("poisoning");
            }
        }
    }
}
