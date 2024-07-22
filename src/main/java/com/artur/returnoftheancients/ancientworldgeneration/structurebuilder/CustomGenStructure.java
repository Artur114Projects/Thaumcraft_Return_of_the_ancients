package com.artur.returnoftheancients.ancientworldgeneration.structurebuilder;

import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomGenStructure {
    private static final List<String> rawStructures = new ArrayList<>();
    private static final Map<String, TRAStructure> structures = new HashMap<>();
    private static boolean isUseEBS = false;

    public static void put(String name) {
        rawStructures.add(name);
    }

    public static void setUseEBS() {
        isUseEBS = true;
    }

    public static void register() {
        if (!isUseEBS) {
            for (String s : rawStructures) {
                structures.put(s, new TRAStructure(s));
            }
        } else {
            for (String s : rawStructures) {
                structures.put(s, new TRAStructureEBS(s));
            }
            isUseEBS = false;
        }
        rawStructures.clear();
    }

    public static void please(World world, int x, int y, int z, String name) {
        if (structures.containsKey(name)) {
            structures.get(name).please(world, x, y, z);
        } else {
            throw new RuntimeException("invalid structure name: " + name);
        }
    }

    public static void registerOrPlease(World world, int x, int y, int z, String name) {
        if (structures.containsKey(name)) {
            structures.get(name).please(world, x, y, z);
        } else {
            TRAStructure structure = new TRAStructure(name);
            structures.put(name, structure);
            structure.please(world, x, y, z);
        }
    }
}
