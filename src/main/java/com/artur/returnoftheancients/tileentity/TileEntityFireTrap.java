package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.FireTrap;
import com.artur.returnoftheancients.client.TrapParticleFlame;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.main.MainR;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockGlowstone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static com.artur.returnoftheancients.blocks.FireTrap.ACTIVE;

public class TileEntityFireTrap extends TileEntity implements ITickable {
    private AxisAlignedBB detectionBox = null;
    private IBlockState fireState = null;
    private BlockPos[] poss = null;
    private int soundTimer = 18;

    @Override
    public void update() {
        if (world != null) {
            if (detectionBox == null) {
                detectionBox = new AxisAlignedBB(pos.add(-1, -1, -1), pos.add(2, 2, 2));
            }
            if (poss == null) {
                poss = new BlockPos[]{pos.add(0, 1 ,1), pos.add(0, 1 ,-1), pos.add(1, 1 ,0), pos.add(-1, 1 ,0)};
            }
            if (fireState == null) {
                fireState = Blocks.FIRE.getDefaultState();
            }
            List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, detectionBox);
            IBlockState currentState = world.getBlockState(pos);
            boolean isActive = currentState.getValue(ACTIVE);
            boolean playerNearby = !players.isEmpty();

            if (playerNearby) {
                List<EntityLiving> livings = world.getEntitiesWithinAABB(EntityLiving.class, detectionBox);
                if (!world.isRemote) {

                    if (world.getBlockState(poss[0]).getBlock().equals(Blocks.AIR)) {
                        world.setBlockState(poss[0], fireState);
                    }
                    if (world.getBlockState(poss[1]).getBlock().equals(Blocks.AIR)) {
                        world.setBlockState(poss[1], fireState);
                    }
                    if (world.getBlockState(poss[2]).getBlock().equals(Blocks.AIR)) {
                        world.setBlockState(poss[2], fireState);
                    }
                    if (world.getBlockState(poss[3]).getBlock().equals(Blocks.AIR)) {
                        world.setBlockState(poss[3], fireState);
                    }

                    soundTimer++;
                    if (soundTimer >= 18) {
                        world.playSound(null, pos, InitSounds.FIRE_TRAP_SOUND.SOUND, SoundCategory.BLOCKS, 4f, 1f);
                        soundTimer = 0;
                    }
                }

                for (EntityPlayer player : players) {
                    if (world.isRemote) {
                        spawnParticle(world, pos);
                        if (!player.isBurning() && !player.isCreative()) {
                            player.performHurtAnimation();
                            player.playSound(SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE, 1, 1);
                        }
                    }
                    if (!world.isRemote) {
                        if (!player.isCreative()) {
                            boolean haveResistance = player.getActivePotionEffect(MobEffects.FIRE_RESISTANCE) != null;
                            if (!player.isBurning()) {
                                if (haveResistance) {
                                    player.setHealth(player.getHealth() - (player.getHealth() / 4));
                                } else {
                                    player.setHealth(player.getHealth() / 2);
                                }
                            }
                            if (!haveResistance) {
                                player.setHealth(player.getHealth() - 0.2f);
                            }
                            player.setFire(20);
                        }
                    }
                }

                for (EntityLiving living : livings) {
                    if (!world.isRemote) {
                        if (!living.isBurning()) {
                            living.setHealth(living.getHealth() / 2);
                        }
                        living.setHealth(living.getHealth() - 0.1f);
                        living.setFire(20);
                    }
                }
            }

            if (playerNearby && !isActive) {
                world.setBlockState(pos, currentState.withProperty(ACTIVE, true), 3);
            } else if (!playerNearby && isActive) {
                world.setBlockState(pos, currentState.withProperty(ACTIVE, false), 3);
                if (!world.isRemote) {
                    soundTimer = 18;
                }
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public static void spawnParticle(World world, BlockPos pos) {
        for (int i = 0; i != 4; i++) {
            spawnCustomParticle(world, pos.getX() + 0.7, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.1 + i / 20D, 0);
            spawnCustomParticle(world, pos.getX() + 0.3, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.1 + i / 20D, 0);
            spawnCustomParticle(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.3, 0, 0.1 + i / 20D, 0);
            spawnCustomParticle(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.7, 0, 0.1 + i / 20D, 0);
        }
        for (int i = 0; i != 6; i++) {
            spawnCustomParticle(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.1 + i / 20D, 0);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void spawnCustomParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new TrapParticleFlame(world, x, y, z, xSpeed, ySpeed, zSpeed));
    }
}
