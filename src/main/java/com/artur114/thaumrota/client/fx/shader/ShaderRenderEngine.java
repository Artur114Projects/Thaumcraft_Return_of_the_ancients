package com.artur114.thaumrota.client.fx.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class ShaderRenderEngine {
    private static final Logger log = LogManager.getLogger("ThaumRotA/Shaders");
    private static int lastFBOWidth = -1, lastFBOHeight = -1;
    private static int depthCopyTexture = -1;
    private static int depthCopyFBO = -1;
    private static Framebuffer framebuffer;


    public static void renderFullScreen(ShaderRender render) {
        renderFullScreen(render, (s) -> {});
    }

    public static void renderFullScreen(ShaderRender render, Consumer<ShaderProgram> onShaderEnabled) {  // TODO: 02.05.2025 Rewrite!
        if (render == null) return;

        Minecraft mc = Minecraft.getMinecraft();
        int current = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
        GL11.glPushAttrib(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ENABLE_BIT);
        int activeTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);

        if (framebuffer == null) {
            framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        }
        if (mc.displayWidth != framebuffer.framebufferWidth || mc.displayHeight != framebuffer.framebufferHeight) {
            framebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
        }

        framebuffer.framebufferClear();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.depthMask(true);

        render.shader.enable();
        onShaderEnabled.accept(render.shader);

        if (render.mainTextureName != null) {
            GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
            GlStateManager.enableTexture2D();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.getFramebuffer().framebufferTexture);
            render.shader.uniform(render.mainTextureName, 0);
        }

        if (render.depthTextureName != null) {
            GlStateManager.setActiveTexture(GL13.GL_TEXTURE2);
            GlStateManager.enableTexture2D(); copyDepth();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthCopyTexture);
            render.shader.uniform(render.depthTextureName, 2);
        }

        final int[] texture = {2};
        render.textures.forEach((name, tex) -> {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + texture[0]);
            GlStateManager.enableTexture2D();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
            render.shader.uniform(name, texture[0]);
            texture[0]++;
        });

        framebuffer.bindFramebuffer(false);
        GL11.glPushMatrix();
        GL11.glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glLoadIdentity();
        drawQuad();
        render.shader.disable();

        for (int i = texture[0]; i != 2; i--) {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + i);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL11.glPopMatrix();
        mc.getFramebuffer().bindFramebuffer(false);

        GL11.glPushMatrix();
        GL11.glBindTexture(GL_TEXTURE_2D, framebuffer.framebufferTexture);
        GL11.glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glLoadIdentity();
        drawQuad();
        GL11.glPopMatrix();

        framebuffer.framebufferClear();
        mc.getFramebuffer().bindFramebuffer(false);
        GL11.glPopAttrib();
        GL11.glPopAttrib();
        GlStateManager.setActiveTexture(activeTexture);
        GL11.glBindTexture(GL_TEXTURE_2D, current);
    }

    private static void drawQuad() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-1, -1, 0).tex(0, 0).endVertex();
        buffer.pos(1, -1, 0).tex(1, 0).endVertex();
        buffer.pos(1, 1, 0).tex(1, 1).endVertex();
        buffer.pos(-1, 1, 0).tex(0, 1).endVertex();
        tessellator.draw();
    }

    private static void copyDepth() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!OpenGlHelper.isFramebufferEnabled()) return;
        int mainFBO = mc.getFramebuffer().framebufferObject;
        if (mainFBO == -1 || !mc.getFramebuffer().useDepth) return;

        int width = mc.getFramebuffer().framebufferWidth;
        int height = mc.getFramebuffer().framebufferHeight;

        if (depthCopyFBO == -1 || width != lastFBOWidth || height != lastFBOHeight) {
            createDepthCopyFBO();
            lastFBOWidth = width;
            lastFBOHeight = height;
        }

        boolean depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        GL11.glDepthMask(true);

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mainFBO);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, depthCopyFBO);
        GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);

        int err = GL11.glGetError();
        if (err != 0) log.error("GL error after blit: {}", err);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, mainFBO);
        GL11.glDepthMask(depthMask);
    }

    public static void createDepthCopyFBO() {
        Minecraft mc = Minecraft.getMinecraft();
        int width = mc.getFramebuffer().framebufferWidth;
        int height = mc.getFramebuffer().framebufferHeight;

        deleteDepthCopyFBO();

        depthCopyTexture = TextureUtil.glGenTextures();
        GlStateManager.bindTexture(depthCopyTexture);
        GlStateManager.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, null);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GlStateManager.bindTexture(0);

        depthCopyFBO = OpenGlHelper.glGenFramebuffers();
        OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, depthCopyFBO);
        OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthCopyTexture, 0);

        GL11.glDrawBuffer(GL11.GL_NONE);
        GL11.glReadBuffer(GL11.GL_NONE);

        int status = OpenGlHelper.glCheckFramebufferStatus(OpenGlHelper.GL_FRAMEBUFFER);
        if (status != OpenGlHelper.GL_FRAMEBUFFER_COMPLETE) {
            log.error("Depth copy FBO not compiled! Status: {}", status);
        }

        OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, mc.getFramebuffer().framebufferObject);
    }

    private static void deleteDepthCopyFBO() {
        if (depthCopyFBO != -1) {
            OpenGlHelper.glDeleteFramebuffers(depthCopyFBO);
            depthCopyFBO = -1;
        }
        if (depthCopyTexture != -1) {
            TextureUtil.deleteTexture(depthCopyTexture);
            depthCopyTexture = -1;
        }
    }
}
