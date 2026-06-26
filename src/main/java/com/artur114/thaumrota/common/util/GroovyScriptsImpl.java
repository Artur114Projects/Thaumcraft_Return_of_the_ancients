package com.artur114.thaumrota.common.util;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroovyScriptsImpl implements IGroovyEngine {
    private static final Logger log = LogManager.getLogger("GroovyScripts");
    private final Map<Path, GroovyScript> cache = new ConcurrentHashMap<>();
    private final List<GroovyClass> groovyClasses = new ArrayList<>();
    private GroovyClassLoader classLoader = new GroovyClassLoader();
    private GroovyClassLoader scriptLoader = new GroovyClassLoader(this.classLoader);
    private GroovyShell groovyShell = new GroovyShell(this.scriptLoader);

    @Override
    public void loadClass(Path clazz) {
        this.groovyClasses.add(new GroovyClass(clazz));
    }

    @Override
    public void evaluate(Path script, Object... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("args.length is not even");
        }

        Binding binding = new Binding();
        binding.setProperty("logIn", log);
        binding.setProperty("shellIn", this);
        String str = null;

        for (int i = 0; i != args.length; i++) {
            int index = i / 2;
            if (i % 2 == 0) {
                if (!(args[i] instanceof String)) throw new IllegalArgumentException("Arg key must be String");
                str = ((String) args[i]);
            } else {
                binding.setProperty(str + "In", args[i]);
            }
        }

        this.processScript(script, binding);
    }

    @Override
    public void evaluate(Path scriptPath, String[] argsIds, Object[] args) {
        if (args.length != argsIds.length) {
            throw new IllegalArgumentException("args.length != argsIds.length");
        }

        Binding binding = new Binding();
        binding.setProperty("logIn", log);
        binding.setProperty("shellIn", this);

        for (int i = 0; i != argsIds.length; i++) {
            binding.setProperty(argsIds[i] + "In", args[i]);
        }

        this.processScript(scriptPath, binding);
    }

    private void processScript(Path scriptPath, Binding binding) {
        try {
            if (this.checkAndUpdateModTimeC()) {
                if (this.classLoader != null) this.classLoader.close();
                if (this.scriptLoader != null) this.scriptLoader.close();
                this.classLoader = new GroovyClassLoader();
                this.scriptLoader = new GroovyClassLoader(this.classLoader);
                this.groovyShell = new GroovyShell(this.scriptLoader);
                this.cache.clear();
                this.reparseClasses();
            } else if (this.checkAndUpdateModTimeS(scriptPath)) {
                this.cache.remove(scriptPath);
                if (this.scriptLoader != null) this.scriptLoader.close();
                this.scriptLoader = new GroovyClassLoader(this.classLoader);
                this.groovyShell = new GroovyShell(this.scriptLoader);
            }

            GroovyScript script = this.findScript(scriptPath);

            if (script == null) return;

            this.runScript(script, binding);
        } catch (IOException e) {
            log.warn("Failed to process script: [{}]", scriptPath, e);
        } catch (Exception e) {
            log.warn("Exception while attempt to process script: [{}]", scriptPath, e);
            GroovyScript script = this.cache.get(scriptPath);
            if (script != null) script.isBroken = true;
        }
    }

    private void reparseClasses() {
        for (GroovyClass clazz : this.groovyClasses) {
            try {
                log.info("Parsing groovy class {}", clazz.pathToClass);
                this.classLoader.parseClass(clazz.pathToClass.toFile());
            } catch (Exception e) {
                log.warn("Failed to parse groovy class: [{}]", clazz.pathToClass, e);
            }
        }
    }

    private GroovyScript findScript(Path scriptPath) {
        return this.cache.computeIfAbsent(scriptPath, path -> {
            try {
                FileTime mod = Files.getLastModifiedTime(scriptPath);
                log.info("Parsing script {}", scriptPath);
                return new GroovyScript(scriptPath, mod, this.groovyShell.parse(scriptPath.toFile()).getClass().getDeclaredConstructor());
            } catch (IOException | NoSuchMethodException e) {
                log.warn("Failed to parse script: [{}]", scriptPath, e);
            }
            return GroovyScript.BROKEN;
        });
    }

    private boolean checkAndUpdateModTimeS(Path scriptPath) throws IOException {
        GroovyScript script = this.cache.get(scriptPath);
        boolean ret = false;
        if (script != null) {
            FileTime time = Files.getLastModifiedTime(scriptPath);

            if (script.isMod(time)) {
                ret = true;
                script.newScript = null;
                script.lastModTime = time;
            }
        }
        return ret;
    }


    private boolean checkAndUpdateModTimeC() throws IOException {
        boolean ret = false;
        for (GroovyClass clazz : this.groovyClasses) {
            FileTime time = Files.getLastModifiedTime(clazz.pathToClass);
            if (clazz.lastModTime == null || clazz.isMod(time)) {
                clazz.lastModTime = time; ret = true;
            }
        }
        return ret;
    }

    private void runScript(GroovyScript script, Binding binding) {
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

    private static class GroovyScript {
        private static final GroovyScript BROKEN;

        static {
            BROKEN = new GroovyScript(null, null, null);
            BROKEN.isBroken = true;
        }

        private boolean isBroken = false;
        private final Path name;
        private FileTime lastModTime;
        private Constructor<?> newScript;

        public GroovyScript(Path name, FileTime lastModTime, Constructor<?> newScript) {
            this.lastModTime = lastModTime;
            this.newScript = newScript;
            this.name = name;
        }

        private boolean isMod(FileTime newMod) {
            if (this == BROKEN) return false;
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
