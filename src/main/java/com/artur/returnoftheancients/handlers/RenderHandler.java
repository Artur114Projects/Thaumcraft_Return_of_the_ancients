package com.artur.returnoftheancients.handlers;

public class RenderHandler {
    public static float interpolate(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    public static double interpolate(double start, double end, float pct) {
        return start + (end - start) * pct;
    }
}
