package scripts

import com.artur114.thaumrota.client.light.EnumLightType
import com.artur114.thaumrota.client.render.fx.HeatRenderer
import com.artur114.thaumrota.common.init.InitDimensions
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumRotate
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.IStructureType
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.StrPos
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1StaticManager
import groovy.transform.BaseScript
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.FMLCommonHandler

@BaseScript
RotADevScript script

if (eventIn.type != RenderGameOverlayEvent.ElementType.ALL) {
    return
}

List debug = [
    "FPS: ${Minecraft.minecraft.debugFPS}",
    "Lights count L:${HeatRenderer.debugLightsCount(EnumLightType.LINE)} P:${HeatRenderer.debugLightsCount(EnumLightType.POINT)}",
    "Rendered lights count L:${HeatRenderer.debugLightsToRenCount(EnumLightType.LINE)} P:${HeatRenderer.debugLightsToRenCount(EnumLightType.POINT)}"
]

AncientLayer1 sector = AncientLayer1StaticManager.sectorForPlayer(player)
AncientLayer1 server = null
try {
    server = AncientLayer1StaticManager.sectorForPlayer(FMLCommonHandler.instance().minecraftServerInstance.getWorld(InitDimensions.ANCIENT_WORLD_ID).playerEntities[0])
} catch (Exception ignored) {}

if (sector != null) {
    StrPos pos = sector.players[0].calculatePosOnMap(sector.pos, sector.size)
    IStructureType type = sector.map().structure(pos)?.type()
    EnumRotate rotate = sector.map().structure(pos)?.rotate()
    boolean flag = true
    if (server != null) {
        IStructureType typeS = server.map().structure(pos)?.type()
        EnumRotate rotateS = server.map().structure(pos)?.rotate()

        if (typeS != type || rotateS != rotate) {
            debug << "${TextFormatting.RED}Desyned!"
            debug << "${TextFormatting.RED}Server str: ${typeS}, rot: ${rotateS}"
            debug << "${TextFormatting.RED}Client str: ${type}, rot: ${rotate}"
            flag = false;
        }
    }
    if (flag) {
        debug << "Current str: ${type}, rot: ${rotate}"
    }
}

RayTraceResult res = player.rayTrace(8, partialTicksIn)

if (res.typeOfHit == RayTraceResult.Type.BLOCK) {
    debug << "Look at [${res.blockPos.x}, ${res.blockPos.y}, ${res.blockPos.z}]"
}


this.renderList(debug)

void renderList(List<String> list) {
    FontRenderer font = Minecraft.minecraft.fontRenderer;
    int id = 0

    list.each {
        font.drawStringWithShadow(it, 2, id * (font.FONT_HEIGHT + 1) + 3, 14737632); id++
    }
}