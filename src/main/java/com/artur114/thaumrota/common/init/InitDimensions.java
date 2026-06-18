package com.artur114.thaumrota.common.init;

import com.artur114.bananalib.mc.registry.ann.AutoInstantiate;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePre;
import com.artur114.thaumrota.common.generation.worlds.WorldProviderAncientWorld;
import com.artur114.thaumrota.common.config.RotAConfigs;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

@AutoInstantiate
public class InitDimensions implements ILoadStagePre {
    public static final String ANCIENT_WORLD_NAME = "ancient_world";
    public static final int ANCIENT_WORLD_ID = RotAConfigs.CompatibilitySettings.ancientWorldDimId;
    public static final DimensionType ANCIENT_WORLD_DIM_TYPE = DimensionType.register(
        ANCIENT_WORLD_NAME,
        "_" + ANCIENT_WORLD_NAME,
        ANCIENT_WORLD_ID,
        WorldProviderAncientWorld.class,
        false
    );

    @Override
    public void onPreInit() {
        DimensionManager.registerDimension(ANCIENT_WORLD_ID, ANCIENT_WORLD_DIM_TYPE);
    }
}