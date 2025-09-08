package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.event.ClientEventsHandler;
import com.artur.returnoftheancients.client.model.ModelAncientFan;
import com.artur.returnoftheancients.client.model.ModelIncinerator;
import com.artur.returnoftheancients.tileentity.TileEntityIncinerator;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityIncineratorRender extends TileEntitySpecialRendererBase<TileEntityIncinerator> implements IItemStackRenderer {
    private static final ResourceLocation TEXTURE_LAVA = EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("big_lava_still");
    private static final ResourceLocation TEXTURE_BASE = EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("incinerator");
    private final ModelIncinerator modelBase = new ModelIncinerator();

    @Override
    public void doRender(TileEntityIncinerator tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
//        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        this.bindTexture(TEXTURE_LAVA);
        this.modelBase.setLavaBoxIndex((int) (ClientEventsHandler.GLOBAL_TICK_MANAGER.gameTickCounter / 10.0F));
        this.modelBase.renderLava();

        this.bindTexture(TEXTURE_BASE);
        this.modelBase.renderBase();
        this.modelBase.renderJetBase();
        this.modelBase.renderJet();

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.modelBase.renderGlass();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        this.defaultTransform(0.0, 0.0, 0.0);

        this.bindTexture(TEXTURE_BASE);
        this.modelBase.renderBase();
        this.modelBase.renderJetBase();
        this.modelBase.renderLava();
        this.modelBase.renderJet();

        this.defaultEnd();
    }
}
