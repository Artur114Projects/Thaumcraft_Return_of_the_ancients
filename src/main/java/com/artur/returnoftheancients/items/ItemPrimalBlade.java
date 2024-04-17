package com.artur.returnoftheancients.items;

import com.artur.returnoftheancients.ancientworldutilities.Configs;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.main.Main;
import com.artur.returnoftheancients.utils.interfaces.IHasModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.common.items.tools.ItemElementalSword;

import java.util.List;

public class ItemPrimalBlade extends ItemAxe implements IHasModel {

    public ItemPrimalBlade(String name, Item.ToolMaterial e) {
        super(e, Configs.Any.primalBladeDamage - 1, (float) Configs.Any.primalBladeSpeed);
        setRegistryName(name);
        setUnlocalizedName(name);
        setMaxStackSize(1);
        setCreativeTab(Main.ReturnOfTheAncientsTab);
        setNoRepair();

        InitItems.ITEMS.add(this);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.BLACK + I18n.translateToLocal("item.itemprimalblade.text"));
    }

    public @NotNull EnumRarity getRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
