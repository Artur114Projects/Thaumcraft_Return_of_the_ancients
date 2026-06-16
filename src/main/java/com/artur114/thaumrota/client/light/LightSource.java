package com.artur114.thaumrota.client.light;

import com.artur114.bananalib.math.m3d.vec.IVec3DM;

import java.awt.*;

public class LightSource {
    private float brightness;
    private float range;
    private Color color;
    private IVec3DM pos;

    public LightSource(Color color, IVec3DM pos, float range, float brightness) {
        this.color = color;
        this.pos = pos;
        this.range = range;
        this.brightness = brightness;
    }

    public float brightness() {
        return brightness;
    }

    public float range() {
        return range;
    }

    public Color color() {
        return color;
    }

    public IVec3DM pos() {
        return pos;
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

    public void setPos(IVec3DM pos) {
        this.pos = pos;
    }
}
