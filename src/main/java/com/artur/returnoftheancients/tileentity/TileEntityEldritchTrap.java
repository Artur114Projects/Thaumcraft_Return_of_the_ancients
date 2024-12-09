package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.client.misc.CameraShake;
import com.artur.returnoftheancients.handlers.HandlerR;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;

import java.util.List;

public class TileEntityEldritchTrap extends TileEntity implements ITickable {
    private AxisAlignedBB detectionBox = null;
    private BlockPos[] poss = null;
    private byte phase = 0;
    private byte m = 8;

    @Override
    public void update() {
        if (detectionBox == null) {
            detectionBox = new AxisAlignedBB(pos.add(-7, 0, -7), pos.add(7, 3, 7));
        }
        if (poss == null) {
            poss = new BlockPos[] {pos.add(0, 1, -8), pos.add(0, 1, 7), pos.add(-8, 1, 0), pos.add(7, 1, 0)};
        }

        List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, detectionBox);

        if (!players.isEmpty() && phase == 0 && !players.get(0).isCreative()) {
            if (!world.isRemote) {
                for (EntityPlayer player : players) {
                    HandlerR.researchTC((EntityPlayerMP) player, "!ELDRITCH_TRAP");
                    player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 0));
                }
            }
            phase = 1;
            return;
        }

        if (phase == 1) {
            phase = 2;
            if (!world.isRemote) {
                world.setBlockState(pos.add(0, 4, 4), Blocks.AIR.getDefaultState());
                world.setBlockState(pos.add(-1, 4, 4), Blocks.AIR.getDefaultState());

                world.setBlockState(pos.add(4, 4, 0), Blocks.AIR.getDefaultState());
                world.setBlockState(pos.add(4, 4, -1), Blocks.AIR.getDefaultState());

                world.setBlockState(pos.add(0, 4, -5), Blocks.AIR.getDefaultState());
                world.setBlockState(pos.add(-1, 4, -5), Blocks.AIR.getDefaultState());

                world.setBlockState(pos.add(-5, 4, 0), Blocks.AIR.getDefaultState());
                world.setBlockState(pos.add(-5, 4, -1), Blocks.AIR.getDefaultState());

                CustomGenStructure.gen(world, pos.add(-3, 1, -8), "ancient_door_rock_rotate-1");
                CustomGenStructure.gen(world, pos.add(-3, 1, 7), "ancient_door_rock_rotate-1");
                CustomGenStructure.gen(world, pos.add(-8, 1, -3), "ancient_door_rock_rotate-2");
                CustomGenStructure.gen(world, pos.add(7, 1, -3), "ancient_door_rock_rotate-2");
            }
            if (world.isRemote) {
                for (EntityPlayer player : players) {
                    player.playSound(SoundEvents.BLOCK_STONE_PLACE, 8, 1);
                }
            }
            return;
        }

        if (phase == 2) {
            phase = 3;
            if (world.isRemote) {
                CameraShake.startShake(4);
                spawnParticles(BlocksTC.stoneAncient);
            }
            return;
        }

        if (phase == 3) {
            if (!world.isRemote) {
                int x;
                int z;
                if (world.rand.nextBoolean()) {
                    x = getRandomInt(8);
                    z = getRandomInt(2);
                } else {
                    x = getRandomInt(2);
                    z = getRandomInt(8);
                }
                Entity entity;
                if (world.rand.nextBoolean()) {
                    entity = ItemMonsterPlacer.spawnCreature(world, EntityList.getKey(EntityEldritchGuardian.class), (pos.getX() - 0.5) + x, (pos.getY() + 1), (pos.getZ() - 0.5) + z);
                } else {
                    entity = ItemMonsterPlacer.spawnCreature(world, EntityList.getKey(EntityInhabitedZombie.class), (pos.getX() - 0.5) + x, (pos.getY() + 1), (pos.getZ() - 0.5) + z);
                }
                if (entity != null) {
                    EntityLiving living = (EntityLiving) entity;
                    world.spawnEntity(living);
                }
            }
            m--;
            if (m <= 0) {
                phase = 4;
            }
            return;
        }


        if (phase == 4) {
            List<EntityLiving> livings = world.getEntitiesWithinAABB(EntityLiving.class, detectionBox);
            if (world.isRemote) {
                if (livings.isEmpty()) {
                    for (EntityPlayer player : players) {
                        player.playSound(SoundEvents.BLOCK_STONE_BREAK, 8, 1);
                    }
                    spawnParticles(BlocksTC.stoneArcane);
                }
            }
            if (!world.isRemote) {
                if (livings.isEmpty()) {
                    phase = 5;
                    world.setBlockState(pos, BlocksTC.stoneEldritchTile.getDefaultState());
                    breakDoors();
                }
            }
        }
    }

    private void breakDoors() {
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
                            if (!((y == 0 && x == 0) || (y == 3 && x == 0) || (y == 0 && x == 5) || (y == 3 && x == 5))) {
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
                            if (!((y == 0 && z == 0) || (y == 3 && z == 0) || (y == 0 && z == 5) || (y == 3 && z == 5))) {
                                world.setBlockState(posI.add(0, y, z), Blocks.AIR.getDefaultState());
                            }
                        }
                    }
                }

            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles(Block block) {
        ParticleManager particleManager = Minecraft.getMinecraft().effectRenderer;
        int stateID = Block.getStateId(block.getDefaultState());

        for (byte i = 0; i != poss.length; i++) {
            if (i < 2) {
                BlockPos posI = poss[i].add(-2, 3, 0);
                for (byte a = 0; a != 2; a++) {
                    if (a == 1) {
                        posI = posI.add(0, -2, 0);
                    }
                    for (byte x = 0; x != 5; x++) {
                        for (byte j = 0; j != 20; j++) {
                            particleManager.spawnEffectParticle(
                                    EnumParticleTypes.BLOCK_CRACK.getParticleID(),
                                    (posI.getX() + x) + world.rand.nextDouble(),
                                    posI.getY() + world.rand.nextDouble(),
                                    posI.getZ() + world.rand.nextDouble(),
                                    0.0D, 0.4D, 0.0D,
                                    stateID
                            );
                        }
                    }
                }
            }
            if (i >= 2) {
                BlockPos posI = poss[i].add(0, 3, -2);
                for (byte a = 0; a != 2; a++) {
                    if (a == 1) {
                        posI = posI.add(0, -2, 0);
                    }
                    for (byte z = 0; z != 5; z++) {
                        for (byte j = 0; j != 20; j++) {
                            particleManager.spawnEffectParticle(
                                    EnumParticleTypes.BLOCK_CRACK.getParticleID(),
                                    posI.getX() + world.rand.nextDouble(),
                                    posI.getY() + world.rand.nextDouble(),
                                    (posI.getZ() + z) + world.rand.nextDouble(),
                                    0.0D, 0.4D, 0.0D,
                                    stateID
                            );
                        }
                    }
                }
            }
        }
    }

    private int getRandomInt(int bound) {
        return world.rand.nextBoolean() ? world.rand.nextInt(bound) : -world.rand.nextInt(bound);
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
