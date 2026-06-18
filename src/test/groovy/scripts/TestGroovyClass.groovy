package scripts

import com.artur114.bananalib.math.m3d.vec.IVec3DM
import com.artur114.thaumrota.client.fx.shader.ShaderProgram

import com.artur114.thaumrota.client.light.LineLightSource
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureUtil
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.shader.Framebuffer
import net.minecraft.util.math.BlockPos
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL30

import java.util.function.Consumer

import static org.lwjgl.opengl.GL11.GL_MODELVIEW
import static org.lwjgl.opengl.GL11.GL_PROJECTION
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D

class TestGroovyClass {
    static BlockPos lastPoint = null;


    private static Framebuffer framebuffer

    static void renderFullScreen(ShaderProgram shaderProgram) {
        renderFullScreen(shaderProgram, {});
    }

    static void renderFullScreen(ShaderProgram shaderProgram, Consumer<ShaderProgram> onShaderEnabled) {  // TODO: 02.05.2025 Rewrite!
        if (shaderProgram == null) {
            return;
        }

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

        shaderProgram.enable();
        onShaderEnabled.accept(shaderProgram)

        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
        GlStateManager.enableTexture2D();
        GL11.glBindTexture(GL_TEXTURE_2D, mc.getFramebuffer().framebufferTexture);
        shaderProgram.uniform("screenTexture", 0);

        GlStateManager.setActiveTexture(GL13.GL_TEXTURE2)
        GlStateManager.enableTexture2D(); copyDepth()
        GL11.glBindTexture(GL_TEXTURE_2D, depthCopyTexture)
        shaderProgram.uniform("depthTexture", 2)

        framebuffer.bindFramebuffer(false);
        GL11.glPushMatrix();
        GL11.glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glLoadIdentity();
        drawQuad();
        shaderProgram.disable();
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL_TEXTURE_2D, 0);
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D, 0);

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

    private static int depthCopyFBO = -1;
    private static int depthCopyTexture = -1;
    private static int lastWidth = -1, lastHeight = -1;

    private static void copyDepth() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!OpenGlHelper.isFramebufferEnabled()) return;
        int mainFBO = mc.getFramebuffer().framebufferObject;
        if (mainFBO == -1 || !mc.getFramebuffer().useDepth) return;

        int width = mc.getFramebuffer().framebufferWidth;
        int height = mc.getFramebuffer().framebufferHeight;

        if (depthCopyFBO == -1 || width != lastWidth || height != lastHeight) {
            createDepthCopyFBO();
            lastWidth = width;
            lastHeight = height;
        }

        boolean depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        GL11.glDepthMask(true);

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mainFBO);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, depthCopyFBO);
        GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height,
                GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);

        int err = GL11.glGetError();
        if (err != 0) System.err.println("GL error after blit: " + err);

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
        GlStateManager.glTexImage2D(GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24,
                width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, null);
        GlStateManager.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GlStateManager.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GlStateManager.bindTexture(0);

        depthCopyFBO = OpenGlHelper.glGenFramebuffers();
        OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, depthCopyFBO);
        OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER,
                OpenGlHelper.GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthCopyTexture, 0);

        GL11.glDrawBuffer(GL11.GL_NONE);
        GL11.glReadBuffer(GL11.GL_NONE);

        int status = OpenGlHelper.glCheckFramebufferStatus(OpenGlHelper.GL_FRAMEBUFFER);
        if (status != OpenGlHelper.GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Depth copy FBO incomplete! Status: " + status);
        }

        OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, mc.getFramebuffer().framebufferObject);
    }

    static void deleteDepthCopyFBO() {
        if (depthCopyFBO != -1) {
            OpenGlHelper.glDeleteFramebuffers(depthCopyFBO);
            depthCopyFBO = -1;
        }
        if (depthCopyTexture != -1) {
            TextureUtil.deleteTexture(depthCopyTexture);
            depthCopyTexture = -1;
        }
    }

    static int getDepthTexture() {
        return depthCopyTexture;
    }
}
