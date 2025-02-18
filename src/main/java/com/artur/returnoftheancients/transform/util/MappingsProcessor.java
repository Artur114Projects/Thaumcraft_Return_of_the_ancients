package com.artur.returnoftheancients.transform.util;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MappingsProcessor {
    public static boolean LOADED = false;
    private static final Map<String, String> methods = new HashMap<>(9656);

    public static void unload() {
        methods.clear();
    }

    public static void load() {
        if (LOADED) {
            return;
        }
        try {
            System.out.println("Loading mappings start!");
            loadMethods();
            System.out.println("Loading mappings complete!");
            LOADED = true;
        } catch (IOException e) {
            e.printStackTrace(System.err);
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
            new NullPointerException().printStackTrace(System.err);
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            map.put(values[1], values[0]);
        }
    }

    @NotNull
    public static String getObfuscateMethodName(String name) {
        if (FMLLaunchHandler.isDeobfuscatedEnvironment()) {
            return name;
        }
        load();
        String obfuscatedName = methods.get(name);
        if (obfuscatedName == null) {
            return name;
        }
        return obfuscatedName;
    }
}
