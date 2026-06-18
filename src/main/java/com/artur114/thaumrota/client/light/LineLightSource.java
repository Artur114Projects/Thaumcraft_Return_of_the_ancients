package com.artur114.thaumrota.client.light;

import com.artur114.bananalib.math.m3d.vec.IVec3DM;
import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

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
                buffer.put(1.0F / (this.range * this.range));
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
        }
    }

    @Override
    public boolean isOnView(int maxRenderDist) {
        return BananaMC.isInPlayerView(this.from, maxRenderDist) || BananaMC.isInPlayerView(this.to, maxRenderDist);
    }

    @Override
    public boolean collideToPos(BlockPos pos) {
        return this.from.equals(pos) || this.to.equals(pos);
    }

    @Override
    public int distanceSqToPlayer() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        return (int) (this.from.distanceSq(player.posX, player.posY, player.posZ) + this.to.distanceSq(player.posX, player.posY, player.posZ)) / 2;
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
