package com.artur.returnoftheancients.client.fx.particle;

import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.client.particle.ParticleWaterWake;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleWaterBubbleDyn extends ParticleBubble {
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();
    public ParticleWaterBubbleDyn(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
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

    }
}
