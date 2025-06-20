package com.artur.returnoftheancients.client.fx.particle;

import com.artur.returnoftheancients.client.fx.particle.util.ParticleSprite;
import com.artur.returnoftheancients.init.InitParticleSprite;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticlePhantom extends ParticleBase<ParticleSprite> {
    private final float finalMotionY = (this.rand.nextFloat() * 0.15F) + 0.05F;

    public ParticlePhantom(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);

        this.setSprite(InitParticleSprite.PARTICLE_QUAD_GENERIC);
        this.bindSprite();

        this.particleGravity = 0;
        this.canCollide = false;

        this.particleRed = 0;
        this.particleGreen = 0;
        this.particleBlue = 0;

        this.particleScale = 4.0F / (16.0F + rand.nextFloat() / 2);
    }

    public ParticlePhantom(World worldIn, double posXIn, double posYIn, double posZIn, Vec3d vec3d, int livingTime) {
        super(worldIn, posXIn, posYIn, posZIn, vec3d.x, vec3d.y, vec3d.z);

        this.setSprite(InitParticleSprite.PARTICLE_QUAD_GENERIC);
        this.bindSprite();

        this.particleGravity = 0;
        this.canCollide = false;

        this.particleRed = 0;
        this.particleGreen = 0;
        this.particleBlue = 0;

        this.particleScale = 2.0F / (16.0F + rand.nextFloat() / 2);

        this.motionX = vec3d.x;
        this.motionZ = vec3d.z;

        if (livingTime != -1) {
            this.particleMaxAge = livingTime;
        }
    }

    @Override
    public void onUpdate() {
        if (this.motionY < this.finalMotionY) {
            this.motionY = this.finalMotionY;
        }

        super.onUpdate();
    }
}
