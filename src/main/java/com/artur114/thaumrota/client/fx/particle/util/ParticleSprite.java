package com.artur114.thaumrota.client.fx.particle.util;

import com.artur114.thaumrota.client.init.InitParticleSprite;
import com.artur114.thaumrota.common.util.EnumAsset;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

public class ParticleSprite {

    protected ResourceLocation sprite = null;
    protected TextureAtlasSprite atlasSprite = null;

    public ParticleSprite(EnumAsset textureLocation, String name) {
        if (textureLocation != null) {
            sprite = new ResourceLocation(textureLocation.getPathNotTextures(name));
        }
        InitParticleSprite.PARTICLES_SPRITES.add(this);
    }

    public ParticleSprite(String name) {
        this(EnumAsset.TEXTURES_PARTICLE, name);
    }

    public void register(TextureStitchEvent.Pre e) {
        this.atlasSprite = e.getMap().registerSprite(sprite);
    }

    public String iconName() {
        return sprite.toString();
    }

    public ResourceLocation rl() {
        return sprite;
    }

    public TextureAtlasSprite atlasSprite() {
        return atlasSprite;
    }

    @Override
    public String toString() {
        return this.iconName();
    }
}
