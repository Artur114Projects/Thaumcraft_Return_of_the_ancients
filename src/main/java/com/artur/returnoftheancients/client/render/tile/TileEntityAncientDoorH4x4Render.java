package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelAncientDoor8x6;
import com.artur.returnoftheancients.client.model.ModelAncientDoorH4x4;
import com.artur.returnoftheancients.tileentity.TileEntityAncientDoorH4x4;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class TileEntityAncientDoorH4x4Render extends TileEntitySpecialRendererBase<TileEntityAncientDoorH4x4> {
    private static final ResourceLocation TEXTURE_BASE = EnumAssetLocation.TEXTURES_BLOCKS.getPngRL("ancient_door_h_4x4");
    private final ModelAncientDoorH4x4 modelBase = new ModelAncientDoorH4x4();
    @Override
    public void doRender(TileEntityAncientDoorH4x4 tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        this.bindTexture(TEXTURE_BASE);

        modelBase.renderArch();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, -1.5F * (1 - tile.doorMoveProgress(partialTicks)));
        modelBase.renderDoor1();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 1.5F * (1 - tile.doorMoveProgress(partialTicks)));
        modelBase.renderDoor2();
        GlStateManager.popMatrix();
    }
}
