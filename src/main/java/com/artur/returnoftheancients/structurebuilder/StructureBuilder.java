package com.artur.returnoftheancients.structurebuilder;

import com.artur.returnoftheancients.blockprotect.BlockProtectHandler;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.structurebuilder.interf.IBuildProperties;
import com.artur.returnoftheancients.structurebuilder.interf.IStructureBuilder;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StructureBuilder implements IStructureBuilder {
    protected final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();
    protected final Map<Integer, NBTTagCompound> tilesData = new HashMap<>();
    protected final IBlockState[] palette;
    protected final boolean[] blocksUseEbs;
    protected final BlockPos halfSizeXZ;
    protected final BlockPos size;
    protected final int[] blocks;
    protected final String name;

    public StructureBuilder(String name) {
        NBTTagCompound structureNBT = this.readStructureAsName(name);
        NBTTagList palette = structureNBT.getTagList("palette", 10);
        NBTTagList blocksNBT = structureNBT.getTagList("blocks", 10);
        NBTTagList nbttaglist = structureNBT.getTagList("size", 3);
        this.size = new BlockPos(nbttaglist.getIntAt(0), nbttaglist.getIntAt(1), nbttaglist.getIntAt(2));
        this.halfSizeXZ = new BlockPos(nbttaglist.getIntAt(0) / 2, 0, nbttaglist.getIntAt(2) / 2);

        this.name = name;

        this.palette = new IBlockState[palette.tagCount()];
        this.blocksUseEbs = new boolean[palette.tagCount()];
        for (int i = 0; i != palette.tagCount(); i++) {
            IBlockState state = NBTUtil.readBlockState(palette.getCompoundTagAt(i));
            this.palette[i] = state;
            this.blocksUseEbs[i] = (state.isFullBlock() && !state.canProvidePower() && state.isFullCube() && state.getLightValue() == 0 && !state.getBlock().hasTileEntity(state)) || state.getMaterial() == Material.AIR;
        }

        ArrayList<Integer> rawBlocks = new ArrayList<>(blocksNBT.tagCount());
        for (int i = 0; i != blocksNBT.tagCount(); i++) {
            NBTTagCompound compound = blocksNBT.getCompoundTagAt(i);
            NBTTagList pos = compound.getTagList("pos", 3);
            int state = compound.getInteger("state");
            int block = this.packBytes((byte) pos.getIntAt(0), (byte) pos.getIntAt(1), (byte) pos.getIntAt(2), (byte) state);
            rawBlocks.add(block);
            if (compound.hasKey("nbt")) {
                this.tilesData.put(block, compound.getCompoundTag("nbt"));
            }
        }

        this.blocks = rawBlocks.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public @NotNull String name() {
        return this.name;
    }

    @Override
    public void build(World world, BlockPos pos, IBuildProperties properties) {
        for (int block : blocks) {
            byte x = (byte) (block >> 24);
            byte y = (byte) (block >> 16);
            byte z = (byte) (block >> 8);
            byte type = (byte) block;
            IBlockState state = properties.blockStateHook(this.palette[type]);
            if (state == null) return;
            this.blockPos.setPos(pos).add(x, y, z);
            if (properties.isPosAsXZCenter()) this.blockPos.deduct(this.halfSizeXZ);
            if (state.getMaterial() == Material.AIR && properties.isIgnoreAir()) continue;
            if (this.blockPos.getY() >> 4 >= 16 || this.blockPos.getY() >> 4 < 0) continue;

            if (properties.isNeedProtect() && state.getMaterial() != Material.AIR && properties.blockProtectHook(state, blockPos)) {
                BlockProtectHandler.protect(world, this.blockPos);
            }

            if (this.blocksUseEbs[type] && properties.isUseEBSHook(state)) {
                ExtendedBlockStorage storage = this.blockPos.ebs(world);
                this.blockPos.normalizeToEBS();
                storage.set(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ(), state);
            } else {
                NBTTagCompound data = this.tilesData.get(block);
                if (world.setBlockState(this.blockPos, state) && data != null) {
                    TileEntity tile = world.getTileEntity(this.blockPos);

                    if (tile != null) {
                        data.setInteger("x", this.blockPos.getX());
                        data.setInteger("y", this.blockPos.getY());
                        data.setInteger("z", this.blockPos.getZ());
                        tile.readFromNBT(data);
                        tile.markDirty();
                    }
                }
            }
        }

        if (properties.isNeedMarkRenderUpdate()) {
            if (properties.isPosAsXZCenter()) {
                world.markBlockRangeForRenderUpdate(pos.toImmutable().add(-this.size.getX() / 2, -this.size.getY() / 2, -this.size.getZ() / 2), this.blockPos.setPos(pos).add(this.size.getX() / 2, this.size.getY() / 2, this.size.getZ() / 2));
            } else {
                world.markBlockRangeForRenderUpdate(pos, this.blockPos.setPos(pos).add(this.size));
            }
        }
    }

    private NBTTagCompound readStructureAsName(String structureName) {
        InputStream inputstream = MinecraftServer.class.getResourceAsStream("/assets/" + Referense.MODID + "/structures/" + structureName + ".nbt");
        try {
            assert inputstream != null;
            return CompressedStreamTools.readCompressed(inputstream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    protected int packBytes(byte b1, byte b2, byte b3, byte b4) {
        return ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
    }
}
