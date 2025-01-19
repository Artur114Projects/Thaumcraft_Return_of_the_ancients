package com.artur.returnoftheancients.client.misc.eventmanagers;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.client.lib.events.RenderEventHandler;

public class FogManager {
    private final FogParams defaultFog = new FogParams(0, 0, 0, 0);
    private FogParams oldfogParams = null;
    private FogParams newfogParams = null;
    public int maxFogTome = 160;
    public int fogTime = 0;

    public void entityViewRenderEventFogColors(EntityViewRenderEvent.FogColors e) {
        if (oldfogParams == null && newfogParams == null) {
            return;
        }
        if (!(newfogParams == null && fogTime == maxFogTome)) {
            float scaleBase = (float) fogTime / maxFogTome;
            FogParams startFog;
            FogParams endFog;

            if (oldfogParams == null) {
                defaultFog.set(e);
                startFog = defaultFog;
            } else {
                startFog = oldfogParams;
            }
            if (newfogParams == null) {
                defaultFog.set(e);
                endFog = defaultFog;
            } else {
                endFog = newfogParams;
            }

            e.setRed((startFog.r + (endFog.r - startFog.r) * scaleBase) / 255.0F);
            e.setGreen((startFog.g + (endFog.g - startFog.g) * scaleBase) / 255.0F);
            e.setBlue((startFog.b + (endFog.b - startFog.b) * scaleBase) / 255.0F);
        }
    }

    public void tickEventPlayerTickEvent(TickEvent.PlayerTickEvent e) {
        if (RenderEventHandler.fogDuration < (newfogParams != null ? newfogParams.fodDuration : 0)) {
            RenderEventHandler.fogDuration++;
            RenderEventHandler.fogFiddled = true;
        }
        if (fogTime < maxFogTome) {
            fogTime++;
        }
    }

    public void setFogParams(FogParams fog) {
        oldfogParams = newfogParams;
        newfogParams = fog;
        fogTime = 0;
    }

    public static class FogParams {
        public int fodDuration;
        public float r;
        public float g;
        public float b;

        public FogParams(float r, float g, float b, int fodDuration) {
            this.fodDuration = fodDuration;
            this.r = r;
            this.b = b;
            this.g = g;
        }

        public void set(EntityViewRenderEvent.FogColors e) {
            this.r = e.getRed() * 255.0F;
            this.g = e.getGreen() * 255.0F;
            this.b = e.getBlue() * 255.0F;
        }
    }
}
