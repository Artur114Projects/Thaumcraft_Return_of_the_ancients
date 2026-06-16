package com.artur114.thaumrota.client.fx.shader;

import com.artur114.thaumrota.client.init.InitShaders;
import com.artur114.thaumrota.client.util.RenderHandler;
import com.artur114.thaumrota.common.util.math.BoundingBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;

//@Mod.EventBusSubscriber
public class HeatShader {
    protected static Framebuffer framebuffer;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderShaders(RenderWorldLastEvent evt) { // TODO: 02.05.2025 Rewrite!
        if (Minecraft.getMinecraft().player == null) {
            return;//  || Minecraft.getMinecraft().player.dimension != InitDimensions.ancient_world_dim_id
        }

        drawArea();

        ShaderProgram.renderFullScreen(InitShaders.TEST_SHADER.shader(), () -> {
            InitShaders.TEST_SHADER.shader().uniform("texelSize", 1.0F / framebuffer.framebufferWidth, 1.0F / framebuffer.framebufferHeight);
            InitShaders.TEST_SHADER.shader().uniformInvMVPMatrix("invMVPMatrix");
//            InitShaders.HEAT.shader().uniform("time", (System.currentTimeMillis() % 100000L) / 30000.0F);
        });
    }

    private static void drawArea() {
        Minecraft mc = Minecraft.getMinecraft();
        int current = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        if (framebuffer == null) {
            framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        }
        if (mc.displayWidth != framebuffer.framebufferWidth || mc.displayHeight != framebuffer.framebufferHeight) {
            framebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
        }
        framebuffer.framebufferClear();

        framebuffer.bindFramebuffer(false);
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mc.getFramebuffer().framebufferObject);
        GL30.glBlitFramebuffer(0, 0, mc.displayWidth, mc.displayHeight, 0, 0, mc.displayWidth, mc.displayHeight, GL_DEPTH_BUFFER_BIT, GL_NEAREST);
        BlockPos pos = new BlockPos(8, 100, 8);
        int range = 8;
        BoundingBox box = new BoundingBox(pos, pos).grow(range);
        GL11.glPushMatrix();
        GL11.glBindTexture(GL_TEXTURE_2D, 0);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glTranslated(-Particle.interpPosX, -Particle.interpPosY, -Particle.interpPosZ);
        RenderHandler.renderCube(box);
        GL11.glPopMatrix();

        mc.getFramebuffer().bindFramebuffer(false);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, current);

        drawBox(box);
    }

    private static void drawBox(BoundingBox boundingBox) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(4.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        boundingBox.renderArea(1.0F);

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}