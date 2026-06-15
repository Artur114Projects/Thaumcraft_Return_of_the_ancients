package scripts

import com.artur114.bananalib.math.m3d.box.Box3I
import com.artur114.bananalib.math.m3d.vec.Vec3DM
import groovy.transform.BaseScript
import net.minecraft.entity.Entity

import java.awt.Color

@BaseScript
RotADevScript script

if (true) {
    return
}

prepareToDraw {
    def area = vec3d(64, 64, 64)
    def box = Box3I.EMPTY.include(playerPos() - area).include(playerPos() + area)

    allEntityInBox(box, Entity).each {
        if (it == player) return
        def ePos = entityPosToRen(it)
        def ePosE = ePos['y'] + 2

        drawLine(ePos, ePos['y'] + 4, Color.WHITE)

        def vec = vec3d(1, 0, 0)
        def vecPrev = vec

        int count = 16
        for (i in 1..count) {
            def rot = vec.rotateY(360 * (i / count))
            drawLine(vecPrev + ePosE, rot + ePosE, Color.WHITE)
            vecPrev = rot
        }
    }
}
