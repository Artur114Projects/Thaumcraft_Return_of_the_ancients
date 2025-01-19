package com.artur.returnoftheancients.client.misc.eventmanagers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import thaumcraft.client.lib.events.RenderEventHandler;

// TODO: Доделать
public class ThaumcraftFogFixer {

    public void entityViewRenderEventRenderFogEvent(EntityViewRenderEvent.RenderFogEvent e) {
        if (RenderEventHandler.fogFiddled && RenderEventHandler.fogTarget > 0.0F) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(RenderEventHandler.fogTarget);
        }
    }
}
