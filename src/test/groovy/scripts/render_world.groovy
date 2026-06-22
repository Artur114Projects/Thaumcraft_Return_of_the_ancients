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
import com.artur114.thaumrota.client.light.LineLightSourceD
import com.artur114.thaumrota.client.light.PointLightSource
import com.artur114.thaumrota.client.render.fx.HeatRenderer
import com.artur114.thaumrota.common.util.math.BoundingBox
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures.StructureCombatRoom
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1StaticManager
import com.artur114.thaumrota.common.worldstate.ancientworld.system.client.AncientLayer1Client
import groovy.transform.BaseScript
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.Particle
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.fml.common.FMLCommonHandler

import java.awt.Color
import java.lang.reflect.Field

@BaseScript
RotADevScript script

if (Minecraft.getMinecraft().player == null) {
    return
}

boolean enable = true

if (!enable) {
    return
}

prepareToDraw {
    Random rand = new Random()
//    HeatRenderer.lights.forEach {String name, Map<EnumLightType, ILightSource> map ->
//        map.forEach {EnumLightType t, List<ILightSource> sources ->
//            sources.each {
//                if (it instanceof LineLightSource) {
//                    drawBox(new Box3D(it.from(), it.to()).offset(0.5, 0.5, 0.5).grow(0.5).grow(0.002))
////                    drawBox(new Box3D(
////                        Math.min(it.from().x(), it.to().x()) + 0.5F - (it.range() * 2),
////                        Math.min(it.from().y(), it.to().y()) + 0.5F - (it.range() * 2),
////                        Math.min(it.from().z(), it.to().z()) + 0.5F - (it.range() * 2),
////                        Math.max(it.from().x(), it.to().x()) + 0.5F + (it.range() * 2),
////                        Math.max(it.from().y(), it.to().y()) + 0.5F + (it.range() * 2),
////                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
////                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
////                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
////                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
////                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
////                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
////                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
////                        Math. max(it.from().z(), it.to().z()) + 0.5F + (it.range() * 2)
////                    ), Color.RED)
//                } else if (it instanceof PointLightSource) {
//                    drawBox(new Box3D(it.pos(), it.pos()).offset(0.5, 0.5, 0.5).grow(0.5).grow(0.002))
//                } else if (it instanceof LineLightSourceD) {
//                    def axis = it.to().toImmutable().subtract(it.from()).normalize().abs()
//                    IVec3D vec = (vec3d(1, 1, 1) - axis).abs() * 0.5
//
//                    drawLine(it.from().toImmutable()['x'] + vec[0], it.to().toImmutable()['x'] + vec[0] + axis)
//                    drawLine(it.from().toImmutable()['x'] - vec[0], it.to().toImmutable()['x'] - vec[0] + axis)
//
//                    drawLine(it.from().toImmutable()['y'] + vec[1], it.to().toImmutable()['y'] + vec[1] + axis)
//                    drawLine(it.from().toImmutable()['y'] - vec[1], it.to().toImmutable()['y'] - vec[1] + axis)
//
//                    drawLine(it.from().toImmutable()['z'] + vec[2], it.to().toImmutable()['z'] + vec[2] + axis)
//                    drawLine(it.from().toImmutable()['z'] - vec[2], it.to().toImmutable()['z'] - vec[2] + axis)
//                }
//            }
//        }
//    }

    if (TestGroovyClass.lastPoint != null) {
        drawBox(new Box3D(new VecMc3D(TestGroovyClass.lastPoint), new VecMc3D(player.rayTrace(4, partialTicksIn).blockPos)).offset(0.5, 0.5, 0.5).grow(0.5).grow(0.02 * rand.nextDouble()), Color.BLUE)
    }

    TestGroovyClass.boxes.each {
        drawBox(it.toDouble().grow(0.02))
    }

    TestGroovyClass.poses.each {
        drawBox(new AxisAlignedBB(it).grow(0.02), Color.GREEN)
    }

    AncientLayer1 sector = AncientLayer1StaticManager.sectorForPlayer(FMLCommonHandler.instance().minecraftServerInstance.getWorld(player.dimension).playerEntities[0])
    if (sector != null && sector.players[0].currentRoom instanceof StructureCombatRoom) {
        Field field = StructureCombatRoom.getDeclaredField("triggerBoxes")
        field.setAccessible(true)
        field.get(sector.players[0].currentRoom).each {drawBox(it.toDouble().grow(0.02))}

        Field field1 = StructureCombatRoom.getDeclaredField("spawnArea")
        field1.setAccessible(true)
        field1.get(sector.players[0].currentRoom).renderArea(1)
    }
}