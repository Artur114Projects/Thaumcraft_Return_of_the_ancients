package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.handlers.HandlerR;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import thaumcraft.common.entities.monster.EntityMindSpider;

import java.util.List;

public class TileEntityEldritchTrap extends TileEntity implements ITickable {
    private AxisAlignedBB detectionBox = null;
    private byte phase = 0;

    @Override
    public void update() {
        if (!world.isRemote) {
            if (detectionBox == null) {
                detectionBox = new AxisAlignedBB(pos.add(-7, -7, -7), pos.add(8, 8, 8));
            }

            List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, detectionBox);

            if (!players.isEmpty() && phase == 0) {
                phase = 1;
                return;
            }

            if (phase == 1) {
                phase = 2;
                CustomGenStructure.gen(world, pos.add(-3, 1, -8), "ancient_door_rock_rotate-1");
                CustomGenStructure.gen(world, pos.add(-3, 1, 7), "ancient_door_rock_rotate-1");
                CustomGenStructure.gen(world, pos.add(-8, 1, -3), "ancient_door_rock_rotate-2");
                CustomGenStructure.gen(world, pos.add(7, 1, -3), "ancient_door_rock_rotate-2");
                return;
            }

            if (phase == 2) {
                phase = 3;
                for (int i = 0; i != 10; i++) {
                    Entity spider = ItemMonsterPlacer.spawnCreature(world, EntityList.getKey(EntityMindSpider.class), pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                    if (spider != null) {
                        world.spawnEntity(spider);
                    }
                }
                return;
            }

            if (phase == 3) {
                List<EntityLiving> livings = world.getEntitiesWithinAABB(EntityLiving.class, detectionBox);
                if (livings.isEmpty()) {
                    world.setBlockState(pos, HandlerR.getBlockByString("thaumcraft:stone_eldritch_tile").getDefaultState());
                    BlockPos[] poss = new BlockPos[] {pos.add(0, 1, -8), pos.add(0, 1, 7), pos.add(-8, 1, 0), pos.add(7, 1, 0)};
                    byte orientation4Door = (byte) world.rand.nextInt(4);
                    byte orientation2Door = (byte) world.rand.nextInt(4);
                    for (byte i = 0; i != poss.length; i++) {
                        if (i == orientation4Door) {
                            if (i < 2) {
                                world.setBlockState(poss[i].add(0, 0, 0), Blocks.AIR.getDefaultState());
                                world.setBlockState(poss[i].add(-1, 0, 0), Blocks.AIR.getDefaultState());
                                world.setBlockState(poss[i].add(0, 1, 0), Blocks.AIR.getDefaultState());
                                world.setBlockState(poss[i].add(-1, 1, 0), Blocks.AIR.getDefaultState());
                            }
                            if (i >= 2) {
                                world.setBlockState(poss[i].add(0, 0, 0), Blocks.AIR.getDefaultState());
                                world.setBlockState(poss[i].add(0, 0, -1), Blocks.AIR.getDefaultState());
                                world.setBlockState(poss[i].add(0, 1, 0), Blocks.AIR.getDefaultState());
                                world.setBlockState(poss[i].add(0, 1, -1), Blocks.AIR.getDefaultState());
                            }
                        }
                        if (i == orientation2Door) {
                            world.setBlockState(poss[i].add(0, 0, 0), Blocks.AIR.getDefaultState());
                            world.setBlockState(poss[i].add(0, 1, 0), Blocks.AIR.getDefaultState());
                        }
                        if (i < 2) {
                            BlockPos posI = poss[i].add(-3, 0, 0);
                            for (byte y = 0; y != 4; y++) {
                                for (byte x = 0; x != 6; x++) {
                                    if (world.rand.nextInt(2) == 0) {
                                        if ((y != 0 && y != 3) && (x != 0 && x != 5)) {
                                            world.setBlockState(posI.add(x, y, 0), Blocks.AIR.getDefaultState());
                                        }
                                    }
                                }
                            }
                        }
                        if (i >= 2) {
                            BlockPos posI = poss[i].add(0, 0, -3);
                            for (byte y = 0; y != 4; y++) {
                                for (byte z = 0; z != 6; z++) {
                                    if (world.rand.nextInt(2) == 0) {
                                        if ((y != 0 && y != 3) && (z != 0 && z != 5)) {
                                            world.setBlockState(posI.add(0, y, z), Blocks.AIR.getDefaultState());
                                        }
                                    }
                                }
                            }

                        }
                    }
                    phase = 4;
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound nbt = super.writeToNBT(compound);
        nbt.setByte("phase", phase);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        phase = compound.getByte("phase");
    }
}
