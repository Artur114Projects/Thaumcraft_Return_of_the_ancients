package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelEnergyLine;
import com.artur.returnoftheancients.energy.bases.tile.ITileEnergy;
import com.artur.returnoftheancients.tileentity.TileEntityEnergyLine;
import com.artur.returnoftheancients.util.Tuple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class TileEntityEnergyLineRenderer extends TileEntitySpecialRenderer<TileEntityEnergyLine> {

    private final ModelEnergyLine modelBase = new ModelEnergyLine();

    @Override
    public void render(TileEntityEnergyLine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 1, y + 1.5, z + 1);

        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        this.setLightmapDisabled(true);

        float lineAlpha = te.alphaForRender(partialTicks);
        GlStateManager.color(192.0F / 255.0F, 1.0F, 1.0F, lineAlpha);

        modelBase.renderBase();

        for (Tuple<Boolean, Tuple<EnumFacing, ITileEnergy>> neighbor : te.neighbors) {
            float localAlpha = lineAlpha;
            if (neighbor.getSecond().getSecond() instanceof TileEntityEnergyLine && te.isWorking()) {
                localAlpha = Math.min(((TileEntityEnergyLine) neighbor.getSecond().getSecond()).alphaForRender(partialTicks), localAlpha);
            }
            GlStateManager.color(192.0F / 255.0F, 1.0F, 1.0F, localAlpha);
            modelBase.renderBone2(neighbor.getSecond().getFirst());
        }

        this.setLightmapDisabled(false);
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
    }
}
