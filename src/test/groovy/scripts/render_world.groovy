package scripts

import com.artur114.bananalib.math.m3d.box.Box3D
import com.artur114.bananalib.math.m3d.box.Box3I
import com.artur114.bananalib.math.m3d.vec.IVec3D
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3I
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM
import com.artur114.bananalib.mc.math.m3d.vec.VecMc3D
import com.artur114.thaumrota.client.event.ClientEventsHandler
import com.artur114.thaumrota.client.init.InitShaders
import com.artur114.thaumrota.client.light.EnumLightType
import com.artur114.thaumrota.client.light.ILightSource
import com.artur114.thaumrota.client.light.LineLightSource
import com.artur114.thaumrota.client.light.PointLightSource
import com.artur114.thaumrota.client.render.fx.HeatRenderer
import groovy.transform.BaseScript
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.Particle
import net.minecraft.util.math.AxisAlignedBB

import java.awt.Color

@BaseScript
RotADevScript script

if (Minecraft.getMinecraft().player == null) {
    return
}

boolean enable = false

if (!enable) {
    return
}

prepareToDraw {
    HeatRenderer.lights.forEach {String name, Map<EnumLightType, ILightSource> map ->
        map.forEach {EnumLightType t, List<ILightSource> sources ->
            sources.each {
                if (it instanceof LineLightSource) {
                    drawBox(new Box3D(it.from(), it.to()).offset(0.5, 0.5, 0.5).grow(0.5).grow(0.002))
//                    drawBox(new Box3D(   
//                        Math.min(it.from().x(), it.to().x()) + 0.5F - (it.range() * 2),
//                        Math.min(it.from().y(), it.to().y()) + 0.5F - (it.range() * 2),
//                        Math.min(it.from().z(), it.to().z()) + 0.5F - (it.range() * 2),
//                        Math.max(it.from().x(), it.to().x()) + 0.5F + (it.range() * 2),
//                        Math.max(it.from().y(), it.to().y()) + 0.5F + (it.range() * 2),
//                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
//                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
//                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
//                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
//                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
//                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
//                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
//                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
//                    ), Color.RED)
                } else if (it instanceof PointLightSource) {
                    drawBox(new Box3D(it.pos(), it.pos()).offset(0.5, 0.5, 0.5).grow(0.5).grow(0.002))
                }
            }
        }
    }

    if (TestGroovyClass.lastPoint != null) {
        drawBox(new Box3D(new VecMc3D(TestGroovyClass.lastPoint), new VecMc3D(player.rayTrace(4, partialTicksIn).blockPos)).offset(0.5, 0.5, 0.5).grow(0.5).grow(0.002), Color.BLUE)
    }
}