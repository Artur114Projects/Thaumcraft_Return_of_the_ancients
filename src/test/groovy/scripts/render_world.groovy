package scripts

import com.artur114.bananalib.math.m3d.box.Box3I
import groovy.transform.BaseScript
import net.minecraft.entity.Entity
import net.minecraft.util.math.AxisAlignedBB

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
        def renPos = ePos['y'] + it.eyeHeight

        drawLine(ePos, ePos['y'] + 4, Color.WHITE)
        drawLine(renPos, renPos + entityLookVec(it) * 2, Color.WHITE)
    }
}
