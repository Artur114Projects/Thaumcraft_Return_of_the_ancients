package com.artur.returnoftheancients.ancientworldgeneration.main;

import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedList;
@Mod.EventBusSubscriber(modid = Referense.MODID)
public class AncientWorld {
    private static final LinkedList<AncientEntry> ancientEntries = new LinkedList<>();

    public static void tpToAncientWorld(EntityPlayerMP player) {

    }

    public static void serverStarting(FMLServerStartingEvent starting) {
        NBTTagCompound nbt = WorldData.get().saveData.getCompoundTag("AncientWorldPak");
        int ancientEntriesCount = nbt.getInteger("ancientEntriesCount");
        for (int i = 0; i != ancientEntriesCount; i++) {

        }
    }

    @SubscribeEvent
    public static void save(WorldEvent.Save e) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("ancientEntriesCount", ancientEntries.size());

    }
}
