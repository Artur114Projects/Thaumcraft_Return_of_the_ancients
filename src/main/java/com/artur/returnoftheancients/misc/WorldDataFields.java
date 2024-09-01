package com.artur.returnoftheancients.misc;

import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ClientPacketMisc;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldDataFields {
    public static boolean isPortalGenerate;
    public static int portalY;
    public static int portalXC;
    public static int portalZC;
    public static int portalX;
    public static int portalZ;
    public static BlockPos blockPosToCompass;

    protected static void onRead(NBTTagCompound saveData) {
        portalXC = saveData.getInteger(IALGS.ancientPortalXPosKey);
        portalY = saveData.getInteger(IALGS.ancientPortalYPosKey);
        portalZC = saveData.getInteger(IALGS.ancientPortalZPosKey);
        isPortalGenerate = saveData.getBoolean(IALGS.isAncientPortalGenerateKey);
        portalZ = (16 * portalZC) + 8;
        portalX = (16 * portalXC) + 8;
        blockPosToCompass = new BlockPos(portalX, portalY, portalZ);
    }

    public static void reload() {
        NBTTagCompound saveData = WorldData.get().saveData;
        portalXC = saveData.getInteger(IALGS.ancientPortalXPosKey);
        portalY = saveData.getInteger(IALGS.ancientPortalYPosKey);
        portalZC = saveData.getInteger(IALGS.ancientPortalZPosKey);
        isPortalGenerate = saveData.getBoolean(IALGS.isAncientPortalGenerateKey);
        portalZ = (16 * portalZC) + 8;
        portalX = (16 * portalXC) + 8;
        blockPosToCompass = new BlockPos(portalX, portalY, portalZ);
    }

    public static void sync(EntityPlayerMP player) {
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger(IALGS.ancientPortalXPosKey, portalXC);
        data.setInteger(IALGS.ancientPortalYPosKey, portalY);
        data.setInteger(IALGS.ancientPortalZPosKey, portalZC);
        data.setBoolean(IALGS.isAncientPortalGenerateKey, isPortalGenerate);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("syncWorldDataFields", data);
        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), player);
    }

    @SideOnly(Side.CLIENT)
    public static void readOnClient(NBTTagCompound data) {
        portalXC = data.getInteger(IALGS.ancientPortalXPosKey);
        portalY = data.getInteger(IALGS.ancientPortalYPosKey);
        portalZC = data.getInteger(IALGS.ancientPortalZPosKey);
        isPortalGenerate = data.getBoolean(IALGS.isAncientPortalGenerateKey);
        portalZ = (16 * portalZC) + 8;
        portalX = (16 * portalXC) + 8;
        blockPosToCompass = new BlockPos(portalX, portalY, portalZ);
    }
}
