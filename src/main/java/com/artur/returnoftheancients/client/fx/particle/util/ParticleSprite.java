package com.artur.returnoftheancients.client.fx.particle.util;

import com.artur.returnoftheancients.init.InitParticleSprite;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

public class ParticleSprite {

    protected ResourceLocation sprite = null;
    protected TextureAtlasSprite atlasSprite = null;

    public ParticleSprite(EnumAssetLocation textureLocation, String name) {
        if (textureLocation != null) {
            sprite = new ResourceLocation(textureLocation.getPathNotTextures(name));
        }
        InitParticleSprite.PARTICLES_SPRITES.add(this);
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
