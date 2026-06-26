package com.artur114.thaumrota.main

import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraft.world.chunk.storage.RegionFile

import java.nio.file.Files
import java.nio.file.StandardCopyOption

def inputDir = new File("C:\\Users\\Valense\\OneDrive\\Рабочий стол\\recomp\\in\\build")
def outDir = new File("C:\\Users\\Valense\\OneDrive\\Рабочий стол\\recomp\\out\\build")
outDir.deleteDir()
outDir.mkdirs()

try {
    inputDir.eachFileRecurse {
        File relativePath = inputDir.toPath().relativize(it.toPath()).toFile()
        File outFile = new File(outDir, relativePath.path)

        outFile.parentFile.mkdirs()
        Files.copy(it.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        println "Copied: ${it.name} -> ${outFile}"

        if (outFile.name.endsWith(".mca")) {
            this.recompileMCAFile(outFile)
        } else if (outFile.name.endsWith(".dat") || outFile.name.endsWith(".nbt")) {
            this.recompileNBTOrDATFile(outFile, outFile.name != "forcedchunks.dat")
        }
    }
} catch (Exception e) {
    e.printStackTrace()
    println "Exception, deleting"
    outDir.deleteDir()
}

void recompileMCAFile(File file) {
    println "Recompiling -> $file"

    RegionFile region = new RegionFile(file)

    for (int x = 0; x != 32; x++) {
        for (int z = 0; z != 32; z++) {
            DataInputStream input = region.getChunkDataInputStream(x, z)

            if (input == null) {
                continue
            }

            NBTTagCompound dat = CompressedStreamTools.read(input)
            input.close()

            this.recompileNBTCompound(dat)

            DataOutputStream output = region.getChunkDataOutputStream(x, z)
            CompressedStreamTools.write(dat, output)
            output.close()
        }
    }

    region.close()
}


void recompileNBTOrDATFile(File file, boolean compressed) {
    println "Recompiling -> $file"

    NBTTagCompound dat = file.withDataInputStream {
        if (compressed) {
            CompressedStreamTools.readCompressed(it)
        } else {
            CompressedStreamTools.read(it)
        }
    }

    this.recompileNBTCompound(dat)

    file.withDataOutputStream {
        if (compressed) {
            CompressedStreamTools.writeCompressed(dat, it)
        } else {
            CompressedStreamTools.write(dat, it)
        }
    }
}

@SuppressWarnings('GroovyAccessibility')
void recompileNBTCompound(NBTTagCompound dat) {
    ArrayDeque<NBTBase> queue = new ArrayDeque<>()
    queue.addLast(dat)

    while (!queue.isEmpty()) {
        NBTBase base = queue.poll()

        if (base instanceof NBTTagCompound) {
            base.keySet.each {queue.addLast(base.getTag(it))}
        } else if (base instanceof NBTTagList) {
            base.each {queue.addLast(it)}
        } else if (base instanceof NBTTagString) {
            base.data = base.string.replace("returnoftheancients", ThaumRotA.MODID)
        }
    }
}