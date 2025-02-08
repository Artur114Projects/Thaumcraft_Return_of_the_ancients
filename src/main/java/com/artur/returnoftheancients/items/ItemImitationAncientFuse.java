package com.artur.returnoftheancients.items;


import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;

public class ItemImitationAncientFuse extends BaseItem {
    public ItemImitationAncientFuse(String name) {
        super(name);

        this.setMaxStackSize(1);
        this.setTRACreativeTab();
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.RARE;
    }
}
