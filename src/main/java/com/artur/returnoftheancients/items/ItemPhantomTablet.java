package com.artur.returnoftheancients.items;

import com.artur.returnoftheancients.client.fx.particle.ParticlePhantom;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.util.math.Pos3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.SoundsTC;

import java.util.List;

public class ItemPhantomTablet extends BaseItem {

    public ItemPhantomTablet(String name) {
        super(name);

        this.setTRACreativeTab();
        this.setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.provider.getDimension() != InitDimensions.ancient_world_dim_id && (!(entityIn instanceof EntityPlayer) || !((EntityPlayer) entityIn).isCreative())) {
            entityIn.replaceItemInInventory(itemSlot, ItemStack.EMPTY);

            if (worldIn.isRemote) {
                for (int i = 0; i != 80; i++) {
                    Pos3d vec = new Pos3d((worldIn.rand.nextFloat() - 0.5F) * 0.2, 0, (worldIn.rand.nextFloat() - 0.5F) * 0.2);
                    Minecraft.getMinecraft().effectRenderer.addEffect(new ParticlePhantom(worldIn, entityIn.posX + ((worldIn.rand.nextFloat() * 2.0F) - 1.0F), entityIn.posY + (worldIn.rand.nextFloat() * 0.5), entityIn.posZ + ((worldIn.rand.nextFloat() * 2.0F) - 1.0F), vec, 20));
                    entityIn.playSound(SoundsTC.crabclaw, 0.01F, 1.0F);
                }
            } else if (entityIn instanceof EntityLivingBase) {
                ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20, 0));
            }
        }
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem.dimension != InitDimensions.ancient_world_dim_id) {
            entityItem.setDead();

            if (entityItem.world.isRemote) {
                for (int i = 0; i != 80; i++) {
                    Pos3d vec = new Pos3d((entityItem.world.rand.nextFloat() - 0.5F) * 0.2, 0, (entityItem.world.rand.nextFloat() - 0.5F) * 0.2);
                    Minecraft.getMinecraft().effectRenderer.addEffect(new ParticlePhantom(entityItem.world, entityItem.posX + ((entityItem.world.rand.nextFloat() * 1.0F) - 0.5F), entityItem.posY + (entityItem.world.rand.nextFloat() * 0.5), entityItem.posZ + ((entityItem.world.rand.nextFloat() * 1.0F) - 0.5F), vec, 20));
                }
            } else {
                entityItem.world.playSound(null, entityItem.getPosition(), SoundsTC.crabclaw, SoundCategory.AMBIENT, 1, 1);
            }

            return true;
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.phantom_tablet.info.1"));
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.isCreative()) {
            tooltip.add(TextFormatting.RED + I18n.format("item.phantom_tablet.info.2"));
        }
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
