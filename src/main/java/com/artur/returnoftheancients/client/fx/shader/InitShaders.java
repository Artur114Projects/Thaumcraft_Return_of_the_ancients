package com.artur.returnoftheancients.client.fx.shader;

import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class InitShaders {
    protected static final List<ShaderBuilder> SHADER_PROGRAMS = new ArrayList<>();

    public static final ShaderBuilder BLACK_WHITE = new ShaderBuilder(() -> new ShaderProgram().addFragment(EnumAssetLocation.SHADERS.getPath("black-white.frag")).addVertex(EnumAssetLocation.SHADERS.getPath("texture_color.vert")).compile());
    public static final ShaderBuilder HEAT = new ShaderBuilder(() -> new ShaderProgram().addFragment(EnumAssetLocation.SHADERS.getPath("heat.frag")).addVertex(EnumAssetLocation.SHADERS.getPath("texture_color.vert")).compile());

    public static void init() {
        for (ShaderBuilder builder : SHADER_PROGRAMS) {
            builder.build();
        }
    }
}
