package com.artur114.thaumrota.common.util;

import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.util.ResourceLocation;
import thaumcraft.Thaumcraft;

public enum EnumAsset {
    TEXTURES_BLOCKS(ThaumRotA.MODID, "textures/blocks"),
    TEXTURES_ITEMS(ThaumRotA.MODID, "textures/items"),
    TEXTURES_GUI(ThaumRotA.MODID, "textures/gui"),
    TEXTURES_GUI_BUTTON(ThaumRotA.MODID, "textures/gui/button"),
    TEXTURES_GUI_CONTAINER(ThaumRotA.MODID, "textures/gui/container"),
    TEXTURES_PARTICLE(ThaumRotA.MODID, "textures/particle"),
    TEXTURES_TC_MODELS(Thaumcraft.MODID, "textures/models"),
    TEXTURES_MISC(ThaumRotA.MODID, "textures/misc"),
    SHADERS(ThaumRotA.MODID, "shaders");

    private final String modId;
    private final String path;
    private final String fullPath;

    EnumAsset(String modId, String path) {
        this.fullPath = modId + ":" + path;
        this.modId = modId;
        this.path = path;
    }

    /**
     * Склеивает {@code this.path} с {@code additionalPath}. <br>
     * Если {@code additionalPath} не начинается с "/" добавляет в начало "/"
     * @param additionalPath дополнительный путь\имя файла
     * @return локальный путь без учета modId
     */
    public String path(String additionalPath) {
        if (!additionalPath.startsWith("/")) additionalPath = "/" + additionalPath;
        return path + additionalPath;
    }

    /**
     * Склеивает {@code this.path} с {@code additionalPath}. <br>
     * Если {@code additionalPath} не начинается с "/" добавляет в начало "/"
     * @param additionalPath дополнительный путь\имя файла
     * @return полный путь от {@code /assets/modId}  до {@code additionalPath}
     */
    public String fullPath(String additionalPath) {
        if (!additionalPath.startsWith("/")) additionalPath = "/" + additionalPath;
        return "/assets/" + modId + "/" + path + additionalPath;
    }

    /**
     * Возвращает ResourceLocation с {@code domain = modId}, {@code path = this.path + addP}. <br>
     * Если {@code additionalPath} не начинается с "/" добавляет в начало "/"
     * Если {@code additionalPath} не заканчивается на ".png" добавляет в конец ".png"
     * @param additionalPath дополнительный путь\имя файла
     * @return новый ResourceLocation с {@code domain = modId}, {@code path = this.path + addP ?: ".png"}
     */
    public ResourceLocation png(String additionalPath) {
        if (!additionalPath.startsWith("/")) additionalPath = "/" + additionalPath;
        if (!additionalPath.endsWith(".png")) additionalPath = additionalPath + ".png";
        return new ResourceLocation(modId, path + additionalPath);
    }

    public String texture(String additionalPath) {
        if (!additionalPath.startsWith("/")) additionalPath = "/" + additionalPath;
        return this.fullPath.replaceAll("textures/", "") + additionalPath;
    }
}
