package com.artur114.thaumrota.common.blocks;

import com.artur114.bananalib.mc.base.BBlockTileBase;
import com.artur114.bananalib.mc.base.MaterialArray;
import com.artur114.bananalib.mc.registry.data.TESRRegData;
import com.artur114.bananalib.mc.registry.interf.IHasTileSRRegister;
import com.artur114.thaumrota.client.render.item.TileEntityItemStackRendererTRA;
import com.artur114.thaumrota.client.render.item.IItemStackRenderer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseBlockTile<T extends TileEntity> extends BBlockTileBase<T> implements IHasTileSRRegister {
    public BaseBlockTile(String name, Material material, MapColor mapColor, float hardness, float resistance, SoundType soundType) {
        super(name, material, mapColor, hardness, resistance, soundType);
    }

    public BaseBlockTile(String name, Material material, float hardness, float resistance, SoundType soundType) {
        this(name, material, material.getMaterialMapColor(), hardness, resistance, soundType);
    }

    public BaseBlockTile(String name, MaterialArray mat) {
        this(name, mat.material(), mat.hardness(), mat.resistance(), mat.soundType());
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World player, @NotNull List<String> tooltip, @NotNull ITooltipFlag advanced) {
        if (this.isForCreative) {
            tooltip.add(TextFormatting.RED + I18n.format("thaumrota.for_creative_only"));
        }
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void registerTSR(TESRRegData data) {
        if (this.item != null && data.specialRenderer() instanceof IItemStackRenderer) {
            this.item.setTileEntityItemStackRenderer(TileEntityItemStackRendererTRA.INSTANCE);
            TileEntityItemStackRendererTRA.INSTANCE.register(this.item, ((IItemStackRenderer) data.specialRenderer()));
        }

        ClientRegistry.bindTileEntitySpecialRenderer((Class) data.tileEntityClass(), (TileEntitySpecialRenderer) data.specialRenderer());
    }
}