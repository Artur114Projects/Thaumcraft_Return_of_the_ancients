package com.artur.returnoftheancients.init;

import com.artur.returnoftheancients.generation.worlds.WorldProviderAncientWorld;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class InitDimensions {
    public static final String ancient_world_name = "ancient_world";
    public static final int ancient_world_dim_id = -2147483647;
    public static final DimensionType ancient_world_dim_type = DimensionType.register(ancient_world_name, "_" + ancient_world_name, ancient_world_dim_id, WorldProviderAncientWorld.class, true);

    public static final void registerDimensions()
    {
        DimensionManager.registerDimension(ancient_world_dim_id, ancient_world_dim_type);
        System.out.println("Dimension registry ancient_world \ndim_type: " + ancient_world_dim_type + " \ndim_id: " + ancient_world_dim_id + " \nname: " + ancient_world_name);
    }

}