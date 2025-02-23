package com.artur.returnoftheancients.client.fx.particle;

import com.artur.returnoftheancients.client.fx.particle.util.ParticleAtlasSprite;
import com.artur.returnoftheancients.client.fx.particle.util.ParticleSprite;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public abstract class ParticleBase<T extends ParticleSprite> extends Particle {
    private T sprite;

    public ParticleBase(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);
    }

    public ParticleBase(World worldIn, double xCordIn, double yCordIn, double zCordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCordIn, yCordIn, zCordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }

    protected void setSprite(T sprite) {
        this.sprite = sprite;
    }

    protected T sprite() {
        return sprite;
    }

    protected void bindSprite() {
        this.setParticleTexture(sprite.atlasSprite());
    }

    protected void bindSprite(int index) {
        if (this.sprite instanceof ParticleAtlasSprite) {
            ParticleAtlasSprite atlasSprite = (ParticleAtlasSprite) sprite;
            this.setParticleTexture(atlasSprite.atlasSprite(index));
        }
    }

    @Override
    public int getFXLayer() {
        return 1;
    }
}
