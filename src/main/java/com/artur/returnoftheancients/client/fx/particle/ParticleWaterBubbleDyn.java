package com.artur.returnoftheancients.client.fx.particle;

import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.world.World;

public class ParticleWaterBubbleDyn extends ParticleBubble {
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();
    private boolean axis;
    private double angle;
    public ParticleWaterBubbleDyn(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.axis = this.rand.nextBoolean();
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY += 0.06D;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.blockPos.setPos(this.posX, this.posY, this.posZ);
        this.motionX *= 0.85D;
        this.motionY *= 0.85D;
        this.motionZ *= 0.85D;

        if (this.world.getBlockState(this.blockPos).getMaterial() != Material.WATER) {
            this.setExpired();
        }

        float rotateSpeed = 1.0F;
        float radius = 0.04F;

        if (axis) {
            this.motionX = radius * Math.sin(this.angle);
            this.motionZ = radius * Math.cos(this.angle);
        } else {
            this.motionX = radius * Math.cos(this.angle);
            this.motionZ = radius * Math.sin(this.angle);
        }

        this.angle += rotateSpeed;
    }
}
