package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.tileentity.TileEntityAncientDoor4X3;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;

public class TileEntitySpecialRendererBase<T extends TileEntity> extends TileEntitySpecialRenderer<T> {
    public void defaultTransform(double x, double y, double z) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);

        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
    }

    public void defaultEnd() {
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public void doRender(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {}

    @Override
    public void render(@NotNull T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        this.defaultTransform(x, y, z);
        this.doRender(te, x, y, z, partialTicks, destroyStage, alpha);
        this.defaultEnd();
    }
}
