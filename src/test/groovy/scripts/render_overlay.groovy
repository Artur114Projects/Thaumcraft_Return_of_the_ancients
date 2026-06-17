package scripts

import com.artur114.thaumrota.client.light.EnumLightType
import com.artur114.thaumrota.client.render.fx.HeatRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraftforge.client.event.RenderGameOverlayEvent

if (eventIn.type != RenderGameOverlayEvent.ElementType.ALL) {
    return
}

def debug = [
    "FPS: ${Minecraft.minecraft.debugFPS}",
    "Lights count L:${HeatRenderer.debugLightsCount(EnumLightType.LINE)} P:${HeatRenderer.debugLightsCount(EnumLightType.POINT)}",
    "Rendered lights count L:${HeatRenderer.debugLightsToRenCount(EnumLightType.LINE)} P:${HeatRenderer.debugLightsToRenCount(EnumLightType.POINT)}"
]

this.renderList(debug)

void renderList(List<String> list) {
    FontRenderer font = Minecraft.minecraft.fontRenderer;
    int id = 0

    list.each {
        font.drawStringWithShadow(it, 2, id * (font.FONT_HEIGHT + 1) + 3, 14737632)
        id++
    }
}