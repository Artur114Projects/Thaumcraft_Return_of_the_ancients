package com.artur.returnoftheancients.util;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.util.ResourceLocation;
import thaumcraft.Thaumcraft;

public enum EnumAssetLocation {
    TEXTURES_BLOCKS(Referense.MODID, "textures/blocks"),
    TEXTURES_ITEMS(Referense.MODID, "textures/items"),
    TEXTURES_GUI(Referense.MODID, "textures/gui"),
    TEXTURES_GUI_BUTTON(Referense.MODID, "textures/gui/button"),
    TEXTURES_GUI_CONTAINER(Referense.MODID, "textures/gui/container"),
    TEXTURES_GUI_GIF(Referense.MODID, "textures/gui/gif"),
    TEXTURES_PARTICLE(Referense.MODID, "textures/particle"),
    TEXTURES_TC_MODELS(Thaumcraft.MODID, "textures/models"),
    TEXTURES_MISC(Referense.MODID, "textures/misc"),
    SHADERS(Referense.MODID, "shaders");

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
