package com.artur114.thaumrota.common.misc;

import com.artur114.thaumrota.main.ThaumicRotA;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;

public class CraftingRegister {
    public static void register() {
        registerRecipes("soul_binder_clear");
    }

    private static void registerRecipes(String name) {
        CraftingHelper.register(new ResourceLocation(ThaumicRotA.MODID, name), (IRecipeFactory) (context, json) -> CraftingHelper.getRecipe(json, context));
    }
}