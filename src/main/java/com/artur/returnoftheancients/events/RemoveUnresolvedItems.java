package com.artur.returnoftheancients.events;

import com.artur.returnoftheancients.handlers.HandlerR;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RemoveUnresolvedItems {
    public static final String isUUI = Referense.MODID + "isUseUnresolvedItems";
    public static final String dead = Referense.MODID + "setDead";
    public static final String time = Referense.MODID + "RemoveItemsTime";
    public static final String PRI = Referense.MODID + "phaseRemoveItems";
    public static final String isRespawn = Referense.MODID + "isRespawn";


    private static void resetNBT(EntityPlayer player) {
        player.getEntityData().setBoolean(isUUI, false);
        player.getEntityData().setInteger(time, 0);
        player.getEntityData().setByte(PRI, (byte) 0);
        System.out.println("NBT is reset");
    }

    @SubscribeEvent
    public void isClone(PlayerEvent.Clone e) {
        e.getEntityPlayer().isDead = false;
    }

    @SubscribeEvent
    public void Tick(TickEvent.PlayerTickEvent e) {
        if (e.player.dimension == InitDimensions.ancient_world_dim_id) {
            if (!HandlerR.isPlayerUseUnresolvedItems(e.player).isEmpty() && (!e.player.getEntityData().getBoolean(isUUI) || !e.player.getEntityData().hasKey(isUUI)) && !e.player.isCreative() && !e.player.isDead) {
                System.out.println("La ti krisa " + e.player.getName());
                e.player.getEntityData().setBoolean(isUUI, true);
            }
        }
        if (e.player.getEntityData().getBoolean(isUUI)) {
            int timeRemoveItems = e.player.getEntityData().getInteger(time);
            byte phaseRemoveItems = e.player.getEntityData().getByte(PRI);
            if (HandlerR.isPlayerUseUnresolvedItems(e.player).isEmpty()) {
                e.player.removePotionEffect(MobEffects.SLOWNESS);
                e.player.removePotionEffect(MobEffects.BLINDNESS);
                resetNBT(e.player);
                return;
            }
            if (phaseRemoveItems == 0) {
                if (e.player instanceof EntityPlayerSP) {
                    e.player.playSound(ModSounds.WHISPER, 1, 1);
                }
                e.player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 1200));
                e.player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1200, 10));
                phaseRemoveItems = 1;
            }
            if (phaseRemoveItems == 1 && timeRemoveItems >= 20) {
                if (timeRemoveItems == 100 && e.player instanceof EntityPlayerMP) {
                    e.player.sendMessage(new TextComponentTranslation(Referense.MODID + ".rui.t"));
                }
                if (timeRemoveItems == 160) {
                    if (e.player instanceof EntityPlayerMP) {
                        e.player.sendMessage(new TextComponentTranslation(Referense.MODID + ".rui.f"));
                        e.player.sendMessage(new TextComponentString(HandlerR.isPlayerUseUnresolvedItems(e.player).toString()));
                    }
                    phaseRemoveItems = 2;
                }
            }
            if (phaseRemoveItems == 2 && timeRemoveItems >= 760) {
                e.player.getEntityData().setBoolean(dead, true);
                e.player.setHealth(-1);
                resetNBT(e.player);
                e.player.isDead = true;
                System.out.println("dead");
                return;
            }
            e.player.getEntityData().setInteger(time, timeRemoveItems + 1);
            e.player.getEntityData().setByte(PRI, phaseRemoveItems);

            if (e.player instanceof EntityPlayerSP) {
                e.player.motionY = -0.1;
            }
            if (timeRemoveItems < 460) {
                if ((timeRemoveItems % 40) == 0 && e.player instanceof EntityPlayerSP) {
                    e.player.playSound(ModSounds.HEARTBEAT, 1, 1);
                }
            } else if (timeRemoveItems < 660) {
                if ((timeRemoveItems % 20) == 0 && e.player instanceof EntityPlayerSP) {
                    e.player.playSound(ModSounds.HEARTBEAT, 1, 1);
                }
            } else {
                if ((timeRemoveItems % 10) == 0 && e.player instanceof EntityPlayerSP) {
                    e.player.playSound(ModSounds.HEARTBEAT, 1, 1);
                }
            }
        }
    }
}
