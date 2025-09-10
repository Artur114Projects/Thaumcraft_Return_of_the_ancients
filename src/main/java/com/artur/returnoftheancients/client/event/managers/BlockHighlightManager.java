package com.artur.returnoftheancients.client.event.managers;

import com.artur.returnoftheancients.tileentity.interf.ITileMultiBBProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

public class BlockHighlightManager {
    public void drawBlockHighlightEvent(DrawBlockHighlightEvent e) {
        if (e.getTarget().typeOfHit != RayTraceResult.Type.BLOCK) {
            return;
        }

        BlockPos pos = e.getTarget().getBlockPos();
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        World world = mc.world;

        TileEntity tileRaw = mc.world.getTileEntity(pos);

        if (tileRaw instanceof ITileMultiBBProvider) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);

            if (world.getWorldBorder().contains(e.getTarget().getBlockPos())) {
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) e.getPartialTicks();
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) e.getPartialTicks();
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) e.getPartialTicks();
                for (AxisAlignedBB bb : ((ITileMultiBBProvider) tileRaw).boundingBoxes()) {
                    RenderGlobal.drawSelectionBoundingBox(bb.grow(0.002D).offset(pos).offset(-x, -y, -z), 0.0F, 0.0F, 0.0F, 0.4F);
                }
            }

            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();

            e.setCanceled(true);
        }
    }
}
