package com.artur.returnoftheancients.client.fx.particle;

import com.artur.returnoftheancients.client.fx.particle.util.ParticleSprite;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitParticleSprite;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleHeat extends ParticleBase<ParticleSprite> {
    private boolean axis;
    private double angle;
    public ParticleHeat(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);

        this.setSprite(InitParticleSprite.PARTICLE_QUAD_GENERIC);
        this.bindSprite();

        this.particleRed = 207.0F / 255.0F;
        this.particleGreen = 73.0F / 255.0F + ((this.rand.nextInt(16 + 1) - 8) / 255.0F);
        this.particleBlue = 10.0F / 255.0F;

        this.particleAlpha = MathHelper.clamp((this.rand.nextFloat() * 0.4F) + 0.6F, 0.0F, 1.0F);
        this.particleScale = 4.0F / (16.0F + rand.nextFloat() / 2);
        this.motionY = 0.15 + ((this.rand.nextDouble() / 10) - 0.05);
        this.axis = this.rand.nextBoolean();
        this.particleMaxAge = 40;
        this.canCollide = false;
    }

    @Override
    public int getBrightnessForRender(float p_189214_1_) {
        return 240;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        float rotateSpeed = 0.1F;
        float speed = 0.06F;

        if (axis) {
            this.motionX = speed * Math.sin(this.angle);
            this.motionZ = speed * Math.cos(this.angle);
        } else {
            this.motionX = speed * Math.cos(this.angle);
            this.motionZ = speed * Math.sin(this.angle);
        }

        this.angle += rotateSpeed;
    }
}
