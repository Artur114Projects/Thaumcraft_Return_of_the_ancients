package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelAncientDoor4X3;
import com.artur.returnoftheancients.client.model.ModelAncientSanctuaryController;
import com.artur.returnoftheancients.tileentity.TileEntityAncientDoor4X3;
import com.artur.returnoftheancients.tileentity.TileEntityAncientSanctuaryController;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityAncientDoor4X3Render extends TileEntitySpecialRenderer<TileEntityAncientDoor4X3>  {
    private final ModelAncientDoor4X3 modelBase = new ModelAncientDoor4X3();

    @Override
    public void render(TileEntityAncientDoor4X3 te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);

        GlStateManager.enableLighting();
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//        GlStateManager.disableAlpha();

        this.bindTexture(EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("texture"));

        modelBase.renderArch();
        modelBase.renderDoor1();
        modelBase.renderDoor2();

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
}
