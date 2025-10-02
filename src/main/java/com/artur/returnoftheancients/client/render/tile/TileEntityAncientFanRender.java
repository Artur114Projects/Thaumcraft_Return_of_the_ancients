package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.event.ClientEventsHandler;
import com.artur.returnoftheancients.client.model.ModelAncientDoorH4x4;
import com.artur.returnoftheancients.client.model.ModelAncientFan;
import com.artur.returnoftheancients.tileentity.TileEntityAncientFan;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TileEntityAncientFanRender extends TileEntitySpecialRendererBase<TileEntityAncientFan> implements IItemStackRenderer {
    private static final ResourceLocation TEXTURE_BASE = EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("ancient_fan");
    private final ModelAncientFan modelBase = new ModelAncientFan();

    @Override
    public void doRender(TileEntityAncientFan tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        switch (tile.axis()) {
            case X:
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(1.0F, -1.0F, 0.0F);
                break;
            case Z:
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.0F, -1.0F, -1.0F);
                break;
        }
        if (tile.isRotated()) GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        float spinSpeed = 20.0F / tile.spinSpeed(partialTicks);
        this.bindTexture(TEXTURE_BASE);
        this.modelBase.setFanRotate((float) ((Math.PI * 2) * ((ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedGameTickCounter(partialTicks) % spinSpeed) / spinSpeed)));
        this.modelBase.renderAll(tile.isClosed());
        GlStateManager.popMatrix();
    }

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        this.defaultTransform(0, 0, 0);
        this.bindTexture(TEXTURE_BASE);
        this.modelBase.setFanRotate(0.0F);
        this.modelBase.renderAll(false);
        this.defaultEnd();
    }
}
