package com.artur114.returnoftheancients.client.fx.shader;

import com.artur114.bananalib.mc.register.interf.ILoadStagePre;
import com.artur114.returnoftheancients.client.init.InitShaders;

import java.util.concurrent.Callable;

public class ShaderBuilder implements ILoadStagePre {
    private final Callable<ShaderProgram> builder;
    private ShaderProgram shaderProgram = null;

    public ShaderBuilder(Callable<ShaderProgram> builder) {
        this.builder = builder;
    }

    @Override
    public void onPreInit() {
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
