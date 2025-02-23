package com.artur.returnoftheancients.events;

import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.init.InitSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class RemoveUnresolvedItems {
    public static final String isUUI = Referense.MODID + "isUseUnresolvedItems";
    public static final String time = Referense.MODID + "RemoveItemsTime";
    public static final String PRI = Referense.MODID + "phaseRemoveItems";

    private static final String rui_title = TextFormatting.BLACK + "[THEY] " + TextFormatting.RESET;


    private static void resetNBT(EntityPlayer player) {
        player.getEntityData().setBoolean(isUUI, false);
        player.getEntityData().setInteger(time, 0);
        player.getEntityData().setByte(PRI, (byte) 0);
        System.out.println("NBT is reset");
    }

    @SubscribeEvent
    public static void Tick(TickEvent.PlayerTickEvent e) {
        if (e.player.ticksExisted % 20 == 0) {
            if (e.player.dimension == InitDimensions.ancient_world_dim_id && TRAConfigs.PortalSettings.checkItems) {
                if (!MiscHandler.isPlayerUseUnresolvedItems(e.player).isEmpty() && (!e.player.getEntityData().getBoolean(isUUI) || !e.player.getEntityData().hasKey(isUUI)) && !e.player.isCreative() && !e.player.isDead) {
                    System.out.println("La ti krisa " + e.player.getName());
                    e.player.getEntityData().setBoolean(isUUI, true);
                }
            }
        }
        if (e.player.getEntityData().getBoolean(isUUI)) {
            int timeRemoveItems = e.player.getEntityData().getInteger(time);
            byte phaseRemoveItems = e.player.getEntityData().getByte(PRI);
            if (MiscHandler.isPlayerUseUnresolvedItems(e.player).isEmpty() || e.player.dimension != InitDimensions.ancient_world_dim_id) {
                e.player.removePotionEffect(MobEffects.SLOWNESS);
                e.player.removePotionEffect(MobEffects.BLINDNESS);
                resetNBT(e.player);
                return;
            }
            if (phaseRemoveItems == 0) {
                if (e.player instanceof EntityPlayerMP) {
                    MiscHandler.playSound((EntityPlayerMP) e.player, InitSounds.WHISPER);
                }
                e.player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 1200));
                e.player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1200, 10));
                phaseRemoveItems = 1;
            }
            if (phaseRemoveItems == 1 && timeRemoveItems >= 20) {
                if (timeRemoveItems == 100 && e.player instanceof EntityPlayerMP) {
                    MiscHandler.sendMessageTranslateWithChangeTitle((EntityPlayerMP) e.player, Referense.MODID + ".rui.t", rui_title);
                }
                if (timeRemoveItems == 160) {
                    if (e.player instanceof EntityPlayerMP) {
                        MiscHandler.sendMessageTranslateWithChangeTitle((EntityPlayerMP) e.player, Referense.MODID + ".rui.f", rui_title);
                        e.player.sendMessage(new TextComponentString(MiscHandler.isPlayerUseUnresolvedItems(e.player).toString()));
                    }
                    phaseRemoveItems = 2;
                }
            }
            if (phaseRemoveItems == 2 && timeRemoveItems >= 760) {
                e.player.setHealth(-1);
                if (e.player instanceof EntityPlayerMP) {
                    MiscHandler.playSound((EntityPlayerMP) e.player, InitSounds.RUI_DEAD);
                }
                resetNBT(e.player);
                System.out.println("dead");
                return;
            }
            e.player.getEntityData().setInteger(time, timeRemoveItems + 1);
            e.player.getEntityData().setByte(PRI, phaseRemoveItems);

            if (timeRemoveItems < 460) {
                if ((timeRemoveItems % 40) == 0 && e.player instanceof EntityPlayerMP) {
                    MiscHandler.playSound((EntityPlayerMP) e.player, InitSounds.HEARTBEAT);
                }
            } else if (timeRemoveItems < 660) {
                if ((timeRemoveItems % 20) == 0 && e.player instanceof EntityPlayerMP) {
                    MiscHandler.playSound((EntityPlayerMP) e.player, InitSounds.HEARTBEAT);
                }
            } else {
                if ((timeRemoveItems % 10) == 0 && e.player instanceof EntityPlayerMP) {
                    MiscHandler.playSound((EntityPlayerMP) e.player, InitSounds.HEARTBEAT);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTransferDimension(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent e) {
        if (e.fromDim == InitDimensions.ancient_world_dim_id) {
            e.player.getEntityData().setBoolean("isUUI", false);
        }
    }

    @SubscribeEvent
    public static void clientWorldLoad(WorldEvent.Load e) {
        if (e.getWorld().isRemote && e.getWorld().provider.getDimension() != InitDimensions.ancient_world_dim_id) {
            e.getWorld().playerEntities.forEach((p) -> p.getEntityData().setBoolean("isUUI", false));
        }
    }
}
