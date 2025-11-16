package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelAncientDoor4X3;
import com.artur.returnoftheancients.client.render.item.IItemStackRenderer;
import com.artur.returnoftheancients.tileentity.TileEntityAncientDoor4X3;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityAncientDoor4X3Render extends TileEntitySpecialRendererBase<TileEntityAncientDoor4X3> implements IItemStackRenderer {
    private static final ResourceLocation TEXTURE_BASE = EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("ancient_door_4x3");
    private final ModelAncientDoor4X3 modelBase = new ModelAncientDoor4X3();

    @Override
    public void render(TileEntityAncientDoor4X3 te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);

        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (te.axis() == EnumFacing.Axis.X) GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.disableAlpha();

        this.bindTexture(TEXTURE_BASE);

        modelBase.renderArch();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, -1.5F * (1 - te.doorMoveProgress(partialTicks)));
        modelBase.renderDoor1();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 1.5F * (1 - te.doorMoveProgress(partialTicks)));
        modelBase.renderDoor2();
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
        GlStateManager.enableAlpha();
    }

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        this.defaultTransform(0, 0 ,0);
        this.bindTexture(TEXTURE_BASE);
        modelBase.renderArch();
        modelBase.renderDoor1();
        modelBase.renderDoor2();
        this.defaultEnd();
    }
}
