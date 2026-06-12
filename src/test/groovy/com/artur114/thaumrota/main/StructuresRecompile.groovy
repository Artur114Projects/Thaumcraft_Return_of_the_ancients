package com.artur114.thaumrota.main

import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList


def resourceDir = new File("src/main/resources/assets/thaumrota/structures")
def outDir = new File("recompile/out/structures")
outDir.mkdirs()

resourceDir.eachFileMatch(~/.*\.nbt/) {file ->
    file.withDataInputStream {input ->
        NBTTagCompound nbt = CompressedStreamTools.readCompressed(input)
        NBTTagList palette = nbt.getTagList("palette", 10)

        palette.each {paleBase ->
            NBTTagCompound pale = paleBase as NBTTagCompound
            if (pale.hasKey("Name")) {
                pale.setString("Name", pale.getString("Name").replace("returnoftheancients", "thaumrota"))
            }
        }

        def outFile = new File(outDir, file.name)

        outFile.withDataOutputStream {output ->
            CompressedStreamTools.writeCompressed(nbt, output)
        }

        println "Recompiled structure: ${file.name} -> ${outFile}"
    }
}