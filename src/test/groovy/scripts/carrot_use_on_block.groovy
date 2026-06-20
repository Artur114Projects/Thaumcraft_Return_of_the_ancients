package scripts

import com.artur114.bananalib.math.m3d.vec.IVec3D
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM
import com.artur114.bananalib.mc.math.m3d.vec.VecMc3D
import com.artur114.thaumrota.client.init.InitShaders
import com.artur114.thaumrota.client.light.EnumLightType
import com.artur114.thaumrota.client.light.ILightSource
import com.artur114.thaumrota.client.light.LineLightSource
import com.artur114.thaumrota.client.light.PointLightSource
import com.artur114.thaumrota.client.render.fx.HeatRenderer
import com.artur114.thaumrota.common.init.InitDimensions
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures.LightTemplates
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1StaticManager
import com.artur114.thaumrota.common.worldstate.ancientworld.system.client.AncientLayer1Client
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager
import groovy.transform.BaseScript
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.text.TextComponentString

@BaseScript
RotADevScript script


onClient {
    player.sendStatusMessage(new TextComponentString("[${posIn.getX() & 15}, ${posIn.getY() - 80}, ${posIn.getZ() & 15}]"), false)
//    ((AncientLayer1Client) sector)?.lightMap?.clear()
    if (!player.isSneaking()) {
//        HeatRenderer.addLight("debug", new PointLightSource(new PosMc3IM(posIn.offset(facingIn)), HeatRenderer.HEAT_COLOR, 0.3F, 4.0F, 1.5))
//        this.addLine()
    } else {
//        HeatRenderer.removeLight("debug", posIn.offset(facingIn))
//        HeatRenderer.clearLight("debug")
//        compileLights()
    }
}

onServer {
    if (world.provider.dimension != InitDimensions.ANCIENT_WORLD_ID) {
        AncientLayer1StaticManager.intoAncientWorld(player as EntityPlayerMP)
    } else {
        def str = [
//                "ancient_boss.nbt": 64,
//                "ancient_big_hot_room.nbt": 64,
//                "ancient_entry.nbt": 64,
//                "ancient_hot_room.nbt": 64,
//                "ancient_long_room_rotate-1.nbt": 64,
//                "ancient_water_room.nbt": 64
        ]

        int o = 0
        str.forEach {String name, int off ->
            println "building: $name"
            StructuresBuildManager.createBuildRequest(world, pos(o, 100, 0), name.replace(".nbt", "")).setIgnoreAir().build()
            o += off
        }
    }
}


void addLine() {
    if (TestGroovyClass.lastPoint == null) {
        TestGroovyClass.lastPoint = posIn.offset(facingIn).toImmutable(); return
    }

    HeatRenderer.addLight("debug", new LineLightSource(new PosMc3IM(TestGroovyClass.lastPoint), new PosMc3IM(posIn.offset(facingIn)), HeatRenderer.HEAT_COLOR, 0.6F, 3.5F, 1))
    TestGroovyClass.lastPoint = null
}

void compileLights() {
    BlockPos start = new ChunkPos(player.position).getBlock(0, 100, 0)
    boolean listAdd = true
    Map<EnumLightType, List<ILightSource>> map = HeatRenderer.lights.getOrDefault("debug", Collections.emptyMap())
    String ret = ""
    map.forEach {EnumLightType type, List<ILightSource> sources ->
        switch (type) {
            case EnumLightType.POINT:
                sources.each {
                    PointLightSource source = it

                    ret += "${listAdd ? "list.add(" : ""}new PointLightSource(new PosMc3IM(${source.pos().x - start.x}, ${source.pos().y - start.y}, ${source.pos().z - start.z}), HeatRenderer.HEAT_COLOR, ${source.brightness()}F, ${source.range()}F, ${source.heat()}F)${listAdd ? ");" : ", "}\n"
                }
                break
            case EnumLightType.LINE:
                sources.each {
                    LineLightSource source = it

                    ret += "${listAdd ? "list.add(" : ""}new LineLightSource(new PosMc3IM(${source.from().x - start.x}, ${source.from().y - start.y}, ${source.from().z - start.z}), new PosMc3IM(${source.to().x - start.x}, ${source.to().y - start.y}, ${source.to().z - start.z}), HeatRenderer.HEAT_COLOR, ${source.brightness()}F, ${source.range()}F, ${source.heat()}F)${listAdd ? ");" : ", "}\n"
                }
                break
        }
    }

    log.info("\n${ret}")
}