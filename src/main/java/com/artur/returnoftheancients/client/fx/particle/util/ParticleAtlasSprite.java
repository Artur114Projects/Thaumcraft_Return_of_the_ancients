package com.artur.returnoftheancients.client.fx.particle.util;

import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

public class ParticleAtlasSprite extends ParticleSprite {
    protected final TextureAtlasSprite[] atlasSprites;
    protected final ResourceLocation[] sprites;

    public ParticleAtlasSprite(EnumAssetLocation textureLocation, String... names) {
        super(null, null);
        this.sprites = new ResourceLocation[names.length];
        this.atlasSprites = new TextureAtlasSprite[names.length];
        for (int i = 0; i != sprites.length; i++) {
            this.sprites[i] = new ResourceLocation(textureLocation.getPathNotTextures(names[i]));
        }
    }

    @Override
    public void register(TextureStitchEvent.Pre e) {
        for (int i = 0; i != sprites.length; i++) {
            this.atlasSprites[i] = e.getMap().registerSprite(sprites[i]);
        }
    }

    @Override
    public String iconName() {
        return sprites[0].toString();
    }

    public String iconName(int id) {
        return id < sprites.length && id >= 0 ? sprites[id].toString() : "";
    }

    @Override
    public ResourceLocation rl() {
        return sprites[0];
    }

    public ResourceLocation rl(int id) {
        return id < sprites.length && id >= 0 ? sprites[id] : null;
    }

    @Override
    public TextureAtlasSprite atlasSprite() {
        return atlasSprites[0];
    }

    public TextureAtlasSprite atlasSprite(int id) {
        return id < sprites.length && id >= 0 ? atlasSprites[id] : null;
    }

    public int atlasSize() {
        return sprites.length;
    }

    public static String[] genNumberedNames(String name, int startIndex, int namesCount) {
        String[] ret = new String[namesCount];

        for (int i = 0; i != namesCount; i++) {
            ret[i] = name + (startIndex + i);
        }

        return ret;
    }
}
