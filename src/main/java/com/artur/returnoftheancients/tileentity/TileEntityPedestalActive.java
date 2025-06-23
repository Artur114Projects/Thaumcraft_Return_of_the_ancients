package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.BlockPedestalActive;
import com.artur.returnoftheancients.client.fx.particle.ParticlePhantom;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.items.ItemPhantomTablet;
import com.artur.returnoftheancients.tileentity.interf.ITileBlockUseListener;
import com.artur.returnoftheancients.util.math.Pos3d;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TileEntityPedestalActive extends TileBase implements ITileBlockUseListener {
    private BlockPos parent = null;
    private boolean isActive = false;
    private int rotate = 0;

    public int rotate() {
        return this.rotate;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void bindParent(BlockPos parent) {
        if (this.world.getTileEntity(parent) instanceof ITileActivatedWithPedestal && parent.distanceSq(this.pos) < 20 * 20) {
            this.parent = new BlockPos(parent.getX() - this.pos.getX(), parent.getY() - this.pos.getY(), parent.getZ() - this.pos.getZ());
            this.syncTile(false);
        }
    }

    protected void onActivate() {
        if (this.world.isRemote) {
            for (int i = 0; i != 10; i++) {
                Pos3d vec = new Pos3d(0.1, 0, 0).rotateYaw(this.rotate);
                Minecraft.getMinecraft().effectRenderer.addEffect(new ParticlePhantom(world, this.pos.getX() + 6.0F / 16.0F + (world.rand.nextFloat() * 4.0F / 16.0F), this.pos.getY() + 12.0F / 16.0F, this.pos.getZ() + 6.0F / 16.0F + (world.rand.nextFloat() * 4.0F / 16.0F), vec, -1));
            }
            ((WorldClient) this.world).playSound(this.pos, InitSounds.PEDESTAL_ACTIVATED.SOUND, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
        }

        if (this.parent != null) {
            TileEntity tile = this.world.getTileEntity(this.pos.add(this.parent));

            if (tile instanceof ITileActivatedWithPedestal) {
                ((ITileActivatedWithPedestal) tile).activate(this);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        Item item = stack.getItem();
        if (item instanceof ItemPhantomTablet) {
            if (this.isActive || playerIn.getCooldownTracker().hasCooldown(item)) {
                return false;
            }
            playerIn.getCooldownTracker().setCooldown(item, 20);
            playerIn.setActiveHand(hand);
            this.isActive = true;
            this.onActivate();
            return true;
        } else if (!this.isActive){
            playerIn.sendStatusMessage(new TextComponentTranslation("returnoftheancients.pedestal_active.need_tablet"), true);
        }

        return !this.isActive;
    }

    @Override
    public void onLoad() {
        IBlockState state = this.world.getBlockState(this.pos);

        switch (state.getValue(BlockPedestalActive.DIRECTION)) {
            case EAST:
                this.rotate = 0;
                break;
            case WEST:
                this.rotate = 180;
                break;
            case NORTH:
                this.rotate = 270;
                break;
            case SOUTH:
                this.rotate = 90;
                break;
        }

        if (state.getValue(BlockPedestalActive.ROTATE)) {
            this.rotate += 45;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        compound.setBoolean("isActive", this.isActive);

        if (this.parent != null) {
            compound.setLong("parent", this.parent.toLong());
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.isActive = compound.getBoolean("isActive");

        if (compound.hasKey("parent")) {
            this.parent = BlockPos.fromLong(compound.getLong("parent"));
        }
    }

    @Override
    public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
        return this.writeToNBT(nbt);
    }

    @Override
    public void readSyncNBT(NBTTagCompound nbt) {
        this.readFromNBT(nbt);
    }

    public interface ITileActivatedWithPedestal {
        void activate(TileEntityPedestalActive tile);
    }
}
