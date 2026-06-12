package scripts

import com.artur114.thaumrota.client.event.ClientEventsHandler
import com.artur114.thaumrota.common.util.math.AreasCombiner
import com.artur114.thaumrota.common.util.math.BoundingBox
import com.artur114.thaumrota.common.util.math.IArea
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import org.apache.logging.log4j.Logger

EntityPlayer playerS = player
World worldS = worldIn
Logger logger = log
BlockPos posS = pos



if (worldS.isRemote) {

}