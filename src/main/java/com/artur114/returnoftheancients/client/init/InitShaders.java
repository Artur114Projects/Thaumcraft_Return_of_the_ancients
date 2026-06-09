package com.artur114.returnoftheancients.client.init;

import com.artur114.bananalib.mc.register.ann.RegistryContainer;
import com.artur114.returnoftheancients.client.fx.shader.ShaderBuilder;
import com.artur114.returnoftheancients.client.fx.shader.ShaderProgram;
import com.artur114.returnoftheancients.common.util.EnumAssetLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@RegistryContainer
@SideOnly(Side.CLIENT)
public class InitShaders {
    public static final ShaderBuilder TEST_SHADER = new ShaderBuilder(() -> new ShaderProgram().addFragment(EnumAssetLocation.SHADERS.getPath("test_shader.frag")).addVertex(EnumAssetLocation.SHADERS.getPath("texture_color.vert")).compile());
    public static final ShaderBuilder BLACK_WHITE = new ShaderBuilder(() -> new ShaderProgram().addFragment(EnumAssetLocation.SHADERS.getPath("black-white.frag")).addVertex(EnumAssetLocation.SHADERS.getPath("texture_color.vert")).compile());
    public static final ShaderBuilder HEAT_PAR = new ShaderBuilder(() -> new ShaderProgram().addFragment(EnumAssetLocation.SHADERS.getPath("heat_par.frag")).addVertex(EnumAssetLocation.SHADERS.getPath("texture_color.vert")).compile());
    public static final ShaderBuilder HEAT = new ShaderBuilder(() -> new ShaderProgram().addFragment(EnumAssetLocation.SHADERS.getPath("heat.frag")).addVertex(EnumAssetLocation.SHADERS.getPath("texture_color.vert")).compile());
}
