package com.artur114.thaumrota.client.fx.misc;

import com.artur114.thaumrota.client.init.InitShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER;

//@Mod.EventBusSubscriber
public class FBOTest {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static Framebuffer framebufferSub;
    private static Framebuffer framebuffer;

    @SubscribeEvent
    public static void fboTest(RenderWorldLastEvent event) {
        int current = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);

        prepareFrameBuff();
        drawMainQuad();

        GL11.glColor4f(1f, 1f, 1f,1f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


        InitShaders.TEST_SHADER.shader().enable();

        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + 0);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(mc.getFramebuffer().framebufferTexture);
        InitShaders.TEST_SHADER.shader().uniform("screenTexture", 0);

        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + 1);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(framebuffer.framebufferTexture);
        InitShaders.TEST_SHADER.shader().uniform("customTexture", 1);

        framebufferSub.bindFramebuffer(false);
        drawFrameBuff(mc.getFramebuffer().framebufferTexture);

        InitShaders.TEST_SHADER.shader().disable();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + 1);
        GlStateManager.bindTexture(0);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + 0);
        GlStateManager.bindTexture(0);



        mc.getFramebuffer().bindFramebuffer(false);
        drawFrameBuff(framebufferSub.framebufferTexture);

        post(current);
    }

    private static void post(int current) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        framebuffer.framebufferClear();
        framebufferSub.framebufferClear();
        mc.getFramebuffer().bindFramebuffer(false);
        GL11.glBindTexture(GL_TEXTURE_2D, current);
    }

    private static void drawFrameBuff(int frameBuffTex) {
        GL11.glPushMatrix();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, frameBuffTex);
        GL11.glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glLoadIdentity();
        drawQuad();
        GL11.glPopMatrix();
    }

    private static void prepareFrameBuff() {
        if (framebuffer == null)
            framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        if (mc.displayWidth != framebuffer.framebufferWidth
                || mc.displayHeight != framebuffer.framebufferHeight)
            framebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);

        if (framebufferSub == null)
            framebufferSub = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        if (mc.displayWidth != framebufferSub.framebufferWidth
                || mc.displayHeight != framebufferSub.framebufferHeight)
            framebufferSub.createBindFramebuffer(mc.displayWidth, mc.displayHeight);


        framebuffer.bindFramebuffer(false);

        GL30.glBindFramebuffer(GL_READ_FRAMEBUFFER, mc.getFramebuffer().framebufferObject);
        GL30.glBlitFramebuffer(0, 0,
                mc.displayWidth, mc.displayHeight,
                0, 0,
                mc.displayWidth, mc.displayHeight,
                GL_DEPTH_BUFFER_BIT,
                GL_NEAREST);
    }

    private static void drawMainQuad() {
        GL11.glPushMatrix();
        GL11.glBindTexture(GL_TEXTURE_2D, 0);
        GL11.glColor4f(1f, 0f,0f,1f);
        GL11.glTranslated(-Particle.interpPosX, -Particle.interpPosY, -Particle.interpPosZ);
        GL11.glTranslatef(0, 3, 0);
        GL11.glTranslatef(0, 0, 0.01f);
        drawQuad();
        GL11.glPopMatrix();
    }

    private static void drawQuad(){
        Tessellator tessellator = Tessellator.getInstance();

        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-1, -1, 0).tex(0, 0).endVertex();
        buffer.pos(1, -1, 0).tex(1, 0).endVertex();
        buffer.pos(1, 1, 0).tex(1, 1).endVertex();
        buffer.pos(-1, 1, 0).tex(0, 1).endVertex();

        tessellator.draw();
    }
}
