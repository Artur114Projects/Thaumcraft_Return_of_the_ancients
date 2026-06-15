package com.artur114.thaumrota.main

import com.artur114.thaumrota.asm.ASMHookRotA
import com.artur114.thaumrota.common.worldstate.blockprotect.BlockProtectHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.objectweb.asm.Type

class TestGroovy {
    static void main(String[] args) {
        println Type.getMethodDescriptor(ASMHookRotA.getMethods().find {it.name == "isClientPlayerInTaintBiome"})
    }
}
