package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelAncientSanctuaryController;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.tileentity.TileEntityAncientSanctuaryController;
import com.artur.returnoftheancients.util.EnumTextureLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TileEntityAncientSanctuaryControllerRenderer extends TileEntitySpecialRenderer<TileEntityAncientSanctuaryController> {
    private final EntityItem FUSE_IMITATION_ENTITY_TO_RENDER = new EntityItem(Minecraft.getMinecraft().world, 0.0, 0.0, 0.0, new ItemStack(InitItems.IMITATION_ANCIENT_FUSE));
    private static final ResourceLocation TEXTURE_BASE = EnumTextureLocation.BLOCKS_PATH.getRL("ancient_sanctuary_controller");
    private final ModelAncientSanctuaryController modelBase = new ModelAncientSanctuaryController();

    public TileEntityAncientSanctuaryControllerRenderer() {
        FUSE_IMITATION_ENTITY_TO_RENDER.hoverStart = 0.0F;
    }

    @Override
    public void render(TileEntityAncientSanctuaryController te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);

        GlStateManager.pushMatrix();

        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.bindTexture(TEXTURE_BASE);

        modelBase.setDoorProgress(te.doorMovingProgress(true), te.doorMovingProgress(false), partialTicks);

        modelBase.renderAll();

        GlStateManager.popMatrix();

        if (te.hasItem() && !te.isClose()) {
            GlStateManager.translate(0, -1.29 + Math.cos((System.currentTimeMillis() * 0.001D * 0.1D) % (Math.PI * 2)) * 0.008D, 0);

            RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.renderEntity(FUSE_IMITATION_ENTITY_TO_RENDER, 0.0, 0.0, 0.0, 0.0F, 0.0F, false);
        }

        GlStateManager.popMatrix();
    }
}
