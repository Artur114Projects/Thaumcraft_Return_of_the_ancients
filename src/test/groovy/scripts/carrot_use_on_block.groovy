package scripts

import com.artur114.bananalib.math.m3d.vec.IVec3D
import com.artur114.bananalib.math.m3d.vec.Vec3DM
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM
import com.artur114.bananalib.mc.math.m3d.vec.VecMc3D
import com.artur114.thaumrota.client.light.PointLightSource
import com.artur114.thaumrota.client.render.fx.HeatRenderer
import com.artur114.thaumrota.common.init.InitDimensions
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1StaticManager
import groovy.transform.BaseScript
import net.minecraft.util.text.TextComponentString

import java.awt.Color

@BaseScript
RotADevScript script

onClient {
    player.sendStatusMessage(new TextComponentString("[${posIn.getX() & 15}, ${posIn.getY() - 80}, ${posIn.getZ() & 15}]"), false)
    player.sendStatusMessage(new TextComponentString(HeatRenderer.lineLights.toString()), false)
}

onServer {
    if (world.provider.dimension != InitDimensions.ancient_world_dim_id) {
        AncientLayer1StaticManager.intoAncientWorld(player)
    }
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
//            HeatRenderer.addLight(new PointLightSource(new PosMc3IM(vec), heat, 0.05, 2, 1))
////            this.addLine(heat, vec)
//        }
//    } catch (Exception e) {
//        log.error(e)
//    }
//}


//void addLine(Color heat, IVec3D vec) {
//    if (TestGroovyClass.lastPoint == null) {
//        TestGroovyClass.lastPoint = new Vec3DM(vec); return
//    }
//
//    LightRenderer.lineLight.add(new LineLightSource(0.2, 2, heat, TestGroovyClass.lastPoint, new Vec3DM(vec)))
//    TestGroovyClass.lastPoint = null;
//}