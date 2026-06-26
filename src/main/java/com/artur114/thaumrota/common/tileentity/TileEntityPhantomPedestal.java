package com.artur114.thaumrota.common.tileentity;

import com.artur114.bananalib.math.m3d.vec.Vec3DM;
import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.base.tileabs.ITileBlockUseListener;
import com.artur114.bananalib.mc.base.tileabs.ITileMultiBBProvider;
import com.artur114.thaumrota.client.fx.particle.ParticlePhantom;
import com.artur114.thaumrota.common.init.InitItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TileEntityPhantomPedestal extends TileBase implements ITileMultiBBProvider, ITileBlockUseListener {
    private final List<AxisAlignedBB> boxes = Collections.unmodifiableList(Arrays.asList(
        BananaMC.createAABBFromPixels(0, 0, 0, 16, 4, 16).grow(0.005),
        BananaMC.createAABBFromPixels(4, 4, 4, 12, 12, 12).grow(0.005),
        BananaMC.createAABBFromPixels(2, 12, 2, 14, 16, 14).grow(0.005)
    ));

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand == EnumHand.MAIN_HAND && playerIn.getHeldItem(hand).isEmpty()) {
            if (this.world.isRemote) {
                if (!BananaMC.inventoryContains(playerIn.inventory, InitItems.PHANTOM_TABLET)) {
                    Random rand = new Random();
                    this.spawnParticles();
                    playerIn.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, (rand.nextFloat() - rand.nextFloat()) * 1.4F + 2.0F);
                    return true;
                }
            } else {
                if (!BananaMC.inventoryContains(playerIn.inventory, InitItems.PHANTOM_TABLET)) {
                    playerIn.setHeldItem(hand, new ItemStack(InitItems.PHANTOM_TABLET));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<AxisAlignedBB> boundingBoxes() {
        return this.boxes;
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles() {
        Random rand = new Random();
        Vec3DM pos = Vec3DM.obtain();
        Vec3DM move = Vec3DM.obtain();

        for (int i = 0; i != 16; i++) {
            pos.set(this.pos.getX() + 0.5, this.pos.getY() + 1, this.pos.getZ() + 0.5);
            move.set(1, 0, 0).rotateY(360 * (i / 16.0F) + rand.nextDouble() * 45);
            pos.add(move.pushPos().scale(0.2 + rand.nextDouble() * 0.05));
            move.popPos().scale((0.05 + rand.nextDouble() * 0.05));
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticlePhantom(world, pos.x(), pos.y() - (0.2 * rand.nextDouble()), pos.z(), move, rand.nextInt(10) + 10));
        }

        Vec3DM.release(pos);
        Vec3DM.release(move);
    }
}
