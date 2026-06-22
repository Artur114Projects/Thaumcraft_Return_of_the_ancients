package com.artur114.thaumrota.common.worldstate.ancientworld.system.base;

import com.artur114.thaumrota.common.worldstate.ancientworld.system.server.IServerAncientLayer1Manager;
import com.artur114.thaumrota.common.init.InitCapabilities;
import com.artur114.thaumrota.common.init.InitDimensions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AncientLayer1StaticManager {
    public static void intoAncientWorld(EntityPlayerMP player) {
        if (player.getServer() == null) return;
        World world = player.getServer().getWorld(InitDimensions.ANCIENT_WORLD_ID);

        IAncientLayer1Manager manager = world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (manager instanceof IServerAncientLayer1Manager) {
            ((IServerAncientLayer1Manager) manager).intoAncientWorld(player);
        }
    }

    public static @Nullable AncientLayer1 sectorForPlayer(EntityPlayer player) {
        if (player == null) return null;
        if (player.dimension != InitDimensions.ANCIENT_WORLD_ID) return null;
        IAncientLayer1Manager manager = player.world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (manager == null) {
            return null;
        }

        return manager.sectorFor(player);
    }
}
