package com.artur114.thaumrota.main

import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList

def resourceDir = new File("C:\\Projects\\Thaumcraft_Return_of_the_ancients\\src\\main\\resources\\assets\\thaumrota\\structures")

resourceDir.eachFileMatch(~/.*\.nbt/) {File file ->
    file.withInputStream {
        NBTTagCompound nbt = CompressedStreamTools.readCompressed(it)
        if (!nbt.hasKey("light")) println "Structure \"${file.name}\" not have a light map!"
    }
}