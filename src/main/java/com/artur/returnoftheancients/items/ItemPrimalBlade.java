package com.artur.returnoftheancients.items;

import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.util.interfaces.IHasModel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemPrimalBlade extends ItemAxe implements IHasModel {

    public ItemPrimalBlade(String name, Item.ToolMaterial e) {
        super(e, TRAConfigs.Any.primalBladeDamage - 1, (float) TRAConfigs.Any.primalBladeSpeed);
        setRegistryName(name);
        setUnlocalizedName(name);
        setMaxStackSize(1);
        setCreativeTab(MainR.RETURN_OF_ANCIENTS_TAB);
        setNoRepair();

        InitItems.ITEMS_REGISTER_BUSS.add(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (GuiScreen.isShiftKeyDown()) {
            tooltip.add(TextFormatting.WHITE + I18n.format("item.itemprimalblade.info.1"));
        } else {
            tooltip.add(TextFormatting.GRAY + I18n.format("returnoftheancients.info.p1") + " " + TextFormatting.WHITE + "[Shift]" + TextFormatting.GRAY + " " + I18n.format("returnoftheancients.info.p2"));
        }
    }

    @Override
    public @NotNull EnumRarity getRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public void registerModels() {
        MainR.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
