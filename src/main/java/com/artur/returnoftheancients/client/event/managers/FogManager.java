package com.artur.returnoftheancients.client.event.managers;

import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.handlers.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.lib.events.RenderEventHandler;

@SideOnly(Side.CLIENT)
public class FogManager {
    public final DefaultFog defaultFog = new DefaultFog();
    public FogParams oldFogParams = this.defaultFog;
    public FogParams newFogParams = this.defaultFog;
    public int prevFogTime = 0;
    public float fogCache = 0;
    public int fogTime = 0;


    public void entityViewRenderEventFogColors(EntityViewRenderEvent.FogColors e) {
        if (this.oldFogParams == this.defaultFog && this.newFogParams == this.defaultFog) {
            return;
        }
        if (this.newFogParams == this.defaultFog && this.fogTime == this.newFogParams.fogChangeTime(this.oldFogParams)) {
            return;
        }

        this.defaultFog.update(e);

        if (this.oldFogParams instanceof IDynamicFog) {
            ((IDynamicFog) this.oldFogParams).update(e, this.newFogParams, false);
        }
        if (this.newFogParams instanceof IDynamicFog) {
            ((IDynamicFog) this.newFogParams).update(e, this.oldFogParams, true);
        }

        float deltaPrev = (float) this.prevFogTime / this.newFogParams.fogChangeTime(this.oldFogParams);
        float delta = (float) this.fogTime / this.newFogParams.fogChangeTime(this.oldFogParams);
        this.oldFogParams.mix(this.newFogParams, RenderHandler.interpolate(deltaPrev, delta, (float) e.getRenderPartialTicks())).bind(e);
    }

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();

        if (e.phase != TickEvent.Phase.START || player == null || mc.isGamePaused()) {
            return;
        }

        if (this.oldFogParams instanceof IDynamicFog) {
            ((IDynamicFog) this.oldFogParams).update(player, this.newFogParams, false, this.fogTime);
        }
        if (this.newFogParams instanceof IDynamicFog) {
            this.fogTime = ((IDynamicFog) this.newFogParams).update(player, this.oldFogParams, true, this.fogTime);
        }

        int fogChangeTime = this.newFogParams.fogChangeTime(this.oldFogParams);

        if (RenderEventHandler.fogDuration != this.newFogParams.fogDuration) {
            this.fogCache += (float) Math.abs(this.newFogParams.fogDuration - this.oldFogParams.fogDuration) / fogChangeTime;
            if (this.fogCache > 1) {
                if (RenderEventHandler.fogDuration < this.newFogParams.fogDuration) {
                    RenderEventHandler.fogDuration += (int) this.fogCache;
                    if (RenderEventHandler.fogDuration > this.newFogParams.fogDuration) {
                        RenderEventHandler.fogDuration = this.newFogParams.fogDuration;
                    }
                } else {
                    RenderEventHandler.fogDuration -= (int) this.fogCache;
                    if (RenderEventHandler.fogDuration < this.newFogParams.fogDuration) {
                        RenderEventHandler.fogDuration = this.newFogParams.fogDuration;
                    }
                }
                this.fogCache -= (int) this.fogCache;
            }
        }

        if (!(this.newFogParams == this.defaultFog && this.fogTime == fogChangeTime)) {
            RenderEventHandler.fogDuration += 1;
            RenderEventHandler.fogFiddled = true;
        }

        this.prevFogTime = this.fogTime;
        if (this.fogTime < fogChangeTime) {
            this.fogTime++;
        } else if (this.fogTime > fogChangeTime) {
            this.fogTime = fogChangeTime;
        }
    }

    public void setFogParams(FogParams fog) {
        if (this.newFogParams == this.defaultFog && fog == null) {
            return;
        }
        if (fog == null) {
            fog = this.defaultFog;
        }

        float scaleBase = (float) this.fogTime / this.newFogParams.fogChangeTime(this.oldFogParams);
        MixedFog mix = this.oldFogParams.mix(this.newFogParams, scaleBase);
        this.oldFogParams = this.newFogParams.copy(mix, RenderEventHandler.fogDuration);
        this.newFogParams = fog;
        this.fogTime = 0;
    }

    @SideOnly(Side.CLIENT)
    public static class FogParams {
        protected MixedFog mix = new MixedFog();
        protected int fogChangeTime = 80;
        protected int fogDuration;
        protected float r;
        protected float g;
        protected float b;

        protected FogParams() {}

        public FogParams(float r, float g, float b, int fogDuration) {
            this.fogDuration = fogDuration;
            this.r = r / 255.0F;
            this.b = b / 255.0F;
            this.g = g / 255.0F;
        }

        public FogParams(MixedFog fog, int fogDuration) {
            this.fogDuration = fogDuration;
            this.r = fog.r;
            this.b = fog.b;
            this.g = fog.g;
        }

        public FogParams copy(MixedFog fog, int fogDuration) {
            return new FogParams(fog, fogDuration);
        }

        public MixedFog mix(FogParams end, float delta) {
            this.mix.setR(RenderHandler.interpolate(this.r, end.r, delta));
            this.mix.setG(RenderHandler.interpolate(this.g, end.g, delta));
            this.mix.setB(RenderHandler.interpolate(this.b, end.b, delta));
            return this.mix;
        }

        public int fogChangeTime(FogParams oldFog) {
            return this.fogChangeTime;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class MixedFog {
        private float r;
        private float g;
        private float b;

        public void setR(float r) {
            this.r = r;
        }

        public void setG(float g) {
            this.g = g;
        }

        public void setB(float b) {
            this.b = b;
        }

        protected void bind(EntityViewRenderEvent.FogColors e) {
            e.setRed(this.r);
            e.setGreen(this.g);
            e.setBlue(this.b);
        }
    }

    public static class DefaultFog extends FogParams {
        public DefaultFog() {
            this.fogDuration = -1;
        }

        protected void update(EntityViewRenderEvent.FogColors e) {
            this.r = e.getRed();
            this.g = e.getGreen();
            this.b = e.getBlue();
        }
    }

    public interface IDynamicFog {
        int update(EntityPlayer player, FogParams fog, boolean isNew, int fogTime);
        void update(EntityViewRenderEvent.FogColors e, FogParams fog, boolean isNew);
    }
}
