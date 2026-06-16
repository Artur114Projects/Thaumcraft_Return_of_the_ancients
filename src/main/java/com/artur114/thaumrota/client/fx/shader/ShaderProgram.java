package com.artur114.thaumrota.client.fx.shader;

import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL11.*;

public class ShaderProgram {
    private boolean isEnabled = false;
    private final int programID;

    public ShaderProgram() {
        this.programID = GL20.glCreateProgram();
    }

    public ShaderProgram addFragment(String path) {
        return this.add(path, GL20.GL_FRAGMENT_SHADER);
    }

    public ShaderProgram addVertex(String path) {
        return this.add(path, GL20.GL_VERTEX_SHADER);
    }

    public ShaderProgram addFragment(Path path) {
        return this.add(path, GL20.GL_FRAGMENT_SHADER);
    }

    public ShaderProgram addVertex(Path path) {
        return this.add(path, GL20.GL_VERTEX_SHADER);
    }

    public ShaderProgram add(String path, int shaderType) {
        int shaderID = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderID, this.readFile(path));
        GL20.glCompileShader(shaderID);


        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Compilation error: " + GL20.glGetShaderInfoLog(shaderID, Short.MAX_VALUE));
        }


        GL20.glAttachShader(this.programID, shaderID);
        return this;
    }

    public ShaderProgram add(Path path, int shaderType) {
        int shaderID = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderID, this.readFile(path));
        GL20.glCompileShader(shaderID);


        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Compilation error: " + GL20.glGetShaderInfoLog(shaderID, Short.MAX_VALUE));
        }


        GL20.glAttachShader(this.programID, shaderID);
        return this;
    }

    public ShaderProgram compile() {
        GL20.glLinkProgram(this.programID);
        return this;
    }

    public void enable() {
        GL20.glUseProgram(this.programID);
        this.isEnabled = true;
    }

    public void disable() {
        GL20.glUseProgram(0);
        this.isEnabled = false;
    }

    public void delete() {
        GL20.glDeleteProgram(this.programID);
        this.isEnabled = false;
    }

    public int uniformId(String name) {
        if (!isEnabled) throw new IllegalStateException("Attempt to get uniform id on disabled shader!");
        int i = GL20.glGetUniformLocation(this.programID, name);
        if (i == -1) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAA: " + name);
        }
        return i;
    }

    public void uniform(String name, float value) {
        if (!isEnabled) throw new IllegalStateException("Attempt to set uniform on disabled shader!");
        GL20.glUniform1f(this.uniformId(name), value);
    }

    public void uniform(String name, float value, float value1) {
        if (!isEnabled) throw new IllegalStateException("Attempt to set uniform on disabled shader!");
        GL20.glUniform2f(this.uniformId(name), value, value1);
    }

    public void uniform(String name, int value) {
        if (!isEnabled) throw new IllegalStateException("Attempt to set uniform on disabled shader!");
        GL20.glUniform1i(this.uniformId(name), value);
    }

    public void uniform(String name, Matrix4f matrix4f) {
        if (!isEnabled) throw new IllegalStateException("Attempt to set uniform on disabled shader!");
        FloatBuffer invBuf = BufferUtils.createFloatBuffer(16);
        matrix4f.store(invBuf);
        invBuf.flip();
        GL20.glUniformMatrix4(this.uniformId(name), false, invBuf);
    }

    public void uniformInvProjMatrix(String name) {
        if (!isEnabled) throw new IllegalStateException("Attempt to set uniform on disabled shader!");
        FloatBuffer projBuf = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projBuf);
        Matrix4f proj = new Matrix4f();
        proj.load(projBuf);
        Matrix4f invProj = new Matrix4f();
        Matrix4f.invert(proj, invProj);
        FloatBuffer invBuf = BufferUtils.createFloatBuffer(16);
        invProj.store(invBuf);
        invBuf.flip();
        GL20.glUniformMatrix4(this.uniformId(name), false, invBuf);
    }

    public void uniformProjMatrix(String name) {
        if (!isEnabled) throw new IllegalStateException("Attempt to set uniform on disabled shader!");
        FloatBuffer projBuf = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projBuf);
        projBuf.flip();
        GL20.glUniformMatrix4(this.uniformId(name), false, projBuf);
    }

    public void uniformModelViewMatrix(String name) {
        if (!isEnabled) throw new IllegalStateException("Attempt to set uniform on disabled shader!");
        FloatBuffer projBuf = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL_MODELVIEW_MATRIX, projBuf);
        projBuf.flip();
        GL20.glUniformMatrix4(this.uniformId(name), false, projBuf);
    }

    public void uniformInvModelViewMatrix(String name) {
        if (!isEnabled) throw new IllegalStateException("Attempt to set uniform on disabled shader!");
        FloatBuffer projBuf = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projBuf);
        Matrix4f proj = new Matrix4f();
        proj.load(projBuf);
        Matrix4f invProj = new Matrix4f();
        Matrix4f.invert(proj, invProj);
        FloatBuffer invBuf = BufferUtils.createFloatBuffer(16);
        invProj.store(invBuf);
        invBuf.flip();
        GL20.glUniformMatrix4(this.uniformId(name), false, invBuf);
    }

    public void uniformInvMVPMatrix(String name) {
        if (!isEnabled) throw new IllegalStateException("Shader not enabled");

        FloatBuffer modelBuf = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelBuf);
        FloatBuffer projBuf = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projBuf);

        Matrix4f modelView = new Matrix4f();
        modelView.load(modelBuf);
        Matrix4f projection = new Matrix4f();
        projection.load(projBuf);

        Matrix4f mvp = new Matrix4f();
        Matrix4f.mul(projection, modelView, mvp);
        Matrix4f invMVP = new Matrix4f();
        Matrix4f.invert(mvp, invMVP);

        FloatBuffer invBuf = BufferUtils.createFloatBuffer(16);
        invMVP.store(invBuf);
        invBuf.flip();
        GL20.glUniformMatrix4(uniformId(name), false, invBuf);
    }

    private String readFile(String path) {
        try {
            return IOUtils.toString(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(ThaumRotA.MODID, path)).getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return "";
    }

    private String readFile(Path path) {
        try {
            return IOUtils.toString(path.toUri().toURL().openStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return "";
    }

    private static Framebuffer framebuffer;

    public static void renderFullScreen(ShaderProgram shaderProgram) {
        renderFullScreen(shaderProgram, () -> {});
    }

    public static void renderFullScreen(ShaderProgram shaderProgram, Runnable onShaderEnabled) {  // TODO: 02.05.2025 Rewrite!
        if (shaderProgram == null) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        int current = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        boolean depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
        int activeTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);

        if (framebuffer == null) {
            framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
        }
        if (mc.displayWidth != framebuffer.framebufferWidth || mc.displayHeight != framebuffer.framebufferHeight) {
            framebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
        }

        framebuffer.framebufferClear();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.depthMask(true);

        shaderProgram.enable();
        onShaderEnabled.run();

        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
        GlStateManager.enableTexture2D();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.getFramebuffer().framebufferTexture);
        shaderProgram.uniform("screenTexture", 0);

        GlStateManager.setActiveTexture(GL13.GL_TEXTURE1);
        GlStateManager.enableTexture2D();
        copyDepth();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthCopyTexture);
        shaderProgram.uniform("depthTexture", 1);

        framebuffer.bindFramebuffer(false);
        GL11.glPushMatrix();
        GL11.glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glLoadIdentity();
        drawQuad();
        shaderProgram.disable();
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE1);
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
        GL11.glDepthMask(depthMask);
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
        GlStateManager.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24,
                width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, null);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GlStateManager.bindTexture(0);

        depthCopyFBO = OpenGlHelper.glGenFramebuffers();
        OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, depthCopyFBO);
        OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER,
                OpenGlHelper.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthCopyTexture, 0);

        GL11.glDrawBuffer(GL11.GL_NONE);
        GL11.glReadBuffer(GL11.GL_NONE);

        int status = OpenGlHelper.glCheckFramebufferStatus(OpenGlHelper.GL_FRAMEBUFFER);
        if (status != OpenGlHelper.GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Depth copy FBO incomplete! Status: " + status);
        }

        OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, mc.getFramebuffer().framebufferObject);
    }

    public static void deleteDepthCopyFBO() {
        if (depthCopyFBO != -1) {
            OpenGlHelper.glDeleteFramebuffers(depthCopyFBO);
            depthCopyFBO = -1;
        }
        if (depthCopyTexture != -1) {
            TextureUtil.deleteTexture(depthCopyTexture);
            depthCopyTexture = -1;
        }
    }

    public static int getDepthTexture() {
        return depthCopyTexture;
    }
}