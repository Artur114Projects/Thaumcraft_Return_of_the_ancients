package com.artur114.thaumrota.common.util;

import java.nio.file.Path;

public interface IGroovyEngine {
    void loadClass(Path clazz);
    void evaluate(Path script, Object... args);
    void evaluate(Path script, String[] argsIds, Object[] args);
}
