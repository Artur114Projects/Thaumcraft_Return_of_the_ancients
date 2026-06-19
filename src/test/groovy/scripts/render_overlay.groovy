package scripts

import com.artur114.thaumrota.client.light.EnumLightType
import com.artur114.thaumrota.client.render.fx.HeatRenderer
import com.artur114.thaumrota.common.init.InitDimensions
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.StrPos
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1StaticManager
import groovy.transform.BaseScript
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.util.math.RayTraceResult
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.server.FMLServerHandler

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

if (sector != null) {
    StrPos pos = sector.players[0].calculatePosOnMap(sector.pos, sector.size)
    debug << "Current str: ${sector.map().structure(pos)?.type()}, rot: ${sector.map().structure(pos)?.rotate()}"
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