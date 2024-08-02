package com.artur.returnoftheancients.misc;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;

public class CraftingRegister {
    public static void register() {
        registerRecipes("soul_binder");
        registerRecipes("soul_binder_clear");
    }

    private static void registerRecipes(String name) {
        CraftingHelper.register(new ResourceLocation(Referense.MODID, name), (IRecipeFactory) (context, json) -> CraftingHelper.getRecipe(json, context));
    }
}