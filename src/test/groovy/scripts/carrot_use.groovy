package scripts

import com.artur114.bananalib.math.core.m3d.vec.IVec3DC
import com.artur114.bananalib.math.m3d.matrix.Matrix3DM
import com.artur114.bananalib.math.m3d.vec.IVec3D
import com.artur114.bananalib.math.m3d.vec.Vec3D
import com.artur114.bananalib.math.m3d.vec.Vec3DM
import com.artur114.bananalib.mc.math.m3d.vec.VecMc3D
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.EnumParticleTypes
import net.minecraft.world.World

if (worldIn.isRemote) {
    EntityPlayerSP player = Minecraft.minecraft.player
    VecMc3D lookVec = new VecMc3D(player.lookVec)
    VecMc3D vec = lookVec.scale(5).add(new VecMc3D(player).add(0, player.eyeHeight, 0))

    Vec3DM vec3DM = Vec3DM.obtain()
    Random rand = new Random()
    int maxParticles = 16
    for (j in 1..maxParticles) {
        vec3DM.set(1, 0, 0).scale(0.25).rotateY(360 * (j / maxParticles) + rand.nextDouble() * 20)
        for (i in 1..maxParticles) {
            spawnParticle(EnumParticleTypes.SMOKE_NORMAL, vec, vec3DM.rotateX(360 * (i / maxParticles) + rand.nextDouble() * 20))
        }
    }

    double speed = 0.8

    for (j in 1..6) {
        for (i in 1..maxParticles) {
            spawnParticle(EnumParticleTypes.SMOKE_NORMAL, vec, vec3DM.set(1, 0, 0).scale(speed * (j / 6) + 0.1).rotateY(360 * (i / maxParticles) + 30))
        }
    }

    for (j in 1..6) {
        for (i in 1..maxParticles) {
            spawnParticle(EnumParticleTypes.SMOKE_NORMAL, vec, vec3DM.set(1, 0, 0).scale(speed * (j / 6)).rotateY(360 * (i / maxParticles) + rand.nextDouble() * 20))
        }
    }

    for (j in 1..6) {
        for (i in 1..maxParticles) {
            spawnParticle(EnumParticleTypes.SMOKE_NORMAL, vec, vec3DM.set(1, 0, 0).scale(speed).rotateY(360 * (i / maxParticles) + rand.nextDouble() * 20))
        }
    }


    Vec3DM.release(vec3DM)
}

private void spawnParticle(EnumParticleTypes type, IVec3DC pos, IVec3DC move) {
    worldIn.spawnParticle(type, false, pos.x(), pos.y(), pos.z(), move.x(), move.y(), move.z())
}