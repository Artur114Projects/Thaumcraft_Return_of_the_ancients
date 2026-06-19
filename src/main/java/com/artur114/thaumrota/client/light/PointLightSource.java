package com.artur114.thaumrota.client.light;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.nio.FloatBuffer;

public class PointLightSource implements ILightSource {
    private PosMc3IM pos;
    private Color color;
    private float brightness;
    private float range;
    private float heat;

    public PointLightSource(PosMc3IM pos, Color color, float brightness, float range, float heat) {
        this.pos = pos;
        this.color = color;
        this.brightness = brightness;
        this.range = range;
        this.heat = heat;
    }

    public PointLightSource setPos(PosMc3IM pos) {
        this.pos = pos; return this;
    }

    public PointLightSource setColor(Color color) {
        this.color = color; return this;
    }

    public PointLightSource setBrightness(float brightness) {
        this.brightness = brightness; return this;
    }

    public PointLightSource setRange(float range) {
        this.range = range; return this;
    }

    public PointLightSource setHeat(float heat) {
        this.heat = heat; return this;
    }

    public PosMc3IM pos() {
        return this.pos;
    }

    @Override
    public float heat() {
        return this.heat;
    }

    @Override
    public Color color() {
        return this.color;
    }

    @Override
    public float range() {
        return this.range;
    }

    @Override
    public float brightness() {
        return this.brightness;
    }

    @Override
    public EnumLightType type() {
        return EnumLightType.POINT;
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
                buffer.put((float) (this.pos.x() + 0.5F - Particle.interpPosX));
                buffer.put((float) (this.pos.y() + 0.5F - Particle.interpPosY));
                buffer.put((float) (this.pos.z() + 0.5F - Particle.interpPosZ));
                break;
        }
    }

    @Override
    public boolean isOnView(int maxRenderDist) {
        return BananaMC.isInPlayerView(this.pos, maxRenderDist);
    }

    @Override
    public boolean collideToPos(BlockPos pos) {
        return this.pos.equals(pos);
    }

    @Override
    public int distanceSqToPlayer() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        return (int) this.pos.distanceSq(player.posX, player.posY, player.posZ);
    }

    @Override
    public String toString() {
        return String.valueOf(this.pos);
    }
}
