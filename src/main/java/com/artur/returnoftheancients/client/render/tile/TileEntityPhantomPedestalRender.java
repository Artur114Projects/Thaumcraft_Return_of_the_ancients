package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelPhantomPedestal;
import com.artur.returnoftheancients.client.render.item.IItemStackRenderer;
import com.artur.returnoftheancients.tileentity.TileEntityPhantomPedestal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class TileEntityPhantomPedestalRender extends TileEntitySpecialRendererBase<TileEntityPhantomPedestal> implements IItemStackRenderer {
    private final ModelPhantomPedestal modelBase = new ModelPhantomPedestal();

    @Override
    public void doRender(TileEntityPhantomPedestal tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        GlStateManager.color(0.0F, 0.0F, 0.0F, (float) MathHelper.clamp((Math.abs(Math.cos(System.currentTimeMillis())) * 0.1) + 0.8, 0.0F, 1.0F));
        modelBase.renderBase();

        GlStateManager.pushMatrix();
        GlStateManager.color(0.0F, 0.0F, 0.0F, (float) MathHelper.clamp((Math.abs(Math.cos((System.currentTimeMillis() + 1000))) * 0.1) + 0.6, 0.0F, 1.0F));
        GlStateManager.translate(0.0F, Math.cos(System.currentTimeMillis() / 250.0D) * 0.1F, 0.0F);
        modelBase.setClotRotationAngle(0.0F, (float) ((System.currentTimeMillis() / 1000.0D + Math.toRadians(120)) % (Math.PI * 2)), 0.0F);
        modelBase.renderClot();
        GlStateManager.popMatrix();

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        this.defaultTransform(0, 0, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.color(0.0F, 0.0F, 0.0F, (float) MathHelper.clamp((Math.abs(Math.cos(System.currentTimeMillis() / 250.0D)) * 0.4) + 0.6, 0.0F, 1.0F));
        modelBase.renderBase();
        GlStateManager.enableTexture2D();
        this.defaultEnd();
    }
}
