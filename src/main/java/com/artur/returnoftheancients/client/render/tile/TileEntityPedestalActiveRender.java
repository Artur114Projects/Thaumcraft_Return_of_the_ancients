package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelAncientDoorH4x4;
import com.artur.returnoftheancients.client.model.ModelPedestalActive;
import com.artur.returnoftheancients.tileentity.TileEntityPedestalActive;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class TileEntityPedestalActiveRender extends TileEntitySpecialRendererBase<TileEntityPedestalActive> {
    private static final ResourceLocation TEXTURE_BASE = EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("texture");
    private final ModelPedestalActive modelBase = new ModelPedestalActive();

    @Override
    public void doRender(TileEntityPedestalActive tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.disableAlpha();

        GlStateManager.rotate(tile.rotate(), 0.0F, 1.0F, 0.0F);

        this.bindTexture(TEXTURE_BASE);

        modelBase.renderAll();

        GlStateManager.enableAlpha();
    }
}
