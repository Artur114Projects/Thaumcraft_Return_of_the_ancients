package com.artur.returnoftheancients.ancientworldutilities;

import com.artur.returnoftheancients.handlers.Handler;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.sounds.ModSounds;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RemoveUnresolvedItems {
    public static final String isUUI = Referense.MODID + "isUseUnresolvedItems";
    public static final String dead = Referense.MODID + "setDead";
    public static final String time = Referense.MODID + "RemoveItemsTime";
    public static final String PRI = Referense.MODID + "phaseRemoveItems";
    public static final String isRespawn = Referense.MODID + "isRespawn";

    private static byte tick = 0;


    private static void resetNBT(EntityPlayer player) {
        player.getEntityData().setBoolean(isUUI, false);
        player.getEntityData().setInteger(time, 0);
        player.getEntityData().setByte(PRI, (byte) 0);
        System.out.println("NBT is reset");
    }

    @SubscribeEvent
    public void Tick(TickEvent.PlayerTickEvent e) {
        if (!e.player.getEntityData().getBoolean(isRespawn)) {
            if (e.player.dimension != InitDimensions.ancient_world_dim_id) {
                e.player.getEntityData().setBoolean(isRespawn, true);
            }
        }
        if (tick >= 4) {
            if (e.player.dimension == InitDimensions.ancient_world_dim_id) {
                if (!Handler.isPlayerUseUnresolvedItems(e.player).isEmpty() && (!e.player.getEntityData().getBoolean(isUUI) || !e.player.getEntityData().hasKey(isUUI)) && !e.player.isCreative() && e.player.getEntityData().getBoolean(isRespawn)) {
                    System.out.println("La ti krisa " + e.player.getName());
                    e.player.getEntityData().setBoolean(isUUI, true);
                    tick = 0;
                }
            }
        }
        tick++;
        boolean PlaySound = false;
        if (e.player.getEntityData().getBoolean(isUUI)) {
            int timeRemoveItems = e.player.getEntityData().getInteger(time);
            byte phaseRemoveItems = e.player.getEntityData().getByte(PRI);
            if (Handler.isPlayerUseUnresolvedItems(e.player).isEmpty()) {
                e.player.removePotionEffect(MobEffects.SLOWNESS);
                e.player.removePotionEffect(MobEffects.BLINDNESS);
                resetNBT(e.player);
                return;
            }
            if (phaseRemoveItems == 0) {
                PlaySound = true;
                e.player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 1200));
                e.player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1200, 10));
                phaseRemoveItems = 1;
            }
            if (phaseRemoveItems == 1 && timeRemoveItems >= 20) {
                if (timeRemoveItems == 40 && e.player instanceof EntityPlayerMP) {
                    e.player.sendMessage(new TextComponentTranslation(Referense.MODID + ".rui.o"));
                }
                if (timeRemoveItems == 100 && e.player instanceof EntityPlayerMP) {
                    e.player.sendMessage(new TextComponentTranslation(Referense.MODID + ".rui.t"));
                }
                if (timeRemoveItems == 150) {
                    if (e.player instanceof EntityPlayerMP) {
                        e.player.sendMessage(new TextComponentTranslation(Referense.MODID + ".rui.f"));
                        e.player.sendMessage(new TextComponentString(Handler.isPlayerUseUnresolvedItems(e.player).toString()));
                    }
                    phaseRemoveItems = 2;
                }
            }
            if (phaseRemoveItems == 2 && timeRemoveItems >= 750) {
                e.player.getEntityData().setBoolean(dead, true);
                e.player.setHealth(-1);
                e.player.getEntityData().setBoolean(isRespawn, false);
                resetNBT(e.player);
                System.out.println("dead");
                return;
            }
            e.player.getEntityData().setInteger(time, timeRemoveItems + 1);
            e.player.getEntityData().setByte(PRI, phaseRemoveItems);

            if (e.player instanceof EntityPlayerSP) {
                e.player.motionY = -0.1;
            }
            if (PlaySound) {
                if (e.player instanceof EntityPlayerSP) {
                    e.player.playSound(ModSounds.BUM, 1, 1);
                }
            }
        }
    }
}
