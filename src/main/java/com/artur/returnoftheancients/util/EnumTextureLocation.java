package com.artur.returnoftheancients.util;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.util.ResourceLocation;

public enum EnumTextureLocation {
    BLOCKS_PATH(Referense.MODID, "textures/blocks"),
    ITEMS_PATH(Referense.MODID, "textures/items"),
    GUI_PATH(Referense.MODID, "textures/gui"),
    GUI_BUTTON_PATH(Referense.MODID, "textures/gui/button"),
    GUI_CONTAINER_PATH(Referense.MODID, "textures/gui/container"),
    GUI_GIF_PATH(Referense.MODID, "textures/gui/gif"),
    PARTICLE_PATH(Referense.MODID, "textures/particle");

    private final String modId;
    private final String path;
    private final String fullPath;

    EnumTextureLocation(String modId, String path) {
        this.fullPath = modId + ":" + path;
        this.modId = modId;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getFullPath() {
        return fullPath;
    }

    public ResourceLocation getRL(String additionalPath) {
        if (!additionalPath.startsWith("/")) additionalPath = "/" + additionalPath;
        if (!additionalPath.endsWith(".png")) additionalPath = additionalPath + ".png";
        return new ResourceLocation(modId, path + additionalPath);
    }

    public String getPathNotTextures(String additionalPath) {
        if (!additionalPath.startsWith("/")) additionalPath = "/" + additionalPath;
        return fullPath.replaceAll("textures/", "") + additionalPath;
    }
}
