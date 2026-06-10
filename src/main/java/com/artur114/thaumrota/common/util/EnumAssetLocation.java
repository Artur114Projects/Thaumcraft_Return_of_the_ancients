package com.artur114.thaumrota.common.util;

import com.artur114.thaumrota.main.ThaumicRotA;
import net.minecraft.util.ResourceLocation;
import thaumcraft.Thaumcraft;

public enum EnumAssetLocation {
    TEXTURES_BLOCKS(ThaumicRotA.MODID, "textures/blocks"),
    TEXTURES_ITEMS(ThaumicRotA.MODID, "textures/items"),
    TEXTURES_GUI(ThaumicRotA.MODID, "textures/gui"),
    TEXTURES_GUI_BUTTON(ThaumicRotA.MODID, "textures/gui/button"),
    TEXTURES_GUI_CONTAINER(ThaumicRotA.MODID, "textures/gui/container"),
    TEXTURES_GUI_GIF(ThaumicRotA.MODID, "textures/gui/gif"),
    TEXTURES_PARTICLE(ThaumicRotA.MODID, "textures/particle"),
    TEXTURES_TC_MODELS(Thaumcraft.MODID, "textures/models"),
    TEXTURES_MISC(ThaumicRotA.MODID, "textures/misc"),
    SHADERS(ThaumicRotA.MODID, "shaders");

    private final String modId;
    private final String path;
    private final String fullPath;

    EnumAssetLocation(String modId, String path) {
        this.fullPath = modId + ":" + path;
        this.modId = modId;
        this.path = path;
    }

    public String getPath(String additionalPath) {
        if (!additionalPath.startsWith("/")) additionalPath = "/" + additionalPath;
        return path + additionalPath;
    }

    public String getFullPath(String additionalPath) {
        if (!additionalPath.startsWith("/")) additionalPath = "/" + additionalPath;
        return "/assets/" + modId + "/" + path + additionalPath;
    }

    public ResourceLocation getPngRL(String additionalPath) {
        if (!additionalPath.startsWith("/")) additionalPath = "/" + additionalPath;
        if (!additionalPath.endsWith(".png")) additionalPath = additionalPath + ".png";
        return new ResourceLocation(modId, path + additionalPath);
    }

    public String getPathNotTextures(String additionalPath) {
        if (!additionalPath.startsWith("/")) additionalPath = "/" + additionalPath;
        return fullPath.replaceAll("textures/", "") + additionalPath;
    }
}
