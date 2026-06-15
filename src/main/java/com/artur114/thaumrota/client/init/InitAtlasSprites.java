package com.artur114.thaumrota.client.init;

import com.artur114.bananalib.mc.base.client.RegAtlasSprite;
import com.artur114.bananalib.mc.registry.ann.RegistryContainer;
import com.artur114.thaumrota.common.util.EnumAsset;
import com.artur114.thaumrota.main.ThaumRotA;
import thaumcraft.Thaumcraft;

@RegistryContainer
public class InitAtlasSprites {
    public static final RegAtlasSprite PARTICLE_QUAD_GENERIC = RegAtlasSprite.of(ThaumRotA.loc("particle/particle_quad"));
    public static final RegAtlasSprite[] PARTICLE_PORTAL = RegAtlasSprite.of(ThaumRotA.MODID, "particle/particle_portal_2", "particle/particle_portal_3");
    public static final RegAtlasSprite[] PARTICLE_BLOCK_PROTECT_0 = RegAtlasSprite.of(Thaumcraft.MODID, EnumAsset.TEXTURES_TC_MODELS.path("hemis").replace("textures/", ""), 1, 15);
    public static final RegAtlasSprite[] PARTICLE_BLOCK_PROTECT_1 = RegAtlasSprite.of(Thaumcraft.MODID, EnumAsset.TEXTURES_TC_MODELS.path("ripple").replace("textures/", ""), 1, 15);
}