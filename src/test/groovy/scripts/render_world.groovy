package scripts

import com.artur114.bananalib.math.m3d.box.Box3I
import com.artur114.bananalib.math.m3d.vec.Vec3DM
import com.artur114.thaumrota.client.event.ClientEventsHandler
import com.artur114.thaumrota.client.fx.shader.ShaderProgram
import com.artur114.thaumrota.client.init.InitShaders
import com.artur114.thaumrota.client.light.LightSource
import com.artur114.thaumrota.client.light.LineLightSource
import groovy.transform.BaseScript
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.Particle
import net.minecraft.client.renderer.Matrix4f
import net.minecraft.entity.Entity
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

import java.awt.Color
import java.nio.FloatBuffer

@BaseScript
RotADevScript script

if (Minecraft.getMinecraft().player == null) {
    return
}

boolean enable = true

if (!enable) {
    return
}

FloatBuffer modelBuf = BufferUtils.createFloatBuffer(16);
GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelBuf);
FloatBuffer projBuf = BufferUtils.createFloatBuffer(16);
GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projBuf);

Matrix4f proj = new Matrix4f();
proj.load(projBuf);
projBuf.flip()
Matrix4f invProj = new Matrix4f();
Matrix4f.invert(proj, invProj);
FloatBuffer invBufP = BufferUtils.createFloatBuffer(16);
invProj.store(invBufP);
invBufP.flip();

Matrix4f modelView = new Matrix4f();
modelView.load(modelBuf);
Matrix4f projection = new Matrix4f();
projection.load(projBuf);

Matrix4f mvp = new Matrix4f();
Matrix4f.mul(projection, modelView, mvp);
Matrix4f invMVP = new Matrix4f();
Matrix4f.invert(mvp, invMVP);

FloatBuffer invBufMVP = BufferUtils.createFloatBuffer(16);
invMVP.store(invBufMVP);
invBufMVP.flip();


ShaderProgram.renderFullScreen(InitShaders.COLORED_LIGHT.shader()) {
    GL20.glUniformMatrix4(InitShaders.COLORED_LIGHT.shader().uniformId("invMVPMatrix"), false, invBufMVP);
    this.sendLightSources TestGroovyClass.pointLight
    this.sendLineLightSources TestGroovyClass.lineLight
}

ShaderProgram.renderFullScreen(InitShaders.TEST_SHADER.shader()) {
    GL20.glUniformMatrix4(InitShaders.TEST_SHADER.shader().uniformId("invProjMatrix"), false, invBufP);
    InitShaders.TEST_SHADER.shader().uniform("time", (ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedGameTickCounter(partialTicksIn) / 8) as float)
}

void sendLineLightSources(List<LineLightSource> list) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(list.size() * 3)

    list.each {
        buffer.put(it.to().x - Particle.interpPosX as float)
        buffer.put(it.to().y - Particle.interpPosY as float)
        buffer.put(it.to().z - Particle.interpPosZ as float)
    }

    buffer.flip()
    GL20.glUniform3(InitShaders.COLORED_LIGHT.shader().uniformId("lineLightPosTo"), buffer)
    buffer.clear()

    list.each {
        buffer.put(it.from().x - Particle.interpPosX as float)
        buffer.put(it.from().y - Particle.interpPosY as float)
        buffer.put(it.from().z - Particle.interpPosZ as float)
    }

    buffer.flip()
    GL20.glUniform3(InitShaders.COLORED_LIGHT.shader().uniformId("lineLightPosFrom"), buffer)
    buffer.clear()

    list.each {
        buffer.put(it.color().red / 255 as float)
        buffer.put(it.color().green / 255 as float)
        buffer.put(it.color().blue / 255 as float)
    }

    buffer.flip()
    GL20.glUniform3(InitShaders.COLORED_LIGHT.shader().uniformId("lineLightColor"), buffer)
    buffer.clear()

    list.each {
        buffer.put(1 / (it.range() * it.range()) as float)
        buffer.put(it.brightness())
    }

    buffer.flip()
    GL20.glUniform2(InitShaders.COLORED_LIGHT.shader().uniformId("lineLightParams"), buffer)
    buffer.clear()

    InitShaders.COLORED_LIGHT.shader().uniform("lineLightCount", list.size())
}

void sendLightSources(List<LightSource> list) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(list.size() * 3)

    list.each {
        buffer.put(it.pos().x - Particle.interpPosX as float)
        buffer.put(it.pos().y - Particle.interpPosY as float)
        buffer.put(it.pos().z - Particle.interpPosZ as float)
    }

    buffer.flip()
    GL20.glUniform3(InitShaders.COLORED_LIGHT.shader().uniformId("pointLightPos"), buffer)
    buffer.clear()

    list.each {
        buffer.put(it.color().red / 255 as float)
        buffer.put(it.color().green / 255 as float)
        buffer.put(it.color().blue / 255 as float)
    }

    buffer.flip()
    GL20.glUniform3(InitShaders.COLORED_LIGHT.shader().uniformId("pointLightColor"), buffer)
    buffer.clear()

    list.each {
        buffer.put(1 / (it.range() * it.range()) as float)
        buffer.put(it.brightness())
    }

    buffer.flip()
    GL20.glUniform2(InitShaders.COLORED_LIGHT.shader().uniformId("pointLightParams"), buffer)
    buffer.clear()

    InitShaders.COLORED_LIGHT.shader().uniform("pointLightCount", list.size())
}