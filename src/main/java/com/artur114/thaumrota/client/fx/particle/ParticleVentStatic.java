package com.artur114.thaumrota.client.fx.particle;

import com.artur114.bananalib.math.BananaMath;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import thaumcraft.client.fx.particles.FXVent;

import java.lang.reflect.Field;

public class ParticleVentStatic extends FXVent {
    protected final Field psm;

    public ParticleVentStatic(double x, double y, double z, double moveX, double moveY, double moveZ, int color) {
        super(Minecraft.getMinecraft().world, x, y, z, moveX, moveY, moveZ, color);
        Field psm; try {
            psm = FXVent.class.getDeclaredField("psm");
            psm.setAccessible(true);
            psm.set(this, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.psm = psm;
        this.particleScale = 0.5F;
        this.particleMaxAge = 50;
        this.particleAge = 0;
    }

    public ParticleVentStatic(double x, double y, double z, Vec3d move, int color) {
        this(x, y, z, move.x, move.y, move.z, color);
    }

    public void onUpdate() {
        float psm; try {
            psm = (float) this.psm.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.particleAge++;
        if (this.particleScale >= psm) {
            this.setExpired();
        }

        if (this.motionY < 0.03) this.motionY = 0.03;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.85;
        this.motionY *= 0.85;
        this.motionZ *= 0.85;
        if (this.particleAge > 30 && this.particleScale < psm) {
            this.particleScale = BananaMath.lerp(this.particleScale, psm, 0.06);
        }

        if (this.particleScale > psm) {
            this.particleScale = psm;
        }

        if (this.particleAge >= this.particleMaxAge) {
            this.setExpired();
        }

        if (this.onGround) {
            this.motionX *= 0.7F;
            this.motionZ *= 0.7F;
        }
    }
}
