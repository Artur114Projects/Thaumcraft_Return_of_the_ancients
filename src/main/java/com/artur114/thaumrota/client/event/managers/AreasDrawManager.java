package com.artur114.thaumrota.client.event.managers;

import com.artur114.thaumrota.common.util.math.IArea;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class AreasDrawManager {
    public final List<IRenderEntry> renderEntries = new ArrayList<>();
    public boolean shouldRender = false;

    public void renderWorldLastEvent(RenderWorldLastEvent e) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player == null || !this.shouldRender) {
            return;
        }

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        for (IRenderEntry entry : this.renderEntries) {
            if (entry.shouldRender()) {
                entry.render();
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player == null || mc.isGamePaused()) {
            return;
        }

        Iterator<IRenderEntry> iterator = this.renderEntries.iterator();

        while (iterator.hasNext()) {
            IRenderEntry entry = iterator.next();

            entry.update();

            this.shouldRender |= entry.shouldRender();

            if (entry.isEnded()) {
                iterator.remove();
            }
        }
    }

    public void renderArea(IArea area, int time) {
        this.render(new AreaRenderEntry(area, time));
    }

    public void render(IRenderEntry entry) {
        this.renderEntries.add(entry);
    }

    public interface IRenderEntry {
        void update();
        boolean isEnded();
        boolean shouldRender();
        void render();
    }

    public static class AreaRenderEntry implements IRenderEntry {
        private final IArea area;
        private int time;

        public AreaRenderEntry(IArea area, int time) {
            this.area = area;
            this.time = time;
        }

        @Override
        public void update() {
            if (this.time > 0) {
                this.time--;
            }
        }

        @Override
        public boolean isEnded() {
            return this.time == 0;
        }

        @Override
        public boolean shouldRender() {
            return true;
        }

        @Override
        public void render() {
            float a = 1.0F;
            if (this.time <= 20) {
                a = this.time / 20.0F;
            }
            this.area.renderArea(a);
        }
    }
}
