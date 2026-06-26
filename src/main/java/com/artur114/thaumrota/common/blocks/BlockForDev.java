package com.artur114.thaumrota.common.blocks;

import com.artur114.bananalib.mc.base.BItemBlockBase;
import com.artur114.bananalib.mc.base.MaterialArray;
import com.artur114.bananalib.mc.registry.interf.IOptionalRegister;
import com.artur114.thaumrota.client.render.tile.TileEntityForDevRenderer;
import com.artur114.thaumrota.common.tileentity.TileEntityForDev;
import com.artur114.thaumrota.common.util.DevScriptsShell;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockForDev extends BaseBlockTileEntity<TileEntityForDev> implements IOptionalRegister {
    public BlockForDev(String name, Material material, MapColor mapColor, float hardness, float resistance, SoundType soundType) {
        super(name, material, mapColor, hardness, resistance, soundType);

        this.setTileRender(new TileEntityForDevRenderer());
        this.setNotFillAndOpaqueCube();
        this.setForCreative();
    }

    public BlockForDev(String name, Material material, float hardness, float resistance, SoundType soundType) {
        this(name, material, material.getMaterialMapColor(), hardness, resistance, soundType);
    }

    public BlockForDev(String name, MaterialArray mat) {
        this(name, mat.material(), mat.hardness(), mat.resistance(), mat.soundType());
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    protected @Nullable Item createItemBlock() {
        return ((BItemBlockBase) Objects.requireNonNull(super.createItemBlock())).setRarity(EnumRarity.EPIC);
    }

    @Override
    public boolean shouldRegister(Class<?> registerSource) {
        if (!DevScriptsShell.isDev()) {
            return false;
        }
        return super.shouldRegister(registerSource);
    }

    @Override
    public @NotNull Class<TileEntityForDev> tileClass() {
        return TileEntityForDev.class;
    }

    @Override
    public @Nullable TileEntityForDev createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityForDev();
    }
}
