package com.artur114.thaumrota.client.light;

import com.artur114.bananalib.math.core.m3d.vec.IVec3IC;
import com.artur114.bananalib.math.m3d.vec.IVec3DM;
import com.artur114.bananalib.math.m3d.vec.Vec3DM;
import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.client.render.fx.HeatRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.nio.FloatBuffer;

public class LineLightSource implements ILightSource {
    private PosMc3IM from;
    private PosMc3IM to;
    private Color color;
    private float brightness;
    private float range;
    private float heat;

    public LineLightSource(PosMc3IM from, PosMc3IM to, Color color, float brightness, float range, float heat) {
        this.brightness = brightness;
        this.range = range;
        this.color = color;
        this.from = from;
        this.heat = heat;
        this.to = to;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setHeat(float heat) {
        this.heat = heat;
    }

    public void setFrom(PosMc3IM from) {
        this.from = from;
    }

    public void setTo(PosMc3IM to) {
        this.to = to;
    }

    @Override
    public float brightness() {
        return this.brightness;
    }

    @Override
    public float range() {
        return this.range;
    }

    @Override
    public float heat() {
        return this.heat;
    }

    @Override
    public EnumLightType type() {
        return EnumLightType.LINE;
    }

    @Override
    public void writeToBuff(int pass, FloatBuffer buffer) {
        switch (pass) {
            case 0:
                buffer.put(this.color.getRed() / 255.0F);
                buffer.put(this.color.getGreen() / 255.0F);
                buffer.put(this.color.getBlue() / 255.0F);
                break;
            case 1:
                buffer.put(this.range * this.range);
                buffer.put(this.brightness);
                buffer.put(this.heat);
                break;
            case 2:
                buffer.put((float) (this.from.x() + 0.5F - Particle.interpPosX));
                buffer.put((float) (this.from.y() + 0.5F - Particle.interpPosY));
                buffer.put((float) (this.from.z() + 0.5F - Particle.interpPosZ));
                break;
            case 3:
                buffer.put((float) (this.to.x() + 0.5F - Particle.interpPosX));
                buffer.put((float) (this.to.y() + 0.5F - Particle.interpPosY));
                buffer.put((float) (this.to.z() + 0.5F - Particle.interpPosZ));
                break;
            case 4:
                buffer.put((float) (Math.min(this.from.x(), this.to.x()) + 0.5F - (this.range * 1.8) - Particle.interpPosX));
                buffer.put((float) (Math.min(this.from.y(), this.to.y()) + 0.5F - (this.range * 1.8) - Particle.interpPosY));
                buffer.put((float) (Math.min(this.from.z(), this.to.z()) + 0.5F - (this.range * 1.8) - Particle.interpPosZ));
                break;
            case 5:
                buffer.put((float) (Math.max(this.from.x(), this.to.x()) + 0.5F + (this.range * 1.8) - Particle.interpPosX));
                buffer.put((float) (Math.max(this.from.y(), this.to.y()) + 0.5F + (this.range * 1.8) - Particle.interpPosY));
                buffer.put((float) (Math.max(this.from.z(), this.to.z()) + 0.5F + (this.range * 1.8) - Particle.interpPosZ));
                break;
            case 6:
                Vec3DM vec = Vec3DM.obtain();
                vec.set(this.to).subtract(this.from);
                buffer.put((float) (1.0F / vec.lengthSq()));
                Vec3DM.release(vec);
                break;
        }
    }

    @Override
    public boolean isOnView(int maxRenderDist) {
        if (this.distanceSqToPlayer() > maxRenderDist * maxRenderDist) {
            return false;
        }

        double minX = Math.min(this.from.x(), this.to.x()) - this.range;
        double minY = Math.min(this.from.y(), this.to.y()) - this.range;
        double minZ = Math.min(this.from.z(), this.to.z()) - this.range;
        double maxX = Math.max(this.from.x(), this.to.x()) + this.range;
        double maxY = Math.max(this.from.y(), this.to.y()) + this.range;
        double maxZ = Math.max(this.from.z(), this.to.z()) + this.range;

        return HeatRenderer.FRUSTUM.isBoxInFrustum(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public boolean collideToPos(BlockPos pos) {
        return this.from.equals(pos) || this.to.equals(pos);
    }

    @Override
    public int distanceSqToPlayer() {
        Vec3DM ab = (Vec3DM) Vec3DM.obtain().set(this.to).subtract(this.from);
        Vec3DM ap = (Vec3DM) Vec3DM.obtain().set(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ).subtract(this.from);
        double t = MathHelper.clamp((ap.dot(ab) / ab.dot(ab)), 0.0, 1.0);
        Vec3DM closest = (Vec3DM) Vec3DM.obtain().set(this.from).add(ab.scale(t));
        double dist = closest.distanceSq(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ);
        Vec3DM.release(ab);
        Vec3DM.release(ap);
        Vec3DM.release(closest);
        return (int) dist;
    }

    public Color color() {
        return this.color;
    }

    public PosMc3IM from() {
        return this.from;
    }

    public PosMc3IM to() {
        return this.to;
    }

    @Override
    public String toString() {
        return this.from + " -> " + this.to;
    }
}
