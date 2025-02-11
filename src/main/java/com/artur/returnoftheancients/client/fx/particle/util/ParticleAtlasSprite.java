package com.artur.returnoftheancients.client.fx.particle.util;

import com.artur.returnoftheancients.util.EnumTextureLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.util.Random;

public class ParticleAtlasSprite extends ParticleSprite {
    protected final ResourceLocation[] sprites;
    protected final Random randP = new Random();

    public ParticleAtlasSprite(EnumTextureLocation textureLocation, String... names) {
        super(null, null);
        sprites = new ResourceLocation[names.length];
        for (int i = 0; i != sprites.length; i++) {
            sprites[i] = new ResourceLocation(textureLocation.getPathNotTextures(names[i]));
        }
    }

    @Override
    public void register(TextureStitchEvent.Pre e) {
        for (ResourceLocation location : sprites) {
            e.getMap().registerSprite(location);
        }
    }

    @Override
    public String iconName() {
        return sprites[randP.nextInt(sprites.length)].toString();
    }

    public String iconName(int id) {
        return id < sprites.length && id >= 0 ? sprites[id].toString() : "";
    }

    public int atlasSize() {
        return sprites.length;
    }

}
