package com.artur.returnoftheancients.client.particle;

import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class RotateParticleSmoke extends ParticleSmokeNormal {

    protected static double staticAngle = 0;

    protected double angle;
    protected final double radius;
    protected final double angularVelocity;
    protected final double centerX;
    protected final double centerZ;

    protected RotateParticleSmoke(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double radius, double angularVelocity) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0, 1);
        this.radius = radius;
        this.angularVelocity = angularVelocity;
        this.particleMaxAge *= 3;

        this.centerX = xCoordIn + 0.5D;
        this.centerZ = yCoordIn + 0.5D;

        if (staticAngle >= 360) {
            staticAngle =- 360;
        }
        this.angle = staticAngle;

        this.posX = centerX + radius * Math.cos(this.angle);
        this.posZ = centerZ + radius * Math.sin(this.angle);
        staticAngle++;
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        int preAge = particleAge;
        particleAge = 10;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        particleAge = preAge;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.angle += this.angularVelocity;

        this.posX = centerX + radius * Math.cos(this.angle);
        this.posZ = centerZ + radius * Math.sin(this.angle);
    }
}
