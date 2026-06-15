package com.artur114.thaumrota.common.blocks;

import com.artur114.bananalib.mc.base.BBlockTileBase;
import com.artur114.bananalib.mc.base.MaterialArray;
import com.artur114.thaumrota.client.render.item.TileEntityItemStackRendererTRA;
import com.artur114.thaumrota.client.render.item.IItemStackRenderer;
import com.artur114.thaumrota.client.render.tile.TileEntityForDevRenderer;
import com.artur114.thaumrota.common.tileentity.TileEntityForDev;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseBlockTileEntity<T extends TileEntity> extends BBlockTileBase<T> {
    public BaseBlockTileEntity(String name, Material material, MapColor mapColor, float hardness, float resistance, SoundType soundType) {
        super(name, material, mapColor, hardness, resistance, soundType);
    }

    public BaseBlockTileEntity(String name, Material material, float hardness, float resistance, SoundType soundType) {
        this(name, material, material.getMaterialMapColor(), hardness, resistance, soundType);
    }

    public BaseBlockTileEntity(String name, MaterialArray mat) {
        this(name, mat.material(), mat.hardness(), mat.resistance(), mat.soundType());
    }

    @Override
    public void setTileRender(TileEntitySpecialRenderer<T> render) {
        super.setTileRender(render);

        if (this.item != null && render instanceof IItemStackRenderer) {
            this.item.setTileEntityItemStackRenderer(TileEntityItemStackRendererTRA.INSTANCE);
            TileEntityItemStackRendererTRA.INSTANCE.register(this.item, ((IItemStackRenderer) render));
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (this.isForCreative) {
            tooltip.add(TextFormatting.RED + I18n.format("thaumrota.for_creative_only"));
        }
        super.addInformation(stack, player, tooltip, advanced);
    }
}