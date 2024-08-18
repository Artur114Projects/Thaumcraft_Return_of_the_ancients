package com.artur.returnoftheancients.client;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TrapParticleFlame extends ParticleFlame {

    public TrapParticleFlame(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        Vec3d start = new Vec3d(posX, posY, posZ);
        Vec3d end = start.add(new Vec3d(motionX, motionY, motionZ));
        RayTraceResult result = world.rayTraceBlocks(start, end);

        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            motionX = rand.nextInt(2) == 0 ? rand.nextDouble() / 20 : (rand.nextDouble() / 20) * -1;
            motionY = -motionY / 10;
            motionZ = rand.nextInt(2) == 0 ? rand.nextDouble() / 20 : (rand.nextDouble() / 20) * -1;
        }
    }
}
