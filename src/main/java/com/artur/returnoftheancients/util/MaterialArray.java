package com.artur.returnoftheancients.util;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class MaterialArray {
    public static final MaterialArray ANCIENT_STONE_ARRAY = new MaterialArray().setHardness(2.0F).setResistance(10.0F);

    private SoundType soundType = SoundType.STONE;
    private Material material = Material.ROCK;
    private float resistance;
    private float hardness;

    public MaterialArray setHardness(float hardness) {
        this.hardness = hardness;
        return this;
    }

    public MaterialArray setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public MaterialArray setResistance(float resistance) {
        this.resistance = resistance;
        return this;
    }

    public MaterialArray setSoundType(SoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    public SoundType soundType() {
        return this.soundType;
    }

    public Material material() {
        return this.material;
    }

    public float resistance() {
        return this.resistance;
    }

    public float hardness() {
        return this.hardness;
    }
}
