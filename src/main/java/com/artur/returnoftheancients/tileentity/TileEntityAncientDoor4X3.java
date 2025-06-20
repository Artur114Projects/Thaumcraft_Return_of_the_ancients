package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.BlockDummy;
import com.artur.returnoftheancients.handlers.RenderHandler;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.tileentity.interf.ITileDoor;
import com.artur.returnoftheancients.tileentity.interf.ITileMultiBlock;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.fx.FXDispatcher;

import java.util.HashMap;
import java.util.Map;

public class TileEntityAncientDoor4X3 extends TileEntityDoorBase {
    private static final Map<EnumDoorState, Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>>> axisAlignedBBMap;

    private int startOpenTimer = 0;

    public TileEntityAncientDoor4X3() {
        super(20, 4, 3, InitBlocks.DUMMY_ANCIENT_STONE);
    }

    public void onBlockBreak() {
        this.breakAll();
    }

    @Override
    public void update() {
        super.update();

        if (this.startOpenTimer > 0) {

            if (this.world.isRemote) {
                if (this.startOpenTimer == 8) {
                    ((WorldClient) this.world).playSound(this.pos, InitSounds.PNEUMATIC_PUFF.SOUND, SoundCategory.BLOCKS, 0.25F, 1, false);
                }

                if (this.startOpenTimer == 4) {
                    ((WorldClient) this.world).playSound(this.pos, InitSounds.DOOR_OPEN_2.SOUND, SoundCategory.BLOCKS, 0.25F, 1, false);
                }

                if (this.axis == EnumFacing.Axis.Z) {
                    FXDispatcher.INSTANCE.drawVentParticles(pos.getX() + 8.0F / 16.0F, pos.getY() + 2 + (11.0F / 16.0F), pos.getZ() + 2, 0.001, -0.001, 0.0, 0x999999, 1.0F);
                    FXDispatcher.INSTANCE.drawVentParticles(pos.getX() + 6.0F / 16.0F, pos.getY() + 2 + (11.0F / 16.0F), pos.getZ() + 2, -0.001, -0.001, 0.0, 0x999999, 1.0F);
                } else {
                    FXDispatcher.INSTANCE.drawVentParticles(pos.getX() + 2, pos.getY() + 2 + (11.0F / 16.0F), pos.getZ() + 8.0F / 16.0F, 0.0, -0.001, 0.001, 0x999999, 1.0F);
                    FXDispatcher.INSTANCE.drawVentParticles(pos.getX() + 2, pos.getY() + 2 + (11.0F / 16.0F), pos.getZ() + 6.0F / 16.0F, 0.0, -0.001, -0.001, 0x999999, 1.0F);
                }
            }

            this.startOpenTimer--;

            if (this.startOpenTimer == 0) {
                super.open();
            }
        }
    }

    @Override
    public void open() {
        if (this.currentState == EnumDoorState.CLOSE) {
            this.startOpenTimer = 8;
        }
    }

    @Override
    protected Map<EnumDoorState, Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>>> staticAxisAlignedBBMap() {
        return axisAlignedBBMap;
    }

    static {
        Map<EnumDummyType, AxisAlignedBB> open = new HashMap<>();

        open.put(EnumDummyType.DOOR, Block.NULL_AABB);
        open.put(EnumDummyType.FLOOR, Block.NULL_AABB);

        open.put(EnumDummyType.CORNER_UP_NEAR, new AxisAlignedBB(5.0F / 16.0F, 2.0F / 16.0F, 0, 11.0F / 16.0F, 1, 14.0F / 16.0F));
        open.put(EnumDummyType.CORNER_UP_FAR, new AxisAlignedBB(5.0F / 16.0F, 2.0F / 16.0F, 2.0F / 16.0F, 11.0F / 16.0F, 1, 1));

        open.put(EnumDummyType.WALL_NEAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 8.0F / 16.0F));
        open.put(EnumDummyType.WALL_FAR, new AxisAlignedBB(5.0F / 16.0F, 0, 8.0F / 16.0F, 11.0F / 16.0F, 1, 1));

        open.put(EnumDummyType.CORNER_DOWN_NEAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 8.0F / 16.0F));
        open.put(EnumDummyType.CORNER_DOWN_FAR, new AxisAlignedBB(5.0F / 16.0F, 0, 8.0F / 16.0F, 11.0F / 16.0F, 1, 1));

        open.put(EnumDummyType.ROOF, new AxisAlignedBB(5.0F / 16.0F, 12.0F / 16.0F, 0, 11.0F / 16.0F, 1, 1));


        Map<EnumDummyType, AxisAlignedBB> close = new HashMap<>();

        close.put(EnumDummyType.DOOR, new AxisAlignedBB(7.0F / 16.0F, 0, 0, 9.0F / 16.0F, 1, 1));
        close.put(EnumDummyType.FLOOR, new AxisAlignedBB(7.0F / 16.0F, 0, 0, 9.0F / 16.0F, 1, 1));

        close.put(EnumDummyType.CORNER_UP_NEAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
        close.put(EnumDummyType.CORNER_UP_FAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));

        close.put(EnumDummyType.WALL_NEAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
        close.put(EnumDummyType.WALL_FAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));

        close.put(EnumDummyType.CORNER_DOWN_NEAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
        close.put(EnumDummyType.CORNER_DOWN_FAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));

        close.put(EnumDummyType.ROOF, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));


        axisAlignedBBMap = compileMap(open, close, EnumFacing.Axis.Z);
    }
}
