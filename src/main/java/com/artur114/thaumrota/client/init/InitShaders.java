package com.artur114.thaumrota.client.init;

import com.artur114.bananalib.mc.registry.ann.RegistryContainer;
import com.artur114.thaumrota.client.fx.shader.ShaderBuilder;
import com.artur114.thaumrota.client.fx.shader.ShaderProgram;
import com.artur114.thaumrota.common.util.EnumAsset;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@RegistryContainer
@SideOnly(Side.CLIENT)
public class InitShaders {
    public static final ShaderBuilder TEST_SHADER = new ShaderBuilder(() -> new ShaderProgram().addFragment(EnumAsset.SHADERS.path("test_shader.frag")).addVertex(EnumAsset.SHADERS.path("texture_color.vert")).compile());
    public static final ShaderBuilder BLACK_WHITE = new ShaderBuilder(() -> new ShaderProgram().addFragment(EnumAsset.SHADERS.path("black-white.frag")).addVertex(EnumAsset.SHADERS.path("texture_color.vert")).compile());
    public static final ShaderBuilder HEAT_PAR = new ShaderBuilder(() -> new ShaderProgram().addFragment(EnumAsset.SHADERS.path("heat_par.frag")).addVertex(EnumAsset.SHADERS.path("texture_color.vert")).compile());
    public static final ShaderBuilder HEAT = new ShaderBuilder(() -> new ShaderProgram().addFragment(EnumAsset.SHADERS.path("heat.frag")).addVertex(EnumAsset.SHADERS.path("texture_color.vert")).compile());
}
