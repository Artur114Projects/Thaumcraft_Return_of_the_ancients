package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.main.Main;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class TaintAncientStone extends BaseBlock{
    public TaintAncientStone(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        setCreativeTab(Main.ReturnOfTheAncientsTab);
    }
}
