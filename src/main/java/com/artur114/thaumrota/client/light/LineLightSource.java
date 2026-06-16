package com.artur114.thaumrota.client.light;

import com.artur114.bananalib.math.m3d.vec.IVec3DM;

import java.awt.*;

public class LineLightSource {
    private float brightness;
    private float range;
    private Color color;
    private IVec3DM from;
    private IVec3DM to;

    public LineLightSource(float brightness, float range, Color color, IVec3DM from, IVec3DM to) {
        this.brightness = brightness;
        this.range = range;
        this.color = color;
        this.from = from;
        this.to = to;
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

    public IVec3DM from() {
        return from;
    }

    public IVec3DM to() {
        return to;
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

    public void setFrom(IVec3DM from) {
        this.from = from;
    }

    public void setTo(IVec3DM to) {
        this.to = to;
    }
}
