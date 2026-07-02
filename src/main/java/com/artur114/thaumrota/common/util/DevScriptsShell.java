package com.artur114.thaumrota.common.util;

import com.artur114.thaumrota.main.ThaumRotA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;

public class DevScriptsShell {
    private static final Logger log = LogManager.getLogger("DevScriptsShell");
    private final IGroovyEngine engine;
    private final Path scriptsPath;

    public DevScriptsShell(Path scriptsPath) {
        this.engine = this.createEngine();
        this.scriptsPath = scriptsPath;
    }

    public DevScriptsShell loadClass(File file) {
        this.engine.loadClass(file.toPath());
        return this;
    }

    public DevScriptsShell loadClass(Path path) {
        this.engine.loadClass(path);
        return this;
    }

    public DevScriptsShell loadClass(String name) {
        this.engine.loadClass(this.scriptsPath.resolve(name));
        return this;
    }

    public synchronized void evaluate(String scriptName, Object... args) {
        this.engine.evaluate(this.scriptsPath.resolve(scriptName), args);
    }

    public synchronized void evaluate(String scriptName, String[] argsIds, Object[] args) {
        this.engine.evaluate(this.scriptsPath.resolve(scriptName), argsIds, args);
    }

    public synchronized void evaluate(Path script, Object... args) {
        this.engine.evaluate(script, args);
    }

    public synchronized void evaluate(Path script, String[] argsIds, Object[] args) {
        this.engine.evaluate(script, argsIds, args);
    }

    @SuppressWarnings("unchecked")
    private IGroovyEngine createEngine() {
        IGroovyEngine dummy = new IGroovyEngine() {
            public void loadClass(Path clazz) {}
            public void evaluate(Path script, Object... args) {}
            public void evaluate(Path script, String[] argsIds, Object[] args) {}
        };

        if (ThaumRotA.isDevEnv()) {
            try {
                Class<? extends IGroovyEngine> engine = (Class<? extends IGroovyEngine>) Class.forName("com.artur114.thaumrota.common.util.GroovyScriptsImpl");
                return engine.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                log.warn("Failed to load groovy engine, using dummy engine...");
                return dummy;
            }
        } else {
            return dummy;
        }
    }
}
