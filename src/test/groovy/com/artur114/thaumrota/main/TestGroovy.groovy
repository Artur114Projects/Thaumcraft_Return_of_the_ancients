package com.artur114.thaumrota.main

import com.artur114.thaumrota.asm.ASMHookRotA
import com.artur114.thaumrota.common.worldstate.ancientworld.map.gen.GenPhase
import com.artur114.thaumrota.common.worldstate.blockprotect.BlockProtectHandler
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldProvider
import org.objectweb.asm.Type

class TestGroovy {
    static void main(String[] args) {
        CheckHasLight.newInstance().run()
    }
}
