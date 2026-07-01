
package scripts

import com.artur114.thaumrota.client.render.tile.TileEntityAncientPistonRender
import com.artur114.thaumrota.common.init.InitBlocks
import com.artur114.thaumrota.common.init.InitItems
import com.artur114.thaumrota.common.tileentity.TileEntityAncientPiston
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.RenderItem
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import thaumcraft.api.blocks.BlocksTC


Minecraft mc = Minecraft.minecraft
ScaledResolution resolution = new ScaledResolution(mc);
int width = widthIn, height = heightIn



int x = (int) (width - width / 30);
int y = (int) (height - height / 32);
int scale = 50;
if (resolution.getScaleFactor() == 3) {
    scale = 30; y = (int) (height - height / 12);
} else if (resolution.getScaleFactor() == 1) {
    y = (int) (height - height / 20);
}

GlStateManager.pushMatrix();
GlStateManager.enableBlend();
GlStateManager.enableAlpha();

EntityItem item = new EntityItem(Minecraft.getMinecraft().world, 0.0, 0.0, 0.0, new ItemStack(BlocksTC.stoneAncient));
item.hoverStart = 0
GlStateManager.translate(x, y, 50);
GlStateManager.rotate(-22.0F, 1.0F, 0.0F, 0.0F);
float angle = (float) (-360.0F * ((System.currentTimeMillis() % 2000L) / 2000.0F));
GlStateManager.rotate(angle, 0.0F, 1.0F, 0.0F);
GlStateManager.translate(-x, -y, -50);
this.drawEntityItem(x, y, scale, item)

GlStateManager.disableBlend();
GlStateManager.disableAlpha();
GlStateManager.popMatrix();



void drawEntityItem(int posX, int posY, int scale, EntityItem ent) {
    GlStateManager.pushMatrix();
    GlStateManager.translate(posX, posY, 50.0F);
    GlStateManager.scale(-scale, scale, scale);
    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
    RenderManager renderer = Minecraft.getMinecraft().getRenderManager();
    renderer.setPlayerViewY(180.0F);
    renderer.setRenderShadow(false);
    renderer.renderEntity(ent, 0.0F, 0.0F, 0.0F, ent.rotationYaw, 1.0F, false);
    renderer.setRenderShadow(true);
    GlStateManager.popMatrix();
}