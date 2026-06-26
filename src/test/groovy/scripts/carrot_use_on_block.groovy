package scripts

import com.artur114.bananalib.math.m3d.box.Box3DM
import com.artur114.bananalib.math.m3d.box.Box3IM
import com.artur114.bananalib.math.m3d.vec.IVec3D
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM
import com.artur114.bananalib.mc.math.m3d.vec.VecMc3D
import com.artur114.thaumrota.client.fx.particle.ParticlePhantom
import com.artur114.thaumrota.client.fx.particle.ParticleVentStatic
import com.artur114.thaumrota.client.init.InitShaders
import com.artur114.thaumrota.client.light.EnumLightType
import com.artur114.thaumrota.client.light.ILightSource
import com.artur114.thaumrota.client.light.LineLightSource
import com.artur114.thaumrota.client.light.PointLightSource
import com.artur114.thaumrota.client.render.fx.HeatRenderer
import com.artur114.thaumrota.common.init.InitDimensions
import com.artur114.thaumrota.common.init.InitSounds
import com.artur114.thaumrota.common.network.ClientPacketCreateFX
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures.LightTemplates
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1StaticManager
import com.artur114.thaumrota.common.worldstate.ancientworld.system.client.AncientLayer1Client
import com.artur114.thaumrota.main.ThaumRotA
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager
import groovy.transform.BaseScript
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.IAttributeInstance
import net.minecraft.entity.passive.EntityPig
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.TextComponentString
import thaumcraft.client.fx.FXDispatcher
import thaumcraft.client.fx.ParticleEngine

@BaseScript
RotADevScript script

onServer {
    if (world.provider.dimension != InitDimensions.ANCIENT_WORLD_ID) {
        AncientLayer1StaticManager.intoAncientWorld(player as EntityPlayerMP)
    }
}




//
//onClient {
//    player.sendStatusMessage(new TextComponentString("[${posIn.getX() & 15}, ${posIn.getY() - 80}, ${posIn.getZ() & 15}]"), false)
////    AncientLayer1 sector = AncientLayer1StaticManager.sectorForPlayer(player)
////    ((AncientLayer1Client) sector)?.lightMap?.clear()
//
//    if (!player.isSneaking()) {
////        HeatRenderer.addLight("debug", new PointLightSource(new PosMc3IM(posIn.offset(facingIn)), HeatRenderer.HEAT_COLOR, 0.8F, 3.0F, 1.5))
////        this.addLine()
////        this.addBox()
//        this.addBox()
////        TestGroovyClass.poses.add(posIn.offset(facingIn))
//    } else {
////        HeatRenderer.removeLight("debug", posIn.offset(facingIn))
////        HeatRenderer.clearLight("debug")
////        compileLights()
//        compileBoxes()
////        compilePoses()
//
//        TestGroovyClass.boxes.removeIf {it.contains(new PosMc3IM(posIn.offset(facingIn)))}
//    }
//}
//
//void addBox() {
//    if (TestGroovyClass.lastPoint == null) {
//        TestGroovyClass.lastPoint = posIn.offset(facingIn).toImmutable(); return
//    }
//
//    TestGroovyClass.boxes << (new Box3IM(new Box3DM(new PosMc3IM(TestGroovyClass.lastPoint), new PosMc3IM(posIn.offset(facingIn))).offset(0.5, 0.5, 0.5).grow(0.5)))
//    TestGroovyClass.lastPoint = null
//}
//
//void addLine() {
//    if (TestGroovyClass.lastPoint == null) {
//        TestGroovyClass.lastPoint = posIn.offset(facingIn).toImmutable(); return
//    }
//
//    HeatRenderer.addLight("debug", new LineLightSource(new PosMc3IM(TestGroovyClass.lastPoint), new PosMc3IM(posIn.offset(facingIn)), HeatRenderer.HEAT_COLOR, 0.8F, 2.4F, 2))
//    TestGroovyClass.lastPoint = null
//}
//
//void compileLights() {
//    BlockPos start = new ChunkPos(player.position).getBlock(0, 80, 0)
//    boolean listAdd = true
//    Map<EnumLightType, List<ILightSource>> map = HeatRenderer.lights.getOrDefault("debug", Collections.emptyMap())
//    String ret = ""
//    map.forEach {EnumLightType type, List<ILightSource> sources ->
//        switch (type) {
//            case EnumLightType.POINT:
//                sources.each {
//                    PointLightSource source = it
//
//                    ret += "${listAdd ? "list.add(" : ""}new PointLightSource(new PosMc3IM(${source.pos().x - start.x}, ${source.pos().y - start.y}, ${source.pos().z - start.z}), HeatRenderer.HEAT_COLOR, ${source.brightness()}F, ${source.range()}F, ${source.heat()}F)${listAdd ? ");" : ", "}\n"
//                }
//                break
//            case EnumLightType.LINE:
//                sources.each {
//                    LineLightSource source = it
//
//                    ret += "${listAdd ? "list.add(" : ""}new LineLightSource(new PosMc3IM(${source.from().x - start.x}, ${source.from().y - start.y}, ${source.from().z - start.z}), new PosMc3IM(${source.to().x - start.x}, ${source.to().y - start.y}, ${source.to().z - start.z}), HeatRenderer.HEAT_COLOR, ${source.brightness()}F, ${source.range()}F, ${source.heat()}F)${listAdd ? ");" : ", "}\n"
//                }
//                break
//        }
//    }
//
//    log.info("\n${ret}")
//}
//
//
//void compilePoses() {
//    def listName = "list"
//    BlockPos start = new ChunkPos(player.position).getBlock(0, 80, 0)
//    boolean listAdd = false
//    String ret = ""
//    TestGroovyClass.poses.each {
//        ret += "${listAdd ? "${listName}.add(" : ""}new PosMc3IM(${it.x - start.x}, ${it.y - start.y}, ${it.z - start.z})${listAdd ? ");" : ", "}\n"
//    }
//
//    log.info("\n${ret}")
//}
//
//void compileBoxes() {
//    def listName = "list"
//    BlockPos start = new ChunkPos(player.position).getBlock(0, 80, 0)
//    boolean listAdd = true
//    String ret = ""
//    TestGroovyClass.boxes.each {
//        ret += "${listAdd ? "${listName}.add(" : ""}new Box3IM(${it.minX() - start.x}, ${it.minY() - start.y}, ${it.minZ() - start.z}, ${it.maxX() - start.x}, ${it.maxY() - start.y}, ${it.maxZ() - start.z})${listAdd ? ");" : ", "}\n"
//    }
//
//    log.info("\n${ret}")
//}