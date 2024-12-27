package com.artur.returnoftheancients.items;

import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.utils.interfaces.IHasModel;
import com.artur.returnoftheancients.utils.math.Pos3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumParticleTypes;
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
        setCreativeTab(MainR.ReturnOfTheAncientsTab);
        setNoRepair();

        InitItems.ITEMS.add(this);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
//        if (worldIn.isRemote && isSelected && entityIn instanceof EntityPlayer) {
//            renderParticles((EntityPlayer) entityIn);
//        }
    }

    @SideOnly(Side.CLIENT)
    public void renderParticles(EntityPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        Pos3d playerPos = new Pos3d(player);
        Pos3d flameVec;
        double flameXCoord = 0;
        double flameYCoord = 1.5;
        double flameZCoord = 0;
        Pos3d flameMotion = new Pos3d(player.motionX, player.onGround ? 0 : player.motionY, player.motionZ);
        if (mc.gameSettings.thirdPersonView == 0) {
            flameVec = new Pos3d(1, 1, 1).rotateYaw(player.rotationYaw + 80).add(flameXCoord, (flameYCoord - 1.0) - Math.sin(Math.toRadians(player.rotationPitch)), flameZCoord);
        } else {
            flameXCoord -= 0.45F;
            flameZCoord += 0.15F;
            if (player.isSneaking()) {
                flameYCoord -= 0.55F;
                flameZCoord -= 0.15F;
            }
            flameYCoord -= 0.5F;
            flameZCoord += 1.05F;
            flameVec = new Pos3d(flameXCoord, flameYCoord, flameZCoord).rotateYaw(player.renderYawOffset);
        }
        Pos3d mergedVec = playerPos.add(flameVec);
        player.world.spawnParticle(EnumParticleTypes.FLAME, mergedVec.x, mergedVec.y, mergedVec.z, flameMotion.x, flameMotion.y + 0.1, flameMotion.z);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
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
