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
                GlStateManager.rotate(90, 0, 0 ,1);
                GlStateManager.translate(1, -1, 0);
                break;
            case Z:
                GlStateManager.rotate(90, 1, 0 ,0);
                GlStateManager.translate(0, -1, -1);
                break;
        }
        float spinSpeed = 20.0F / tile.spinSpeed(partialTicks);
        this.bindTexture(TEXTURE_BASE);
        this.modelBase.setFanRotate((float) ((Math.PI * 2) * ((ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedGameTickCounter(partialTicks) % spinSpeed) / spinSpeed)));
        this.modelBase.renderAll();
        GlStateManager.popMatrix();
    }

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        this.defaultTransform(0, 0, 0);
        this.bindTexture(TEXTURE_BASE);
        this.modelBase.setFanRotate(0.0F);
        this.modelBase.renderAll();
        this.defaultEnd();
    }
}
