package com.artur114.thaumrota.common.util;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DevScriptsRunner {
    private static final Logger log = LogManager.getLogger("GroovyScripts");
    private static final Path DEV_SCRIPTS = Paths.get("..", "src/test/groovy/scripts").toAbsolutePath().normalize();
    private static final Map<Path, DevScript> CACHE = new ConcurrentHashMap<>();

    public static void run(String scriptName, Object... args) {
        if (!isDev()) {
            return;
        }

        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("args.length is not even");
        }

        String[] argsIds = new String[args.length / 2];
        Object[] argsArr = new Object[args.length / 2];

        for (int i = 0; i != args.length; i++) {
            int index = i / 2;
            if (i % 2 == 0) {
                argsIds[index] = args[i].toString();
            } else {
                argsArr[index] = args[i];
            }
        }

        run(scriptName, argsIds, argsArr);
    }

    public static void run(String scriptName, String[] argsIds, Object[] args) {
        if (!isDev()) {
            return;
        }

        if (args.length != argsIds.length) {
            throw new IllegalArgumentException("args.length != argsIds.length");
        }

        try {
            Binding binding = new Binding();
            binding.setProperty("log", log);
            binding.setProperty("scripts", DevScriptsRunner.class);

            for (int i = 0; i != argsIds.length; i++) {
                binding.setProperty(argsIds[i], args[i]);
            }

            GroovyShell shell = new GroovyShell();
            Path scriptPath = DEV_SCRIPTS.resolve(scriptName);
            DevScript script = CACHE.computeIfAbsent(scriptPath, path -> {
                try {
                    log.info("Parsing script {}", path);
                    return new DevScript(Files.getLastModifiedTime(path), shell.parse(path.toFile()));
                } catch (IOException e) {
                    log.warn("Failed to parse script: [{}]", scriptName, e); return null;
                }
            });
            if (script == null) {
                CACHE.remove(scriptPath); return;
            }
            FileTime newTime = Files.getLastModifiedTime(scriptPath);
            if (script.isMod(newTime)) {
                log.info("Parsing script {}", scriptPath);
                script.update(newTime, shell.parse(scriptPath.toFile()));
            }
            ((Script) script.script).setBinding(binding);
            ((Script) script.script).run();
        } catch (IOException e) {
            log.warn("Failed to run script: [{}]", scriptName, e);
        } catch (Exception e) {
            log.warn("Exception while attempt to run script: [{}]", scriptName, e);
        }
    }

    public static boolean isDev() {
        return FMLLaunchHandler.isDeobfuscatedEnvironment();
    }

    private static class DevScript {
        private FileTime lastModTime;
        private Object script;

        public DevScript(FileTime lastModTime, Object script) {
            this.lastModTime = lastModTime;
            this.script = script;
        }

        private void update(FileTime lastModTime, Object script) {
            this.lastModTime = lastModTime;
            this.script = script;
        }

        private boolean isMod(FileTime newMod) {
            return lastModTime.compareTo(newMod) < 0;
        }
    }
}
