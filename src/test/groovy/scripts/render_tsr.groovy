package scripts

import com.artur114.thaumrota.common.init.InitItems
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack


EntityItem FUSE_IMITATION_ENTITY_TO_RENDER = new EntityItem(Minecraft.getMinecraft().world, 0.0, 0.0, 0.0, new ItemStack(InitItems.IMITATION_ANCIENT_FUSE));
FUSE_IMITATION_ENTITY_TO_RENDER.hoverStart = 0.0F;


GlStateManager.translate(0, 1.4, -0.42F - 0.5 / 16.0)
GlStateManager.rotate(90, 1, 0, 0)
GlStateManager.scale(1.28, 1.28, 1.28)


RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
rendermanager.renderEntity(FUSE_IMITATION_ENTITY_TO_RENDER, 0.0, 0.0, 0.0, 0.0F, 0.0F, false);
