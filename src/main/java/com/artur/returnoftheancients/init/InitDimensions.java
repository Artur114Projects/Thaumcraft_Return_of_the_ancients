package com.artur.returnoftheancients.init;

import com.artur.returnoftheancients.generation.worlds.WorldProviderAncientWorld;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class InitDimensions {
    public static final String ancient_world_name = "ancient_world";
    public static final int ancient_world_dim_id = -2147483647;
    public static final DimensionType ancient_world_dim_type = DimensionType.register(ancient_world_name, "_" + ancient_world_name, ancient_world_dim_id, WorldProviderAncientWorld.class, false);

    public static void registerDimensions() {
        DimensionManager.registerDimension(ancient_world_dim_id, ancient_world_dim_type);
    }
}