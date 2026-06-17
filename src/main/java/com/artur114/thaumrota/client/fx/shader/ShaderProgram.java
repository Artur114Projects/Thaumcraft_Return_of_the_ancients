package com.artur114.thaumrota.client.fx.shader;

import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;

public class ShaderProgram {
    private static final Logger log = LogManager.getLogger("ThaumRotA/Shaders");
    private final Map<String, Integer> uniforms = new HashMap<>();
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
        this.checkCompileStatus(shaderID);
        GL20.glAttachShader(this.programID, shaderID);
        return this;
    }

    public ShaderProgram add(Path path, int shaderType) {
        int shaderID = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderID, this.readFile(path));
        GL20.glCompileShader(shaderID);
        this.checkCompileStatus(shaderID);
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
        this.checkEnabled();
        return this.uniforms.computeIfAbsent(name, n -> {
            int i = GL20.glGetUniformLocation(this.programID, n);
            if (i == -1) log.warn("Uniform {} nor founded!", n);
            return i;
        });
    }

    public void uniform(String name, float value) {
        this.checkEnabled();
        GL20.glUniform1f(this.uniformId(name), value);
    }

    public void uniform(String name, float value, float value1) {
        this.checkEnabled();
        GL20.glUniform2f(this.uniformId(name), value, value1);
    }

    public void uniform(String name, float value, float value1, float value2) {
        this.checkEnabled();
        GL20.glUniform3f(this.uniformId(name), value, value1, value2);
    }

    public void uniform(String name, int value) {
        this.checkEnabled();
        GL20.glUniform1i(this.uniformId(name), value);
    }

    public void uniform3(String name, FloatBuffer buffer) {
        this.checkEnabled();
        GL20.glUniform3(this.uniformId(name), buffer);
    }

    public void uniform(String name, Matrix4f matrix4f) {
        this.checkEnabled();
        FloatBuffer invBuf = BufferUtils.createFloatBuffer(16);
        matrix4f.store(invBuf);
        invBuf.flip();
        GL20.glUniformMatrix4(this.uniformId(name), false, invBuf);
    }

    public void uniformInvProjMatrix(String name) {
        this.checkEnabled();
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
        this.checkEnabled();
        FloatBuffer projBuf = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projBuf);
        projBuf.flip();
        GL20.glUniformMatrix4(this.uniformId(name), false, projBuf);
    }

    public void uniformModelViewMatrix(String name) {
        this.checkEnabled();
        FloatBuffer projBuf = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL_MODELVIEW_MATRIX, projBuf);
        projBuf.flip();
        GL20.glUniformMatrix4(this.uniformId(name), false, projBuf);
    }

    public void uniformInvModelViewMatrix(String name) {
        this.checkEnabled();
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
        this.checkEnabled();
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
    
    private void checkCompileStatus(int shaderID) {
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            log.error("Compilation error: \n{}", GL20.glGetShaderInfoLog(shaderID, Short.MAX_VALUE));
        }
    }
    
    private void checkEnabled() {
        if (!this.isEnabled) throw new IllegalStateException("Attempt to use disabled shader!");
    }
}