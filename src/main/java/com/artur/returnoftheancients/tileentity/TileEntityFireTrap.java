package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.client.fx.particle.RotateParticleFlame;
import com.artur.returnoftheancients.client.fx.particle.ParticleFlameCanCollide;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.misc.TRAConfigs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityFireTrap extends TileEntity implements ITickable {
    private AxisAlignedBB detectionBox = null;
    private IBlockState fireState = null;
    private BlockPos[] poss = null;
    private int soundTimer = 20;
    private boolean isActivee = false;
    private int activeTimer = 0;
    private boolean isActivating = false;
    private int activatingTimer = 0;

    @Override
    public void update() {
        if (world != null) {
            if (detectionBox == null) {
                detectionBox = new AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 2, 2));
            }
            if (poss == null) {
                poss = new BlockPos[] {pos.add(0, 1 ,1), pos.add(0, 1 ,-1), pos.add(1, 1 ,0), pos.add(-1, 1 ,0)};
            }
            if (fireState == null) {
                fireState = Blocks.FIRE.getDefaultState();
            }
            List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, detectionBox);
            boolean playerNearby = !players.isEmpty();

            if (!isActivating) {
                if (!isActivee) {
                    if (playerNearby) {
                        isActivating = true;
                        if (!world.isRemote) {
                            world.playSound(null, pos, InitSounds.FIRE_TRAP_START_SOUND.SOUND, SoundCategory.BLOCKS, 6f, 1f);
                        }
                    }
                }
            }

            if (isActivating) {
                activatingTimer++;
                if (activatingTimer >= TRAConfigs.DifficultySettings.incineratorActivationSpeed) {
                    activatingTimer = 0;
                    isActivating = false;
                    activeTimer = 40;
                    isActivee = true;
                }
                return;
            }

            if (activeTimer > 0) {
                activeTimer--;
            } else if (!playerNearby && isActivee) {
                isActivee = false;
                soundTimer = 20;
            }

            if (isActivee) {
                if (!world.isRemote) {
                    soundTimer++;
                    if (soundTimer >= 20) {
                        world.playSound(null, pos, InitSounds.FIRE_TRAP_SOUND.SOUND, SoundCategory.BLOCKS, 4f, 1f);
                        soundTimer = 0;
                    }
                }
                if (world.isRemote) {
                    spawnParticle(world, pos);
                }
            }

            if (isActivee) {
                List<EntityLiving> livings = world.getEntitiesWithinAABB(EntityLiving.class, detectionBox);
                if (!world.isRemote) {

                    IBlockState state0 = world.getBlockState(poss[0]);
                    IBlockState state1 = world.getBlockState(poss[1]);
                    IBlockState state2 = world.getBlockState(poss[2]);
                    IBlockState state3 = world.getBlockState(poss[3]);

                    if (state0.getBlock().equals(Blocks.AIR)) {
                        world.setBlockState(poss[0], fireState);
                    }
                    if (state1.getBlock().equals(Blocks.AIR)) {
                        world.setBlockState(poss[1], fireState);
                    }
                    if (state2.getBlock().equals(Blocks.AIR)) {
                        world.setBlockState(poss[2], fireState);
                    }
                    if (state3.getBlock().equals(Blocks.AIR)) {
                        world.setBlockState(poss[3], fireState);
                    }
                }

                for (EntityPlayer player : players) {
                    if (world.isRemote) {
                        if (!player.isBurning() && !player.isCreative()) {
                            player.performHurtAnimation();
                            player.playSound(SoundEvents.ENTITY_PLAYER_HURT, 1, 1);
                        }
                    }
                    if (!world.isRemote) {
                        if (!player.isCreative()) {
                            boolean haveResistance = player.getActivePotionEffect(MobEffects.FIRE_RESISTANCE) != null;
                            if (!player.isBurning()) {
                                if (haveResistance) {
                                    player.setHealth(player.getHealth() - (player.getHealth() / 4));
                                } else {
                                    player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 160, 1));
                                    player.setHealth(player.getHealth() / 2);
                                }
                            }
                            if (!haveResistance) {
                                if (!((player.getHealth() - 0.2f) <= 0)) {
                                    player.setHealth(player.getHealth() - 0.2f);
                                }
                            }
                            player.setFire(8);
                        }
                    }
                }

                for (EntityLiving living : livings) {
                    if (!world.isRemote && living.isNonBoss()) {
                        if (!living.isBurning()) {
                            living.setHealth(living.getHealth() / 2);
                        }
                        living.setHealth(living.getHealth() - 0.1f);
                        living.setFire(8);
                    }
                }
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public static void spawnParticle(World world, BlockPos pos) {
        for (int i = 0; i != 4; i++) {
            spawnCustomParticle(world, pos.getX() + 0.7, pos.getY() + 1, pos.getZ() + 0.5,  world.rand.nextDouble() / 80.0D, 0.6 + i / 20D,  world.rand.nextDouble() / 80.0D);
            spawnCustomParticle(world, pos.getX() + 0.3, pos.getY() + 1, pos.getZ() + 0.5,  world.rand.nextDouble() / 80.0D, 0.6 + i / 20D,  world.rand.nextDouble() / 80.0D);
            spawnCustomParticle(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.3,  world.rand.nextDouble() / 80.0D, 0.6 + i / 20D,  world.rand.nextDouble() / 80.0D);
            spawnCustomParticle(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.7,  world.rand.nextDouble() / 80.0D, 0.6 + i / 20D,  world.rand.nextDouble() / 80.0D);
        }
        for (int i = 0; i != 6; i++) {
            spawnCustomParticleTM(world, pos.getX(), pos.getY() + 0.2, pos.getZ(), 0.5D);
            spawnCustomParticleTM(world, pos.getX(), pos.getY() + 0.8, pos.getZ(), 1.0D);
            spawnCustomParticleTM(world, pos.getX(), pos.getY() + 1.4, pos.getZ(), 0.5D);

            spawnCustomParticle(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, world.rand.nextDouble() / 80.0D, 0.6 + i / 20D,  world.rand.nextDouble() / 80.0D);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void spawnCustomParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFlameCanCollide(world, x, y, z, xSpeed, ySpeed, zSpeed));
    }

    @SideOnly(Side.CLIENT)
    public static void spawnCustomParticleTM(World world, double x, double y, double z, double radius) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new RotateParticleFlame(world, x, y + 1.5, z, radius, 0.6 + (world.rand.nextDouble() / 5)));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound nbt = super.writeToNBT(compound);
        compound.setBoolean("isActivating", isActivating);
        compound.setBoolean("isActivee", isActivee);
        compound.setInteger("activatingTimer", activatingTimer);
        compound.setInteger("activeTimer", activeTimer);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        isActivating = compound.getBoolean("isActivating");
        isActivee = compound.getBoolean("isActivee");
        activatingTimer = compound.getInteger("activatingTimer");
        activeTimer = compound.getInteger("activeTimer");
    }
}
