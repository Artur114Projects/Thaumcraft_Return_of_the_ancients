package com.artur.returnoftheancients.client.fx.particle.util;

import com.artur.returnoftheancients.init.InitParticleSprite;
import com.artur.returnoftheancients.utils.EnumTextureLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

public class ParticleSprite {

    protected ResourceLocation sprite;

    public ParticleSprite(EnumTextureLocation textureLocation, String name) {
        if (textureLocation != null) {
            sprite = new ResourceLocation(textureLocation.getPathNotTextures(name));
        }
        InitParticleSprite.PARTICLES_SPRITES.add(this);
    }

    public void register(TextureStitchEvent.Pre e) {
        e.getMap().registerSprite(sprite);
    }

    public String iconName() {
        return sprite.toString();
    }

    @Override
    public String toString() {
        return this.iconName();
    }
}
