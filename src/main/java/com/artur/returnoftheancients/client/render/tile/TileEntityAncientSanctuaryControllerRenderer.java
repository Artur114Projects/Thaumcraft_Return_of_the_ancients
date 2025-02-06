package com.artur.returnoftheancients.client.render.tile;

import com.artur.returnoftheancients.client.model.ModelAncientSanctuaryController;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.tileentity.TileEntityAncientSanctuaryController;
import com.artur.returnoftheancients.utils.EnumTextureLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityPistonRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.tile.TileJarRenderer;
import thaumcraft.client.renderers.tile.TilePedestalRenderer;
import thaumcraft.common.blocks.basic.BlockPillar;
import thaumcraft.common.blocks.essentia.BlockCentrifuge;

public class TileEntityAncientSanctuaryControllerRenderer extends TileEntitySpecialRenderer<TileEntityAncientSanctuaryController> {
    private static final ResourceLocation TEXTURE_BASE = EnumTextureLocation.BLOCKS_PATH.getRL("ancient_sanctuary_controller");
    private final ModelAncientSanctuaryController modelBase = new ModelAncientSanctuaryController();

    @Override
    public void render(TileEntityAncientSanctuaryController te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);

        GlStateManager.pushMatrix();

        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.bindTexture(TEXTURE_BASE);

        modelBase.renderAll();

        GlStateManager.popMatrix();

        EntityItem item = new EntityItem(Minecraft.getMinecraft().world, 0.0, 0.0, 0.0, new ItemStack(InitItems.IMITATION_ANCIENT_PROTECTOR));
        float ticks = (float) Minecraft.getMinecraft().player.ticksExisted + partialTicks;
        GlStateManager.translate(0, -1.29, 0);
//        GlStateManager.rotate(ticks % 360.0F, 0.0F, 1.0F, 0.0F);

        item.hoverStart = 0.0F;

        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.renderEntity(item, 0.0, 0.0, 0.0, 0.0F, 0.0F, false);

        GlStateManager.popMatrix();
    }
}
