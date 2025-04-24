package com.artur.returnoftheancients.client.fx.shader;

import java.util.concurrent.Callable;

public class ShaderBuilder {
    private final Callable<ShaderProgram> builder;
    private ShaderProgram shaderProgram = null;

    public ShaderBuilder(Callable<ShaderProgram> builder) {
        this.builder = builder;

        InitShaders.SHADER_PROGRAMS.add(this);
    }

    protected void build() {
        try {
            this.shaderProgram = builder.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ShaderProgram shader() {
        return shaderProgram;
    }
}
