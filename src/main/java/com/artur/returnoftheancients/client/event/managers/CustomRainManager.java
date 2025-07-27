package com.artur.returnoftheancients.client.event.managers;

import com.artur.returnoftheancients.ancientworld.map.utils.structures.StructureBase;
import com.artur.returnoftheancients.client.fx.particle.ParticleHeat;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;
import java.util.Random;

public class CustomRainManager {
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();
    private final Random random = new Random(System.currentTimeMillis());

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();

        if (e.phase != TickEvent.Phase.START || player == null || e.side == Side.SERVER || mc.isGamePaused()) {
            return;
        }

        this.addScorchedLandRainParticles(mc, player);
        this.addAncientWorldHeatParticles(mc, player);
    }

    private void addScorchedLandRainParticles(Minecraft mc, EntityPlayer player) {
        float rainStrength = mc.world.getRainStrength(1.0F);

        if (rainStrength != 0.0F) {

            blockPos.setPos(Objects.requireNonNull(mc.getRenderViewEntity()));
            World world = mc.world;

            int particlesCount = (int) (50.0F * rainStrength * rainStrength);

            if (mc.gameSettings.particleSetting == 1) {
                particlesCount >>= 1;
            }
            else if (mc.gameSettings.particleSetting == 2) {
                particlesCount = 0;
            }

            for (int i = 0; i < particlesCount; i++) {
                blockPos.pushPos();
                BlockPos precipitationHeight = world.getPrecipitationHeight(blockPos.add(random.nextInt(20) - random.nextInt(20), 0, random.nextInt(20) - random.nextInt(20)));
                blockPos.popPos();

                Biome biome = world.getBiome(precipitationHeight);

                if (precipitationHeight.getY() <= blockPos.getY() + 10 && precipitationHeight.getY() >= blockPos.getY() - 10 && (biome == InitBiome.TAINT_WASTELAND || biome == InitBiome.INFERNAL_CRATER)) {
                    blockPos.pushPos();

                    blockPos.setPos(precipitationHeight).down();
                    IBlockState iblockstate = world.getBlockState(blockPos);
                    double d3 = random.nextDouble();
                    double d4 = random.nextDouble();
                    AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(world, blockPos);

                    if (iblockstate.getMaterial() != Material.AIR && !iblockstate.getMaterial().isLiquid()) {
                        EnumParticleTypes type = biome == InitBiome.INFERNAL_CRATER && random.nextFloat() < 0.25 ? EnumParticleTypes.FLAME : EnumParticleTypes.SMOKE_NORMAL;
                        mc.world.spawnParticle(type, (double)blockPos.getX() + d3, (double)((float)blockPos.getY() + 0.1F) + axisalignedbb.maxY, (double)blockPos.getZ() + d4, 0.0D, random.nextDouble() / 4, 0.0D);
                    }

                    blockPos.popPos();
                }
            }
            blockPos.setToZero();
        }
    }

    private void addAncientWorldHeatParticles(Minecraft mc, EntityPlayer player) {
        if (player.dimension != InitDimensions.ancient_world_dim_id) {
            return;
        }

        blockPos.setPos(Objects.requireNonNull(mc.getRenderViewEntity()));
        World world = mc.world;

        int particlesCount = 30;

        if (mc.gameSettings.particleSetting == 1) {
            particlesCount >>= 1;
        }
        else if (mc.gameSettings.particleSetting == 2) {
            particlesCount = 0;
        }

        for (int i = 0; i < particlesCount; i++) {
            blockPos.pushPos();

            blockPos.add(random.nextInt(20) - random.nextInt(20), 8, random.nextInt(20) - random.nextInt(20));

            for (int j = 0; j != 17; j++) {
                blockPos.pushPos();

                blockPos.down(j);

                if (world.getBlockState(blockPos).getMaterial().isSolid() && world.getBlockState(blockPos.up()).getMaterial() == Material.AIR) {
                    mc.effectRenderer.addEffect(new ParticleHeat(world, blockPos.getX() + this.random.nextDouble(), blockPos.getY() + 0.1D, blockPos.getZ() + this.random.nextDouble()));
                }

                blockPos.popPos();
            }

            blockPos.popPos();
        }
    }
}
