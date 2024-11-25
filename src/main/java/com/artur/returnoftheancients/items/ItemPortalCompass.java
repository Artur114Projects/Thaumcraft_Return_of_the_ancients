package com.artur.returnoftheancients.items;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.WorldDataFields;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ItemPortalCompass extends BaseItem {
    public ItemPortalCompass(String name)
    {
        super(name);
        this.setMaxStackSize(1);
        this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            double rotation;
            @SideOnly(Side.CLIENT)
            double rota;
            @SideOnly(Side.CLIENT)
            long lastUpdateTick;
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null && !stack.isOnItemFrame())
                {
                    return 0.0F;
                }
                else
                {
                    boolean flag = entityIn != null;
                    Entity entity = (Entity)(flag ? entityIn : stack.getItemFrame());

                    if (worldIn == null)
                    {
                        worldIn = entity.world;
                    }

                    double d0;

                    if (entityIn instanceof EntityPlayer)
                    {
                        EntityPlayer player = (EntityPlayer) entityIn;
                        if (HandlerR.isHasItem(player, InitItems.COMPASS) && player.dimension == WorldDataFields.portalDimension && !HandlerR.isWithinRadius(player.posX, player.posZ, WorldDataFields.portalX, WorldDataFields.portalZ, 8)) {
                            double d1 = flag ? (double) entity.rotationYaw : this.getFrameRotation((EntityItemFrame) entity);
                            d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
                            double d2 = this.getPortalToAngle(entity) / (Math.PI * 2D);
                            d0 = 0.5D - (d1 - 0.25D - d2);
                        } else {
                            d0 = Math.random();
                        }
                    }
                    else
                    {
                        d0 = Math.random();
                    }

                    if (flag)
                    {
                        d0 = this.wobble(worldIn, d0);
                    }

                    return MathHelper.positiveModulo((float)d0, 1.0F);
                }
            }
            @SideOnly(Side.CLIENT)
            private double wobble(World worldIn, double p_185093_2_)
            {
                if (worldIn.getTotalWorldTime() != this.lastUpdateTick)
                {
                    this.lastUpdateTick = worldIn.getTotalWorldTime();
                    double d0 = p_185093_2_ - this.rotation;
                    d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
                    this.rota += d0 * 0.1D;
                    this.rota *= 0.8D;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
                }

                return this.rotation;
            }
            @SideOnly(Side.CLIENT)
            private double getFrameRotation(EntityItemFrame p_185094_1_)
            {
                return MathHelper.wrapDegrees(180 + p_185094_1_.facingDirection.getHorizontalIndex() * 90);
            }
            @SideOnly(Side.CLIENT)
            private double getPortalToAngle(Entity entity)
            {
                BlockPos blockpos = WorldDataFields.blockPosToCompass;
                return Math.atan2((double) blockpos.getZ() - entity.posZ, (double) blockpos.getX() - entity.posX);
            }
        });
        setCreativeTab(MainR.ReturnOfTheAncientsTab);
    }

    public @NotNull EnumRarity getRarity(@NotNull ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

}
