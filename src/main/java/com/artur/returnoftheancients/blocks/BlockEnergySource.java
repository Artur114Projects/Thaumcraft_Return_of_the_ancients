package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.energy.bases.block.BlockEnergyBase;
import com.artur.returnoftheancients.tileentity.TileEnergySource;
import com.artur.returnoftheancients.init.InitItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockEnergySource extends BlockEnergyBase<TileEnergySource> {
    public BlockEnergySource(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);

        this.setForCreative().setTRACreativeTab();
    }

    @Override
    public Class<TileEnergySource> tileEntityClass() {
        return TileEnergySource.class;
    }

    @Override
    public @Nullable TileEnergySource createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEnergySource();
    }

    @Override
    protected Item createItemBlock() {
        return new ItemBlockEnergySource(this).setRegistryName(Objects.requireNonNull(this.getRegistryName()));
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
