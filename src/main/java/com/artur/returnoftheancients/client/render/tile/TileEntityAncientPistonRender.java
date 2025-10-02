package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.event.ClientEventsHandler;
import com.artur.returnoftheancients.client.model.ModelAncientFan;
import com.artur.returnoftheancients.client.model.ModelAncientPiston;
import com.artur.returnoftheancients.tileentity.TileEntityAncientPiston;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityAncientPistonRender extends TileEntitySpecialRendererBase<TileEntityAncientPiston> implements IItemStackRenderer {
    private static final ResourceLocation TEXTURE_BASE = EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("ancient_piston");
    private final ModelAncientPiston modelBase = new ModelAncientPiston();

    @Override
    public void doRender(TileEntityAncientPiston tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        this.bindTexture(TEXTURE_BASE);
        this.modelBase.renderBase();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, -(-12.0F / 16.0F - (12.0F / 16.0F * MathHelper.cos((float) ((Math.PI * ((ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedGameTickCounter(partialTicks) % 40.0F) / 40.0F)) + (Math.PI / 2.0F))))), 0.0F);
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
