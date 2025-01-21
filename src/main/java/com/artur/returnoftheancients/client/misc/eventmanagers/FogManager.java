package com.artur.returnoftheancients.client.misc.eventmanagers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.lib.events.RenderEventHandler;

// TODO: Добавить ожидание перед изменением тумана
@SideOnly(Side.CLIENT)
public class FogManager {
    private final FogParams defaultFog = new FogParams(0, 0, 0, 0);
    private FogParams oldfogParams = null;
    private FogParams newfogParams = null;
    public float fogDuration = 0;
    public int maxFogTome = 80;
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

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (e.phase != TickEvent.Phase.START || player == null || e.side == Side.SERVER) {
            return;
        }

        if (RenderEventHandler.fogDuration < (newfogParams != null ? newfogParams.fodDuration : -1)) {
            fogDuration += ((float) Math.abs((newfogParams != null ? newfogParams.fodDuration : 0) - (oldfogParams != null ? oldfogParams.fodDuration : 0)) / maxFogTome + 0.0F);
            if (fogDuration >= 1) {
                RenderEventHandler.fogDuration += (int) fogDuration;
                fogDuration -= (int) fogDuration;
            }
            RenderEventHandler.fogDuration += 1;
            RenderEventHandler.fogFiddled = true;
        } else if (RenderEventHandler.fogDuration == (newfogParams != null ? newfogParams.fodDuration : -1)) {
            RenderEventHandler.fogDuration++;
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
