package com.artur.returnoftheancients.transform.util;

import com.artur.returnoftheancients.main.MainR;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.common.launcher.FMLDeobfTweaker;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MappingsProcessor {
    private static Map<String, String> methods = new HashMap<>();

    public static void unload() {
        methods.clear();
    }

    public static void load() {
        try {
            System.out.println("Loading mappings start!");
            loadMethods();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadMethods() throws IOException {
        String resourcePath = "/assets/returnoftheancients/transform/mappings/methods.csv";
        loadCSVMappings(methods, resourcePath);
        System.out.println("Loading methods complete");
    }

    private static void loadCSVMappings(Map<String, String> map, String resourcePath) throws IOException {
        InputStream stream = MinecraftServer.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            new NullPointerException().printStackTrace();
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            map.put(values[1], values[0]);
        }
    }

    public static String getObfuscateMethodName(String name) {
        if (FMLLaunchHandler.isDeobfuscatedEnvironment()) {
            return name;
        }
        return methods.get(name);
    }
}
