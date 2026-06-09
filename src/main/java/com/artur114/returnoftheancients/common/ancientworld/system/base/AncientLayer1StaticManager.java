package com.artur114.returnoftheancients.common.ancientworld.system.base;

import com.artur114.returnoftheancients.common.ancientworld.system.server.IServerAncientLayer1Manager;
import com.artur114.returnoftheancients.common.capabilities.TRACapabilities;
import com.artur114.returnoftheancients.common.init.InitDimensions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class AncientLayer1StaticManager {
    public static void intoAncientWorld(EntityPlayerMP player) {
        if (player.getServer() == null) return;
        World world = player.getServer().getWorld(InitDimensions.ancient_world_dim_id);

        IAncientLayer1Manager manager = world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (manager instanceof IServerAncientLayer1Manager) {
            ((IServerAncientLayer1Manager) manager).intoAncientWorld(player);
        }
    }
}
