package scripts


import com.artur114.bananalib.math.m3d.vec.IVec3D
import com.artur114.thaumrota.client.event.ClientEventsHandler
import com.artur114.thaumrota.client.init.InitShaders
import com.artur114.thaumrota.client.render.fx.HeatRenderer
import groovy.transform.BaseScript
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.Particle

@BaseScript
RotADevScript script

if (Minecraft.getMinecraft().player == null) {
    return
}

boolean enable = false

if (!enable) {
    return
}

TestGroovyClass.renderFullScreen(InitShaders.TEST_SHADER.shader()) {
    it.uniform("time", (ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedGameTickCounter(partialTicksIn) / 8) as float)
    it.uniformInvMVPMatrix("invMVPMatrix")
    IVec3D pos = playerPosToRen()
    it.uniform("cameraPos", pos.x - Particle.interpPosX as float, pos.y - Particle.interpPosY as float, pos.z - Particle.interpPosZ as float)
    it.uniform("globalHeat", 0.8F)
//    this.sendLightSources(HeatRenderer.pointLight, it)
//    this.sendLineLightSources(HeatRenderer.lineLight, it)
}

//void sendLineLightSources(List<LineLightSource> list, ShaderProgram it) {
//    FloatBuffer buffer = BufferUtils.createFloatBuffer(list.size() * 3)
//
//    list.each {
//        buffer.put(it.to().x - Particle.interpPosX as float)
//        buffer.put(it.to().y - Particle.interpPosY as float)
//        buffer.put(it.to().z - Particle.interpPosZ as float)
//    }
//
//    buffer.flip()
//    GL20.glUniform3(it.uniformId("lineLightPosTo"), buffer)
//    buffer.clear()
//
//    list.each {
//        buffer.put(it.from().x - Particle.interpPosX as float)
//        buffer.put(it.from().y - Particle.interpPosY as float)
//        buffer.put(it.from().z - Particle.interpPosZ as float)
//    }
//
//    buffer.flip()
//    GL20.glUniform3(it.uniformId("lineLightPosFrom"), buffer)
//    buffer.clear()
//
//    list.each {
//        buffer.put(it.color().red / 255 as float)
//        buffer.put(it.color().green / 255 as float)
//        buffer.put(it.color().blue / 255 as float)
//    }
//
//    buffer.flip()
//    GL20.glUniform3(it.uniformId("lineLightColor"), buffer)
//    buffer.clear()
//
//    list.each {
//        buffer.put(1 / (it.range() * it.range()) as float)
//        buffer.put(it.brightness())
//    }
//
//    buffer.flip()
//    GL20.glUniform2(it.uniformId("lineLightParams"), buffer)
//    buffer.clear()
//
//    it.uniform("lineLightCount", list.size())
//}
//
//void sendLightSources(List<LightSource> list, ShaderProgram it) {
//    FloatBuffer buffer = BufferUtils.createFloatBuffer(list.size() * 3)
//
//    list.each {
//        buffer.put(it.pos().x - Particle.interpPosX as float)
//        buffer.put(it.pos().y - Particle.interpPosY as float)
//        buffer.put(it.pos().z - Particle.interpPosZ as float)
//    }
//
//    buffer.flip()
//    GL20.glUniform3(it.uniformId("pointLightPos"), buffer)
//    buffer.clear()
//
//    list.each {
//        buffer.put(it.color().red / 255 as float)
//        buffer.put(it.color().green / 255 as float)
//        buffer.put(it.color().blue / 255 as float)
//    }
//
//    buffer.flip()
//    GL20.glUniform3(it.uniformId("pointLightColor"), buffer)
//    buffer.clear()
//
//    list.each {
//        buffer.put(1 / (it.range() * it.range()) as float)
//        buffer.put(it.brightness())
//    }
//
//    buffer.flip()
//    GL20.glUniform2(it.uniformId("pointLightParams"), buffer)
//    buffer.clear()
//
//    it.uniform("pointLightCount", list.size())
//}