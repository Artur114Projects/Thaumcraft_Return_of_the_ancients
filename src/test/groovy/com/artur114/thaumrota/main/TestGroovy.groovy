package com.artur114.thaumrota.main

import com.artur114.thaumrota.asm.ASMHookRotA
import com.artur114.thaumrota.common.worldstate.blockprotect.BlockProtectHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldProvider
import org.objectweb.asm.Type

class TestGroovy {
    static void main(String[] args) {
        println new Random().nextInt(1000);
//        println Type.getMethodDescriptor(WorldProvider.getMethods().find {it.name == "getLightmapColors"})
    }
}
