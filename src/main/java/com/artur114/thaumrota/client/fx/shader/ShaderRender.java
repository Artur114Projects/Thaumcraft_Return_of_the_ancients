package com.artur114.thaumrota.client.fx.shader;

import java.util.HashMap;
import java.util.Map;

public class ShaderRender {
    protected Map<String, Integer> textures = new HashMap<>();
    protected String depthTextureName = null;
    protected String mainTextureName = null;
    public final ShaderProgram shader;

    private ShaderRender(ShaderProgram program) {
        this.shader = program;
    }

    public ShaderRender withMainTex(String name) {
        this.mainTextureName = name; return this;
    }

    public ShaderRender withDepthTex(String name) {
        this.depthTextureName = name; return this;
    }

    public ShaderRender withMainTex() {
        return this.withMainTex("screenTexture");
    }

    public ShaderRender withDepthTex() {
        return this.withDepthTex("depthTexture");
    }

    public ShaderRender withTex(String name, int tex) {
        this.textures.put(name, tex); return this;
    }

    public static ShaderRender of(ShaderBuilder builder) {
        return new ShaderRender(builder.shader());
    }

    public static ShaderRender of(ShaderProgram shader) {
        return new ShaderRender(shader);
    }
}
