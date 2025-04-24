package com.artur.returnoftheancients.client.fx.shader;

import com.artur.returnoftheancients.handlers.RenderHandler;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
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
        this.programID = ARBShaderObjects.glCreateProgramObjectARB();
    }

    public ShaderProgram addFragment(String path) {
        return add(path, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
    }

    public ShaderProgram addVertex(String path) {
        return add(path, ARBVertexShader.GL_VERTEX_SHADER_ARB);
    }

    public ShaderProgram add(String path, int shaderType) {
        int shaderID = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
        ARBShaderObjects.glShaderSourceARB(shaderID, readFile(path));
        ARBShaderObjects.glCompileShaderARB(shaderID);

        if (ARBShaderObjects.glGetObjectParameteriARB(shaderID, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
            throw new RuntimeException("Shader compilation error!\n" + ARBShaderObjects.glGetInfoLogARB(shaderID, ARBShaderObjects.glGetObjectParameteriARB(shaderID, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB)));
        }

        ARBShaderObjects.glAttachObjectARB(this.programID, shaderID);
        return this;
    }

    public ShaderProgram compile() {
        ARBShaderObjects.glLinkProgramARB(this.programID);
        return this;
    }

    public void enable() {
        ARBShaderObjects.glUseProgramObjectARB(this.programID);
        this.isEnabled = true;
    }

    public void disable() {
        ARBShaderObjects.glUseProgramObjectARB(0);
        this.isEnabled = false;
    }

    public int uniformId(String name) {
        if (!isEnabled) throw new IllegalStateException("Attempt to get uniform id on disabled shader!");
        return ARBShaderObjects.glGetUniformLocationARB(this.programID, name);
    }

    public void uniform(String name, float value) {
        if (!isEnabled) throw new IllegalStateException("Attempt to set uniform on disabled shader!");
        ARBShaderObjects.glUniform1fARB(this.uniformId(name), value);
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

        framebuffer.bindFramebuffer(false);
        GL11.glPushMatrix();
        GL11.glBindTexture(GL_TEXTURE_2D, mc.getFramebuffer().framebufferTexture);
        GL11.glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glLoadIdentity();

        shaderProgram.enable();
        onShaderEnabled.run();
        drawQuad();
        shaderProgram.disable();
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