package com.artur.returnoftheancients.misc;

import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

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
}
