package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelAncientDoor8x6;
import com.artur.returnoftheancients.client.render.item.IItemStackRenderer;
import com.artur.returnoftheancients.tileentity.TileEntityAncientDoor8X6;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityAncientDoor8x6Render extends TileEntitySpecialRendererBase<TileEntityAncientDoor8X6> implements IItemStackRenderer {
    private static final ResourceLocation TEXTURE_BASE = EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("ancient_door_8x6");
    private final ModelAncientDoor8x6 modelBase = new ModelAncientDoor8x6();

    @Override
    public void doRender(TileEntityAncientDoor8X6 tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (tile.axis() == EnumFacing.Axis.X) GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
        this.bindTexture(TEXTURE_BASE);

        modelBase.renderArch();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, -(3.5 - (2.0F / 16.0F)) * (1 - tile.doorMoveProgress(partialTicks)));
        modelBase.renderDoor1();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, (3.5 - (2.0F / 16.0F)) * (1 - tile.doorMoveProgress(partialTicks)));
        modelBase.renderDoor2();
        GlStateManager.popMatrix();
    }

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        this.defaultTransform(0, 0, 0);
        this.bindTexture(TEXTURE_BASE);
        modelBase.renderArch();
        modelBase.renderDoor1();
        modelBase.renderDoor2();
        this.defaultEnd();
    }
}
