package com.artur114.returnoftheancients.common.init;

import com.artur114.bananalib.mc.register.ann.AutoInstantiate;
import com.artur114.bananalib.mc.register.interf.ILoadStagePre;
import com.artur114.returnoftheancients.common.generation.worlds.WorldProviderAncientWorld;
import com.artur114.returnoftheancients.common.misc.RotAConfigs;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

@AutoInstantiate
public class InitDimensions implements ILoadStagePre {
    public static final String ancient_world_name = "ancient_world";
    public static final int ancient_world_dim_id = RotAConfigs.CompatibilitySettings.ancientWorldDimId;
    public static final DimensionType ancient_world_dim_type = DimensionType.register(
        ancient_world_name,
        "_" + ancient_world_name,
        ancient_world_dim_id,
        WorldProviderAncientWorld.class,
        false
    );

    @Override
    public void onPreInit() {
        DimensionManager.registerDimension(ancient_world_dim_id, ancient_world_dim_type);
    }
}