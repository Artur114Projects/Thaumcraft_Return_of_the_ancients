package com.artur.returnoftheancients.init;

import com.artur.returnoftheancients.client.fx.particle.util.ParticleAtlasSprite;
import com.artur.returnoftheancients.client.fx.particle.util.ParticleSprite;
import com.artur.returnoftheancients.util.EnumTextureLocation;

import java.util.ArrayList;
import java.util.List;

public class InitParticleSprite {

    public static final List<ParticleSprite> PARTICLES_SPRITES = new ArrayList<>();

    public static final ParticleAtlasSprite PARTICLE_PORTAL = new ParticleAtlasSprite(EnumTextureLocation.PARTICLE_PATH, "particle_portal_1", "particle_portal_2", "particle_portal_3");
    public static final ParticleAtlasSprite PARTICLE_BLOCK_PROTECT_0 = new ParticleAtlasSprite(EnumTextureLocation.TC_MODELS_PATH, ParticleAtlasSprite.genNumberedNames("hemis", 1, 15));
    public static final ParticleAtlasSprite PARTICLE_BLOCK_PROTECT_1 = new ParticleAtlasSprite(EnumTextureLocation.TC_MODELS_PATH, ParticleAtlasSprite.genNumberedNames("ripple", 1, 15));
}
