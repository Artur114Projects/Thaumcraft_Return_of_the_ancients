package com.artur.returnoftheancients.client.fx.shader;

import com.artur.returnoftheancients.handlers.RenderHandler;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL11.*;

public class ShaderProgram {
    private boolean isEnabled = false;
    private final int programID;

    public ShaderProgram() {
        this.programID = GL20.glCreateProgram();
    }

    public ShaderProgram addFragment(String path) {
        return add(path, GL20.GL_FRAGMENT_SHADER);
    }

    public ShaderProgram addVertex(String path) {
        return add(path, GL20.GL_VERTEX_SHADER);
    }

    public ShaderProgram add(String path, int shaderType) {
        int shaderID = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderID, readFile(path));
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

    public int uniformId(String name) {
        if (!isEnabled) throw new IllegalStateException("Attempt to get uniform id on disabled shader!");
        return GL20.glGetUniformLocation(this.programID, name);
    }

    public void uniform(String name, float value) {
        if (!isEnabled) throw new IllegalStateException("Attempt to set uniform on disabled shader!");
        GL20.glUniform1f(this.uniformId(name), value);
    }

    public void uniform(String name, int value) {
        if (!isEnabled) throw new IllegalStateException("Attempt to set uniform on disabled shader!");
        GL20.glUniform1i(this.uniformId(name), value);
    }

    private String readFile(String path) {
        try {
            return IOUtils.toString(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Referense.MODID, path)).getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return "";
    }

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static Framebuffer framebuffer;

    public static void renderFullScreen(ShaderProgram shaderProgram) {
        renderFullScreen(shaderProgram, () -> {});
    }

    public static void renderFullScreen(ShaderProgram shaderProgram, Runnable onShaderEnabled) {
        int current = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D); // в 1.12 нужно сохранять текстур атлас
        if (framebuffer == null)
            framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);

        if (mc.displayWidth != framebuffer.framebufferWidth
                || mc.displayHeight != framebuffer.framebufferHeight)
            framebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);

        shaderProgram.enable();
        onShaderEnabled.run();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + 0);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(mc.getFramebuffer().framebufferTexture);
        shaderProgram.uniform("screenTexture", 0);

        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + 1);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(mc.getFramebuffer().depthBuffer);
        shaderProgram.uniform("depthTexture", 1);

        framebuffer.bindFramebuffer(false);
        GL11.glPushMatrix();
        GlStateManager.bindTexture(mc.getFramebuffer().framebufferTexture);
        GL11.glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glLoadIdentity();
        drawQuad();
        shaderProgram.disable();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + 1);
        GlStateManager.bindTexture(0);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + 0);
        GlStateManager.bindTexture(0);

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
        GL11.glBindTexture(GL_TEXTURE_2D, current);
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