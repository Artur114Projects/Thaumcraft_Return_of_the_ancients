package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelAncientProjector;
import com.artur.returnoftheancients.tileentity.TileEntityAncientProjector;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class TileEntityAncientProjectorRender extends TileEntitySpecialRendererBase<TileEntityAncientProjector> implements IItemStackRenderer {
    private static final ResourceLocation TEXTURE_BASE = EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("ancient_projector");
    private final ModelAncientProjector modelBase = new ModelAncientProjector();

    @Override
    public void doRender(TileEntityAncientProjector tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        this.bindTexture(TEXTURE_BASE);
        modelBase.renderAll();
        this.renderProject(tile);
    }

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        this.defaultTransform(0, 0, 0);
        this.bindTexture(TEXTURE_BASE);
        modelBase.renderAll();
        this.defaultEnd();
    }


    private void renderProject(TileEntityAncientProjector tile) {
        if (!tile.isEnabled()) {
            return;
        }

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        GL11.glShadeModel(7425);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        GlStateManager.translate(0, 1.5, 0);

        float alphaStart = (float) MathHelper.clamp(Math.abs(Math.cos(System.currentTimeMillis()) * 0.3) + 0.6, 0.0F, 1.0F);
        float toY = tile.distanceToPedestal();
        float alphaEnd = 0.1F;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos((0.5 + (1.0F / 16.0F)), toY, -(0.5 + (1.0F / 16.0F))).color(0, 0, 0, alphaEnd).endVertex();
        bufferBuilder.pos(-(0.5 + (1.0F / 16.0F)), toY, -(0.5 + (1.0F / 16.0F))).color(0, 0, 0, alphaEnd).endVertex();
        bufferBuilder.pos(0, -(8.0F / 16.0F), 0).color(0, 0, 0, alphaStart).endVertex();
        tessellator.draw();

        bufferBuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos((0.5 + (1.0F / 16.0F)), toY, 0.5 + (1.0F / 16.0F)).color(0, 0, 0, alphaEnd).endVertex();
        bufferBuilder.pos(-(0.5 + (1.0F / 16.0F)), toY, 0.5 + (1.0F / 16.0F)).color(0, 0, 0, alphaEnd).endVertex();
        bufferBuilder.pos(0, -(8.0F / 16.0F), 0).color(0, 0, 0, alphaStart).endVertex();
        tessellator.draw();

        bufferBuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(0.5 + (1.0F / 16.0F), toY, -(0.5 + (1.0F / 16.0F))).color(0, 0, 0, alphaEnd).endVertex();
        bufferBuilder.pos(0.5 + (1.0F / 16.0F), toY, (0.5 + (1.0F / 16.0F))).color(0, 0, 0, alphaEnd).endVertex();
        bufferBuilder.pos(0, -(8.0F / 16.0F), 0).color(0, 0, 0, alphaStart).endVertex();
        tessellator.draw();

        bufferBuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(-(0.5 + (1.0F / 16.0F)), toY, -(0.5 + (1.0F / 16.0F))).color(0, 0, 0, alphaEnd).endVertex();
        bufferBuilder.pos(-(0.5 + (1.0F / 16.0F)), toY, (0.5 + (1.0F / 16.0F))).color(0, 0, 0, alphaEnd).endVertex();
        bufferBuilder.pos(0, -(8.0F / 16.0F), 0).color(0, 0, 0, alphaStart).endVertex();
        tessellator.draw();

        GL11.glShadeModel(7424);
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
    }
}
