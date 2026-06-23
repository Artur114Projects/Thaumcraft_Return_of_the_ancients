
package scripts

import com.artur114.bananalib.math.core.m3d.vec.IVec3IC
import com.artur114.bananalib.math.m3d.box.Box3I
import com.artur114.bananalib.math.m3d.box.Box3IM
import com.artur114.bananalib.math.m3d.box.IBox3I
import com.artur114.bananalib.math.m3d.vec.IVec3D
import com.artur114.bananalib.math.m3d.vec.Vec3D
import com.artur114.bananalib.math.m3d.vec.Vec3DM
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM
import com.artur114.bananalib.util.graphs.BananaGraphs
import com.artur114.thaumrota.client.fx.particle.ParticleVentStatic
import com.artur114.thaumrota.client.light.ILightSource
import com.artur114.thaumrota.client.light.LineLightSource
import com.artur114.thaumrota.client.light.LineLightSourceD
import com.artur114.thaumrota.client.util.LightCompressor
import com.artur114.thaumrota.common.init.InitSounds
import groovy.transform.BaseScript
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import org.jetbrains.annotations.Contract
import thaumcraft.client.fx.ParticleEngine

@BaseScript
RotADevScript script

BlockPos pos = posIn.down()
Random rand = new Random()
for (i in 1..4) {
    for (j in 0..6) {
        def vec = vec3d(pos.x + 0.5, pos.y + 1.0, pos.z + 0.5)
        def move = vec3d(1, 0, 0).rotateY(360 * (j / 6) + rand.nextDouble() * 45);
        def p = vec + move * 0.8
        double y = 1.5 * (i / 4)
        def fb = new ParticleVentStatic(p.x, p.y + y, p.z, (move * 0.000005), 0)
        fb.setAlphaF(0.4F);
        fb.setScale(8);
        ParticleEngine.addEffect(world, fb);

        if (rand.nextBoolean()) {
            p = vec + move * 2.8
            fb = new ParticleVentStatic(p.x, p.y + y, p.z, (move * 0.000005), 0)
            fb.setAlphaF(0.4F);
            fb.setScale(8);
            ParticleEngine.addEffect(world, fb);
        }
    }
}
((WorldClient) world).playSound(pos, InitSounds.MAGIC_PUFF, SoundCategory.AMBIENT, 1111.5, 1, false);