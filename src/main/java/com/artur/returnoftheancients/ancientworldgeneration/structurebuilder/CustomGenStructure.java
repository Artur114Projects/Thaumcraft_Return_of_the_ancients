package com.artur.returnoftheancients.ancientworldgeneration.structurebuilder;

import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomGenStructure {
    private static final List<String> rawStructures = new ArrayList<>();
    private static final Map<String, ITRAStructure> structures = new HashMap<>();
    private static final List<String> useAirList = new ArrayList<>();
    private static boolean isUseEBS = false;
    private static boolean isUseBinary = false;
    private static boolean isUseAir = false;

    public static void put(String name) {
        if (isUseAir) useAirList.add(name);
        rawStructures.add(name);
    }

    public static void setUseEBS() {
        isUseEBS = true;
    }

    public static void setUseBinary() {
        isUseBinary = true;
    }

    public static void setUseAir() {
        isUseAir = true;
    }

    public static void register() {
        if (isUseBinary) {
            for (String s : rawStructures) {
                boolean isAir = false;
                for (String a : useAirList) {
                    if (a.equals(s)) {
                        structures.put(s, new TRAStructureBinary(s, true));
                        isAir = true;
                        break;
                    }
                }
                if (isAir) continue;
                structures.put(s, new TRAStructureBinary(s, false));
            }
            postProsesRegister();
            return;
        }
        if (isUseEBS) {
            for (String s : rawStructures) {
                structures.put(s, new TRAStructureEBS(s));
            }
            postProsesRegister();
            return;
        }

        for (String s : rawStructures) {
            structures.put(s, new TRAStructure(s));
        }
        postProsesRegister();
    }

    private static void postProsesRegister() {
        isUseAir = false;
        isUseEBS = false;
        isUseBinary = false;
        rawStructures.clear();
    }

    public static void gen(World world, int x, int y, int z, String name) {
        if (structures.containsKey(name)) {
            structures.get(name).gen(world, x, y, z);
        } else {
            throw new RuntimeException("invalid structure name: " + name);
        }
    }

    public static void registerOrGen(World world, int x, int y, int z, String name) {
        if (structures.containsKey(name)) {
            structures.get(name).gen(world, x, y, z);
        } else {
            TRAStructure structure = new TRAStructure(name);
            structures.put(name, structure);
            structure.gen(world, x, y, z);
        }
    }

    public static void delete(String name) {
        structures.remove(name);
    }
}
