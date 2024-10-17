package com.artur.returnoftheancients.transform.util;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MappingsProcessor {
    private static Map<String, String> methods = new HashMap<>();

    public static void unload() {
        methods.clear();
    }

    public static void load() {
        try {
            System.out.println("Loading mappings start!");
            loadMethods();
            System.out.println("Loading mappings complete!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadMethods() throws IOException {
        String resourcePath = "methods.csv";
        loadCSVMappings(methods, resourcePath);
        System.out.println("Loading methods complete!");
    }

    private static void loadCSVMappings(Map<String, String> map, String fileName) throws IOException {
        InputStream stream = MinecraftServer.class.getResourceAsStream("/assets/returnoftheancients/transform/mappings/" + fileName);
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
