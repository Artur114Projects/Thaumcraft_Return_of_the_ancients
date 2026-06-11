package com.artur114.thaumrota.common.items;


import com.artur114.bananalib.mc.base.BItemBase;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;

public class ItemImitationAncientFuse extends BItemBase {
    public ItemImitationAncientFuse(String name) {
        super(name);

        this.setMaxStackSize(1);
        this.setCreativeTab(ThaumRotA.CREATIVE_TAB);
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.RARE;
    }
}
