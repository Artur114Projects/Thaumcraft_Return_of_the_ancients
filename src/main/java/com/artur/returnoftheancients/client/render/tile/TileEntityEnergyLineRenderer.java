package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelEnergyLine;
import com.artur.returnoftheancients.tileentity.TileEntityEnergyLine;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityEnergyLineRenderer extends TileEntitySpecialRenderer<TileEntityEnergyLine> {

    private final ModelEnergyLine modelBase = new ModelEnergyLine();

    @Override
    public void render(TileEntityEnergyLine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5 + (8.2 / 16F), y + 1.5, z + 0.5 + (7.8 / 16F));

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.color(192.0F / 255.0F, 1.0F, 1.0F, (float) Math.cos((System.currentTimeMillis() * 0.001D) % (Math.PI) + Math.PI * 1.5));

        modelBase.render(0);

        modelBase.render(1);


        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();

        GlStateManager.popMatrix();
    }
}
