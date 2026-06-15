package scripts

import com.artur114.thaumrota.client.event.ClientEventsHandler
import com.artur114.thaumrota.client.model.ModelAncientPiston
import com.artur114.thaumrota.client.render.tile.TileEntityForDevRenderer
import com.artur114.thaumrota.client.render.tile.TileEntitySpecialRendererBase
import com.artur114.thaumrota.common.init.InitBlocks
import com.artur114.thaumrota.common.init.InitItems
import com.artur114.thaumrota.common.tileentity.TileEntityForDev
import com.artur114.thaumrota.common.util.EnumAsset
import groovy.transform.BaseScript
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.server.MinecraftServer
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper

@BaseScript
RotADevScript script

final ResourceLocation TEXTURE_BASE = EnumAsset.TEXTURES_BLOCKS.png("ancient_piston");
final ModelAncientPiston modelBase = new ModelAncientPiston();
TileEntityForDevRenderer renderer = InitBlocks.DEV_BLOCK.tileRender

EnumFacing face = EnumFacing.UP

if (face == EnumFacing.DOWN) {
    GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
    GlStateManager.translate(0.0F, -2.0F, 0.0F);
}
if (face.getAxis().isHorizontal()) {
    GlStateManager.translate(0.0F, 1.0F, 0.0F);
    GlStateManager.translate(1.0F * (face.getFrontOffsetX() * -1), 0.0F, 1.0F * (face.getFrontOffsetZ() * -1));
    if (face.getAxis() == EnumFacing.Axis.Z) GlStateManager.translate(0.0F, 0.0F, 2.0F * (face.getAxisDirection().getOffset()));
    GlStateManager.rotate(90.0F * (face.getAxisDirection().getOffset() * -1) as float, 1.0F * Math.abs(face.getFrontOffsetZ()) as float, 0.0F, 1.0F * Math.abs(face.getFrontOffsetX()) as float);
}

GlStateManager.rotate(90.0F * (tile.getPos().hashCode() % 4) as float, 0.0F, 1.0F, 0.0F);

Minecraft.minecraft.textureManager.bindTexture(TEXTURE_BASE)
modelBase.renderBase()

GlStateManager.pushMatrix()
float moveRange = 9.0F / 16.0F
float move = (moveRange * ((MathHelper.cos((float) (((Math.PI * 2) * moveProcess(40.0F)))) - 1) / 2))
GlStateManager.translate(0.0F, moveRange + move, 0.0F)
modelBase.renderPiston()
GlStateManager.popMatrix()

TileEntityForDev getTile() {
    return tileIn
}

float moveProcess(float max) {
    float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
    return (float) ((ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedUnloadGameTickCounter(partialTicks)) % max) / max;
}

