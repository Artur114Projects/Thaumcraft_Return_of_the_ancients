package com.artur114.thaumrota.common.blocks;


import com.artur114.bananalib.mc.base.MaterialArray;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class MaterialArrays {
    public static final MaterialArray ANCIENT_STONE_ARRAY = MaterialArray.builder().setHardness(2.0F).setResistance(10.0F).build();
    public static final Item.ToolMaterial TOOL_MAT_PRIMAL = EnumHelper.addToolMaterial("toolmat_primal", 0, 0, 0, 0, 10);
}
