package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelEnergyLine;
import com.artur.returnoftheancients.energy.bases.tile.ITileEnergy;
import com.artur.returnoftheancients.handlers.RenderHandler;
import com.artur.returnoftheancients.tileentity.TileEntityEnergyLine;
import com.artur.returnoftheancients.util.Tuple;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class TileEntityEnergyLineRenderer extends TileEntitySpecialRenderer<TileEntityEnergyLine> {

    private final ModelEnergyLine modelBase = new ModelEnergyLine();

    @Override
    public void render(TileEntityEnergyLine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 1, y + 1.5, z + 1);

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        this.setLightmapDisabled(te.currentTransferredEnergy() > 0);
        float baseAlpha = 0.2F;
        float energyAlpha = MathHelper.clamp(baseAlpha + (RenderHandler.interpolate(te.prevTransferredEnergy(), te.currentTransferredEnergy(), partialTicks) / te.maxTransferredEnergy()), 0.0F, 1.0F);
        GlStateManager.color(192.0F / 255.0F, 1.0F, 1.0F, energyAlpha);
        modelBase.renderBase();

        for (Tuple<Boolean, Tuple<EnumFacing, ITileEnergy>> neighbor : te.neighbors) {
            GlStateManager.color(192.0F / 255.0F, 1.0F, 1.0F, neighbor.getFirst() ? energyAlpha : baseAlpha);
            this.setLightmapDisabled(neighbor.getFirst() && te.currentTransferredEnergy() > 0);
            modelBase.renderBone2(neighbor.getSecond().getFirst());
        }

        this.setLightmapDisabled(false);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
    }
}
