package com.artur.returnoftheancients.init;

import com.artur.returnoftheancients.client.fx.particle.util.ParticleAtlasSprite;
import com.artur.returnoftheancients.client.fx.particle.util.ParticleSprite;
import com.artur.returnoftheancients.util.EnumAssetLocation;

import java.util.ArrayList;
import java.util.List;

public class InitParticleSprite {

    public static final List<ParticleSprite> PARTICLES_SPRITES = new ArrayList<>();

    public static final ParticleSprite PARTICLE_QUAD_GENERIC = new ParticleSprite("particle_quad");
    public static final ParticleAtlasSprite PARTICLE_PORTAL = new ParticleAtlasSprite("particle_portal_1", "particle_portal_2", "particle_portal_3");
    public static final ParticleAtlasSprite PARTICLE_BLOCK_PROTECT_0 = new ParticleAtlasSprite(EnumAssetLocation.TEXTURES_TC_MODELS, ParticleAtlasSprite.genNumberedNames("hemis", 1, 15));
    public static final ParticleAtlasSprite PARTICLE_BLOCK_PROTECT_1 = new ParticleAtlasSprite(EnumAssetLocation.TEXTURES_TC_MODELS, ParticleAtlasSprite.genNumberedNames("ripple", 1, 15));
}
