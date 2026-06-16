package scripts

import com.artur114.bananalib.math.m3d.vec.IVec3D
import com.artur114.bananalib.math.m3d.vec.Vec3D
import com.artur114.bananalib.math.m3d.vec.Vec3DM
import com.artur114.bananalib.mc.math.m3d.vec.VecMc3D
import com.artur114.thaumrota.client.init.InitShaders
import com.artur114.thaumrota.client.light.LightSource
import com.artur114.thaumrota.client.light.LineLightSource
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1StaticManager
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager
import groovy.transform.BaseScript
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextComponentString

import java.awt.Color

@BaseScript
RotADevScript script

onClient {
    player.sendStatusMessage(new TextComponentString(world.getLight(posIn) + ""), true)
}

//onClient {
//    Color heat = new Color(214, 111, 29)
//    try {
//        if (player.isSneaking()) {
//            InitShaders.COLORED_LIGHT.onPreInit()
//            InitShaders.TEST_SHADER.onPreInit()
//            TestGroovyClass.pointLight.clear()
//            TestGroovyClass.lineLight.clear()
//        } else {
//            IVec3D vec = new VecMc3D(posIn.offset(facingIn)).add(0.5, 0.5, 0.5)//.add((rand.nextDouble() * 32) - 16, 0, (rand.nextDouble() * 32) - 16)
////            TestGroovyClass.pointLight.add(new LightSource(heat, new Vec3DM(vec), 2, 0.1))
//            this.addLine(heat, vec)
//        }
//    } catch (Exception e) {
//        log.error(e)
//    }
//}
//
//
//void addLine(Color heat, IVec3D vec) {
//    if (TestGroovyClass.lastPoint == null) {
//        TestGroovyClass.lastPoint = new Vec3DM(vec); return
//    }
//
//    TestGroovyClass.lineLight.add(new LineLightSource(0.2, 2, heat, TestGroovyClass.lastPoint, new Vec3DM(vec)))
//    TestGroovyClass.lastPoint = null;
//}