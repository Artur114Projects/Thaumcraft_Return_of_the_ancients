package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils;


import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures.ILightTemplate;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures.IStructure;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IStructureType {
    @SideOnly(Side.CLIENT)
    ILightTemplate light();
    IStructure create(EnumRotate rotate, StrPos pos);
    String stringId(EnumRotate rotate);
}
