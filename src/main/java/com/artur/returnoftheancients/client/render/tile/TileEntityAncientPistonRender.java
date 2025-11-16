package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelAncientPiston;
import com.artur.returnoftheancients.client.render.item.IItemStackRenderer;
import com.artur.returnoftheancients.tileentity.TileEntityAncientPiston;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityAncientPistonRender extends TileEntitySpecialRendererBase<TileEntityAncientPiston> implements IItemStackRenderer {
    private static final ResourceLocation TEXTURE_BASE = EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("ancient_piston");
    private final ModelAncientPiston modelBase = new ModelAncientPiston();

    @Override
    public void doRender(TileEntityAncientPiston tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (tile.face() == EnumFacing.DOWN) {
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -2.0F, 0.0F);
        }
        if (tile.face().getAxis().isHorizontal()) {
            GlStateManager.translate(0.0F, 1.0F, 0.0F);
            GlStateManager.translate(1.0F * (tile.face().getFrontOffsetX() * -1), 0.0F, 1.0F * (tile.face().getFrontOffsetZ() * -1));
            if (tile.face().getAxis() == EnumFacing.Axis.Z) GlStateManager.translate(0.0F, 0.0F, 2.0F * (tile.face().getAxisDirection().getOffset()));
            GlStateManager.rotate(90.0F * (tile.face().getAxisDirection().getOffset() * -1), 1.0F * Math.abs(tile.face().getFrontOffsetZ()), 0.0F, 1.0F * Math.abs(tile.face().getFrontOffsetX()));
        }

        GlStateManager.rotate(90.0F * (tile.getPos().hashCode() % 4), 0.0F, 1.0F, 0.0F);

        this.bindTexture(TEXTURE_BASE);
        this.modelBase.renderBase();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, -(-9.0F / 16.0F - (9.0F / 16.0F * MathHelper.cos((float) ((Math.PI * tile.moveProcess(40.0F)) + (Math.PI / 2.0F))))), 0.0F);
        this.modelBase.renderPiston();
        GlStateManager.popMatrix();
    }

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        this.defaultTransform(0.0F, 0.0F, 0.0F);
        this.bindTexture(TEXTURE_BASE);
        this.modelBase.renderBase();
        this.modelBase.renderPiston();
        this.defaultEnd();
    }
}
