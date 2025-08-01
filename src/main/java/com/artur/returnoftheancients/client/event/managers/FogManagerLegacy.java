package com.artur.returnoftheancients.client.event.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.lib.events.RenderEventHandler;

// TODO: 28.02.2025 Переписать
@SideOnly(Side.CLIENT)
public class FogManagerLegacy {
    private final FogParams defaultFog = new FogParams(0, 0, 0, 0);
    private FogParams oldfogParams = null;
    private FogParams newfogParams = null;
    public float fogDuration = 0;
    public int maxFogTome = 80;
    public int fogTime = 80;

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

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();

        if (e.phase != TickEvent.Phase.START || player == null || e.side == Side.SERVER || mc.isGamePaused()) {
            return;
        }

        if (RenderEventHandler.fogDuration != (newfogParams != null ? newfogParams.fodDuration : -1)) {
            fogDuration += ((float) Math.abs((newfogParams != null ? newfogParams.fodDuration : 0) - (oldfogParams != null ? oldfogParams.fodDuration : 0)) / maxFogTome + 0.0F);
            if (fogDuration >= 1) {
                if (RenderEventHandler.fogDuration < (newfogParams != null ? newfogParams.fodDuration : -1)) {
                    RenderEventHandler.fogDuration += (int) fogDuration;
                } else {
                    RenderEventHandler.fogDuration -= (int) fogDuration;
                }
                fogDuration -= (int) fogDuration;
            }
        }

        if (!(newfogParams == null && fogTime == maxFogTome)) {
            RenderEventHandler.fogDuration += 1;
            RenderEventHandler.fogFiddled = true;
        }

        if (fogTime < maxFogTome) {
            fogTime++;
        }
    }

    public void setFogParams(FogParams fog) {
        if (newfogParams == null && fog == null) {
            return;
        }
        float scaleBase = (float) fogTime / maxFogTome;
        FogParams startFog;
        FogParams endFog;

        if (oldfogParams == null) {
            startFog = defaultFog;
        } else {
            startFog = oldfogParams;
        }
        if (newfogParams == null) {
            endFog = defaultFog;
        } else {
            endFog = newfogParams;
        }

        oldfogParams = new FogParams((startFog.r + (endFog.r - startFog.r) * scaleBase), (startFog.g + (endFog.g - startFog.g) * scaleBase), (startFog.b + (endFog.b - startFog.b) * scaleBase), RenderEventHandler.fogDuration);
        newfogParams = fog;
        fogTime = 0;
    }

    @SideOnly(Side.CLIENT)
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
