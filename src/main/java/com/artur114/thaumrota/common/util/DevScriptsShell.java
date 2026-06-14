package com.artur114.thaumrota.common.util;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DevScriptsShell {
    private static final Logger log = LogManager.getLogger("GroovyScripts");
    private final Map<Path, DevScript> cache = new ConcurrentHashMap<>();
    private final List<GroovyClass> groovyClasses = new ArrayList<>();
    private Object groovyClassLoader;
    private final Path scriptsPath;
    private Object groovyShell;

    public DevScriptsShell(Path scriptsPath) {
        this.scriptsPath = scriptsPath;
    }

    public DevScriptsShell newClass(File file) {
        this.groovyClasses.add(new GroovyClass(file.toPath()));
        return this;
    }

    public DevScriptsShell newClassInBasePath(String name) {
        this.groovyClasses.add(new GroovyClass(this.scriptsPath.resolve(name)));
        return this;
    }

    public void evaluate(String scriptName, Object... args) {
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
                if (!(args[i] instanceof String)) {
                    throw new IllegalArgumentException("Arg key must be String");
                }
                argsIds[index] = ((String) args[i]);
            } else {
                argsArr[index] = args[i];
            }
        }

        this.evaluate(scriptName, argsIds, argsArr);
    }

    public synchronized void evaluate(String scriptName, String[] argsIds, Object[] args) {
        if (!isDev()) {
            return;
        }

        if (args.length != argsIds.length) {
            throw new IllegalArgumentException("args.length != argsIds.length");
        }

        try {
            boolean isAnyChanged = false;
            Path scriptPath = this.scriptsPath.resolve(scriptName);
            for (GroovyClass clazz : this.groovyClasses) {
                FileTime time = Files.getLastModifiedTime(clazz.pathToClass);
                if (clazz.lastModTime == null || clazz.isMod(time)) {
                    clazz.lastModTime = time; isAnyChanged = true;
                }
            }
            DevScript script = this.cache.get(scriptPath);

            if (script != null) {
                FileTime time = Files.getLastModifiedTime(scriptPath);

                if (script.isMod(time)) {
                    isAnyChanged = true;
                    script.newScript = null;
                    script.lastModTime = time;
                }
            }

            if (isAnyChanged) {
                if (this.groovyClassLoader != null) {
                    ((GroovyClassLoader) this.groovyClassLoader).close();
                }
                GroovyClassLoader l = (GroovyClassLoader) (this.groovyClassLoader = new GroovyClassLoader());
                this.groovyShell = new GroovyShell(l);
            }

            GroovyClassLoader loader = (GroovyClassLoader) (
                this.groovyClassLoader == null ?
                this.groovyClassLoader = new GroovyClassLoader() :
                this.groovyClassLoader
            );
            GroovyShell shell = (GroovyShell) (
                this.groovyShell == null ?
                this.groovyShell = new GroovyShell(loader) :
                this.groovyShell
            );

            if (isAnyChanged) {
                for (GroovyClass clazz : this.groovyClasses) {
                    try {
                        log.info("Parsing groovy class {}", clazz.pathToClass);
                        loader.parseClass(clazz.pathToClass.toFile());
                    } catch (Exception e) {
                        log.warn("Failed to parse groovy class: [{}]", clazz.pathToClass, e);
                    }
                }
                this.cache.clear();
                script = null;
            }

            if (script == null) {
                try {
                    log.info("Parsing script {}", scriptPath);
                    script = new DevScript(scriptName, Files.getLastModifiedTime(scriptPath), shell.parse(scriptPath.toFile()).getClass().getDeclaredConstructor());
                } catch (IOException | NoSuchMethodException e) {
                    log.warn("Failed to parse script: [{}]", scriptName, e);
                }
            }

            if (script == null) {
                return;
            }

            if (script.newScript == null) {
                log.info("Parsing script {}", scriptPath);
                script.isBroken = false;
                script.newScript = shell.parse(scriptPath.toFile()).getClass().getDeclaredConstructor();
            }

            this.cache.put(scriptPath, script);
            this.runScript(script, argsIds, args);
        } catch (IOException e) {
            log.warn("Failed to process script: [{}]", scriptName, e);
        } catch (Exception e) {
            log.warn("Exception while attempt to process script: [{}]", scriptName, e);
        }
    }

    public static boolean isDev() {
        return FMLLaunchHandler.isDeobfuscatedEnvironment();
    }

    private void runScript(DevScript script, String[] argsIds, Object[] args) {
        Binding binding = new Binding();
        binding.setProperty("logIn", log);
        binding.setProperty("shellIn", this);

        for (int i = 0; i != argsIds.length; i++) {
            binding.setProperty(argsIds[i] + "In", args[i]);
        }

        if (script.isBroken) {
            return;
        }

        try {
            Script s = (Script) script.newScript.newInstance();
            s.setBinding(binding);
            s.run();
        } catch (Exception e) {
            log.warn("Failed to run script: [{}]", script.name, e);
            script.isBroken = true;
        }
    }

    private static class DevScript {
        private boolean isBroken = false;
        private final String name;
        private FileTime lastModTime;
        private Constructor<?> newScript;

        public DevScript(String name, FileTime lastModTime, Constructor<?> newScript) {
            this.lastModTime = lastModTime;
            this.newScript = newScript;
            this.name = name;
        }

        private boolean isMod(FileTime newMod) {
            return lastModTime.compareTo(newMod) < 0;
        }
    }

    private static class GroovyClass {
        private final Path pathToClass;
        private FileTime lastModTime;

        public GroovyClass(Path pathToClass) {
            this.pathToClass = pathToClass;
        }

        private boolean isMod(FileTime newMod) {
            return this.lastModTime.compareTo(newMod) < 0;
        }
    }
}
