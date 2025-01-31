package com.artur.returnoftheancients.generation.portal.base.client;

import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class ClientAncientPortal {

    public static final BlockPos[] offsetsArray;

    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

    public final Random random = new Random(System.currentTimeMillis());
    public final ChunkPos portalPos;

    protected final Minecraft mc;

    public final int dimension;
    public final int chunkX;
    public final int chunkZ;

    public final int posX;

    public final int posY;

    public final int posZ;

    public final int id;

    public ClientAncientPortal(NBTTagCompound data) {
        this.dimension = data.getInteger("dimension");
        this.chunkX = data.getInteger("chunkX");
        this.chunkZ = data.getInteger("chunkZ");
        this.portalPos = new ChunkPos(chunkX, chunkZ);
        this.posY = data.getInteger("posY");
        this.id = data.getInteger("id");
        this.mc = Minecraft.getMinecraft();
        this.posX = chunkX << 4;
        this.posZ = chunkZ << 4;
    }

    public boolean isLoaded() {
        return !mc.world.getChunkProvider().provideChunk(chunkX, chunkZ).isEmpty();
    }

    public boolean isCollide(BlockPos pos) {
        blockPos.setPos(pos);
        if (blockPos.getChunkX() == chunkX && blockPos.getChunkZ() == chunkZ) {
            blockPos.setPos(portalPos).setY(0);
            for (BlockPos offset : offsetsArray) {
                blockPos.pushPos();
                if (blockPos.add(offset).equalsXZ(pos)) {
                    return true;
                }
                blockPos.popPos();
            }
        }
        return false;
    }

    public void updateCollidedPlayer(EntityPlayer player) {

    }

    public void update(EntityPlayer player, World world) {
        blockPos.setPos(player);
        if (blockPos.distance(portalPos) < 2) {
            blockPos.setPos(portalPos).setY(posY);
            for (BlockPos offset : offsetsArray) {
                blockPos.offsetAndCallRunnable(offset, offsetPos -> {
                    for (int i = 0; i != 4; i++) {
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, offsetPos.getX() + random.nextDouble(), offsetPos.getY() + random.nextDouble(), offsetPos.getZ() + random.nextDouble(), 0, random.nextDouble() / 4, 0);
                    }
                });
            }
        }
    }


    static {
        List<BlockPos> offsets = new ArrayList<>();

        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();
        pos.setPos(6, 0, 6);

        for (int x = 0; x != 4; x++) {
            for (int z = 0; z != 4; z++) {
                if ((x == 0 && z == 0) || (x == 3 && z == 3) || (x == 3 && z == 0) || (x == 0 && z == 3)) {
                    continue;
                }

                pos.pushPos();
                offsets.add(pos.add(x, 0, z).toImmutable());
                pos.popPos();
            }
        }

        UltraMutableBlockPos.returnBlockPosToPoll(pos);
        offsetsArray = offsets.toArray(new BlockPos[0]);
    }
}
