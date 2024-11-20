package com.artur.returnoftheancients.client.particle;

import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RotateParticleFlame extends ParticleFlame {

    private static double staticAngle = 0;

    private double angle;
    private final double radius;
    private final double angularVelocity;
    private final double centerX;
    private final double centerZ;


    public RotateParticleFlame(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double radius, double angularVelocity) {
        super(worldIn, xCoordIn + 0.5, yCoordIn, zCoordIn + 0.5, 0, 0, 0);
        this.radius = radius;
        this.angularVelocity = angularVelocity;
        this.particleMaxAge *= 3;

        this.centerX = xCoordIn + 0.5D;
        this.centerZ = zCoordIn + 0.5D;

        if (staticAngle >= 360) {
            staticAngle =- 360;
        }
        this.angle = staticAngle;

        this.posX = centerX + radius * Math.cos(this.angle);
        this.posZ = centerZ + radius * Math.sin(this.angle);
        staticAngle++;
    }

//    @Override
//    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
//        GlStateManager.enableBlend();
//        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
//        GlStateManager.depthMask(false);
//
//        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
//
//        GlStateManager.depthMask(true);
//        GlStateManager.disableBlend();
//    }

    @Override
    public void onUpdate() {
        if (this.particleAge++ >= this.particleMaxAge)
        {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, motionX / 40, 0.1, motionZ / 40);
        }

        super.onUpdate();
        this.angle += this.angularVelocity;

        this.posX = centerX + radius * Math.cos(this.angle);
        this.posZ = centerZ + radius * Math.sin(this.angle);
    }
}
