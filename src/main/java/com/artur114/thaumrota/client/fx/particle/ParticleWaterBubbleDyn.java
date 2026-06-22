package com.artur114.thaumrota.client.fx.particle;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.world.World;

public class ParticleWaterBubbleDyn extends ParticleBubble {
    private final PosMc3IM blockPos = new PosMc3IM();
    private final boolean clockwise;
    private double angle;

    public ParticleWaterBubbleDyn(World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        this.clockwise = this.rand.nextBoolean();
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY += 0.06D;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.blockPos.set(this.posX, this.posY, this.posZ);
        this.motionX *= 0.85D;
        this.motionY *= 0.85D;
        this.motionZ *= 0.85D;

        if (this.world.getBlockState(this.blockPos).getMaterial() != Material.WATER) {
            this.setExpired();
        }

        float rotateSpeed = 1.0F;
        float radius = 0.04F;

        if (this.clockwise) {
            this.motionX = radius * Math.sin(this.angle);
            this.motionZ = radius * Math.cos(this.angle);
        } else {
            this.motionX = radius * Math.cos(this.angle);
            this.motionZ = radius * Math.sin(this.angle);
        }

        this.angle += rotateSpeed;
    }
}
