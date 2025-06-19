package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.energy.bases.block.BlockEnergyBase;
import com.artur.returnoftheancients.tileentity.TileEnergySource;
import com.artur.returnoftheancients.init.InitItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockEnergySource extends BlockEnergyBase<TileEnergySource> {
    public BlockEnergySource(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        InitItems.ITEMS.remove(item);
        item = new ItemBlockEnergySource(this).setRegistryName(this.getRegistryName());
        InitItems.ITEMS.add(item);

        this.setForCreative().setTRACreativeTab();
    }

    @Override
    public Class<TileEnergySource> getTileEntityClass() {
        return TileEnergySource.class;
    }

    @Override
    public @Nullable TileEnergySource createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEnergySource();
    }

    private static class ItemBlockEnergySource extends ItemBlock {

        public ItemBlockEnergySource(Block block) {
            super(block);
        }

        @Override
        public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
            return EnumRarity.EPIC;
        }
    }
}
