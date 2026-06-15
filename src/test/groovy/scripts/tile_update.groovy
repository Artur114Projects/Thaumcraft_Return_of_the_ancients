package scripts

import com.artur114.bananalib.math.m3d.matrix.Matrix3D
import com.artur114.thaumrota.client.event.ClientEventsHandler
import com.artur114.thaumrota.common.tileentity.TileEntityForDev
import groovy.transform.BaseScript
import net.minecraft.client.Minecraft
import net.minecraft.util.math.BlockPos
import thaumcraft.client.fx.FXDispatcher

@BaseScript
RotADevScript script

onClient {
    double move = moveProcess(40.0F)
    double bound = 0.2
    if (move > 1 - bound) {
        double norm = Math.min((move - (1 - bound)) * (1 / bound) + 0.4, 1)
        Random rand = new Random()
        int maxParticles = 10 * norm

        def vec = vec3d(0, 1, 0)
        def vecCross = vec3d(1, 0, 0)
        for (i in 1..maxParticles) {
            Matrix3D matrix = Matrix3D.IDENTITY.rotate(360 * (i / maxParticles) + rand.nextDouble() * 45, vec)
            def rot = matrix.transform(vecCross)
            def off = rot * 0.0
            FXDispatcher.INSTANCE.drawVentParticles(pos.x + 0.5 + off.x, pos.y + 5.0F / 16.0F + off.y, pos.z + 0.5 + off.z, rot.x, rot.y, rot.z, 0xa3a3a3, 0.7)
        }
    }
}


BlockPos getPos() {
    return tile.getPos()
}

TileEntityForDev getTile() {
    return tileIn
}

float moveProcess(float max) {
    float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
    return (float) ((ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedUnloadGameTickCounter(partialTicks)) % max) / max;
}