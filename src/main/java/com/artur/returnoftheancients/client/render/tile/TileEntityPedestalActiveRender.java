package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelPedestalActive;
import com.artur.returnoftheancients.tileentity.TileEntityPedestalActive;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityPedestalActiveRender extends TileEntitySpecialRendererBase<TileEntityPedestalActive> {
    private static final ResourceLocation TEXTURE_BASE = EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("pedestal_active");
    private final ModelPedestalActive modelBase = new ModelPedestalActive();

    @Override
    public void doRender(TileEntityPedestalActive tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.rotate(tile.rotate(), 0.0F, 1.0F, 0.0F);

        this.bindTexture(TEXTURE_BASE);

        modelBase.renderAll();

        if (tile.isActive()) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.SRC_ALPHA);
            GlStateManager.disableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, (float) MathHelper.clamp((Math.abs(Math.cos(System.currentTimeMillis() / 250.0D)) * 0.4) + 0.6, 0.0F, 1.0F));

            modelBase.renderTablet();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableLighting();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
        }
    }
}
