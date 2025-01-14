package com.artur.returnoftheancients.client.fx.particle;

import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class RotateParticleSmokeInPlayer extends ParticleSmokeNormal {
    protected final EntityPlayer player;

    protected static double staticAngle = 0;

    protected double angle;
    protected final double radius;
    protected final double angularVelocity;
    protected final double centerX;
    protected final double centerZ;
    protected final double yOffset;


    public RotateParticleSmokeInPlayer(World worldIn, double radius, double angularVelocity, EntityPlayer player, double yOffset) {
        super(worldIn, player.posX, player.posY, player.posZ, 0, 0, 0, 1);
        this.player = player;
        this.radius = radius;
        this.yOffset = yOffset;
        this.angularVelocity = angularVelocity;
        this.particleMaxAge *= 3;

        this.centerX = 0.5D;
        this.centerZ = 0.5D;

        if (staticAngle >= 360) {
            staticAngle =- 360;
        }
        this.angle = staticAngle;

        this.posX = player.posX + radius * Math.cos(this.angle);
        this.posZ = player.posZ + radius * Math.sin(this.angle);
        staticAngle++;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.angle += this.angularVelocity;
        this.posY = player.posY + yOffset;

        this.posX = player.posX + radius * Math.cos(this.angle);
        this.posZ = player.posZ + radius * Math.sin(this.angle);
    }
}
