package scripts

import com.artur114.thaumrota.client.event.ClientEventsHandler
import com.artur114.thaumrota.common.generation.portal.base.AncientPortalsProcessor
import com.artur114.thaumrota.common.generation.terraingen.GenLayersHandler
import com.artur114.thaumrota.common.util.math.AreasCombiner
import com.artur114.thaumrota.common.util.math.BoundingBox
import com.artur114.thaumrota.common.util.math.IArea
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1StaticManager
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager
import groovy.transform.BaseScript
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import org.apache.logging.log4j.Logger

@BaseScript
RotADevScript script

onServer {
    ChunkPos[] portalsGenerationPos = new ChunkPos[AncientPortalsProcessor.portalsCount];
    GenLayersHandler.initPortalsPosOnWorld(portalsGenerationPos, world.provider.seed);

}