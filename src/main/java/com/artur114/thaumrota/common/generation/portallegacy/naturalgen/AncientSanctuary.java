package com.artur114.thaumrota.common.generation.portallegacy.naturalgen;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.math.m2d.vec.PosMc2I;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.bananalib.mc.nbt.IReadFromNBT;
import com.artur114.thaumrota.common.worldstate.blockprotect.BlockProtectHandler;
import com.artur114.thaumrota.common.init.InitBlocks;
import com.artur114.thaumrota.server.structurebuilder.BuildRequest;
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager;
import com.artur114.thaumrota.common.generation.portallegacy.util.PortalOffsets;
import com.artur114.thaumrota.common.event.CommonEventsHandler;
import com.artur114.thaumrota.common.init.InitSounds;
import com.artur114.thaumrota.common.tileentity.TileEntityAncientSanctuaryController;
import com.artur114.thaumrota.common.util.TerrainAnalyzer;
import com.artur114.bananalib.mc.nbt.IWriteToNBT;

import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.blocks.BlocksTC;

import java.util.Arrays;
import java.util.List;

public class AncientSanctuary implements IWriteToNBT, IReadFromNBT {
    private static final Logger log = LogManager.getLogger("ThaumRotA/PortalsLegacy");
    private final Block nitorBlock = BlocksTC.nitor.get(EnumDyeColor.BLACK);
    private AncientPortalNaturalGen portal = null;
    private TileEntityAncientSanctuaryController tile;
    private boolean isBuild = false;
    private boolean active = false;
    private PosMc2I pos;
    private World world;
    private BlockPos tilePos;
    private Type type;

    protected AncientSanctuary(World world, ChunkPos pos, Type type) {
        this.world = world;
        this.type = type;
        this.pos = new PosMc2I(pos.x, pos.z);
    }

    protected AncientSanctuary() {}

    protected BlockPos generate(TerrainAnalyzer analyzer, EnumFacing archFacing, BlockPos[] sanctuaryPillars) {
        if (this.isBuild) {
            return this.getSanctuaryArchOffset(archFacing);
        }

        PosMc3IM blockPos = PosMc3IM.obtain();

        blockPos.setChunk(this.pos).setY(analyzer.getMaxHeight());
        this.type.createBuildRequest(this.world, blockPos.addY(2)).build();

        if (!type.isBroken()) {
            blockPos.pushPos();
            this.bindTile(blockPos.add(7, 2, 7).toImmutable());
            this.updateActiveState(this.tile.isDone());
            blockPos.popPos();
        }

        blockPos.addY(-1);

        for (BlockPos offset : sanctuaryPillars) {
            blockPos.pushPos();

            for (blockPos.add(offset); (this.world.isAirBlock(blockPos) || this.world.getBlockState(blockPos).getMaterial().isLiquid()); blockPos.down()) {
                this.world.setBlockState(blockPos, BlocksTC.stoneEldritchTile.getDefaultState());
                BlockProtectHandler.protect(this.world, blockPos);
            }

            blockPos.popPos();
        }

        BlockPos offset = getSanctuaryArchOffset(archFacing);
        BlockPos ret = blockPos.add(offset).setY(BananaMC.findHighestBlock(this.world, blockPos)).addY(-1).toImmutable();

        PosMc3IM.release(blockPos);

        this.isBuild = true;

        log.debug("Generated new ancient sanctuary pos: {}", this.pos);

        return ret;
    }

    private BlockPos getSanctuaryArchOffset(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return new BlockPos(7, 0, 2);
            case WEST:
                return new BlockPos(2, 0, 7);
            case SOUTH:
                return new BlockPos(7, 0, 12);
            case EAST:
                return new BlockPos(12, 0, 7);
            default:
                return new BlockPos(0, 0, 0);
        }
    }

    private void bindTile(BlockPos pos) {
        this.tilePos = pos;

        TileEntity tileRaw = this.world.getTileEntity(pos);

        if (tileRaw instanceof TileEntityAncientSanctuaryController) {
            this.tile = (TileEntityAncientSanctuaryController) tileRaw;
            this.tile.bindSanctuary(this);
        } else {
            this.world.setBlockState(pos, InitBlocks.ANCIENT_SANCTUARY_CONTROLLER.getDefaultState());
            this.bindTile(pos);
        }
    }

    protected void bindPortal(AncientPortalNaturalGen portal) {
        this.portal = portal;
        if (!this.type.isBroken()) {
            this.portal.updateActiveState(this.active);
        }
    }

    public void onTileLoad(boolean state, BlockPos pos) {
        if (!pos.equals(this.tilePos) || this.type.isBroken()) {
            return;
        }
        this.bindTile(this.tilePos);
        this.updateActiveState(state);
    }

    public void onTileStateChanged(boolean state) {
        this.tile.setCanOpen(false);
        CommonEventsHandler.TIMER_TASKS_MANAGER.addTask(29, () -> {
            CommonEventsHandler.SHORT_CHUNK_LOAD_MANAGER.loadArea(world, portal.portalPos, 1);
            CommonEventsHandler.SHORT_CHUNK_LOAD_MANAGER.loadArea(world, pos, 1);
            CommonEventsHandler.TIMER_TASKS_MANAGER.addTask(1, () -> {
                this.updateActiveState(state);
                this.portal.updateActiveState(state);

                PosMc3IM blockPos = PosMc3IM.obtain();
                this.world.playSound(null, blockPos.set(tilePos).add(0, 2, 0), InitSounds.SPOTLIGHT, SoundCategory.AMBIENT, 1, 1);

                blockPos.setX(portal.portalPos.x << 4).setZ(portal.portalPos.z << 4).setY(portal.posY + 10);

                for (BlockPos pos : PortalOffsets.getCornerOffsets(2, 13)) {
                    blockPos.pushPos();
                    blockPos.add(pos);
                    this.world.playSound(null, blockPos, InitSounds.SPOTLIGHT, SoundCategory.AMBIENT, 1, 1);
                    blockPos.popPos();
                }

                PosMc3IM.release(blockPos);

                this.tile.setCanOpen(true);
            });
        });
    }

    protected void updateActiveState(boolean newState) {
        if (newState == active || type.isBroken() || tilePos == null) {
            return;
        }

        this.setLightState(newState);

        this.active = newState;
    }

    private void setLightState(boolean state) {
        if (state) {
            this.enableLight();
        } else {
            this.disableLight();
        }
    }

    public void enableLight() {
        this.callRunnableOnLightOffsets(pos -> {
            if (world.isAirBlock(pos)) {
                world.setBlockState(pos, nitorBlock.getDefaultState());
                BlockProtectHandler.protect(world, pos);
            }
        });
    }

    public void disableLight() {
        this.callRunnableOnLightOffsets(pos -> {
            if (world.getBlockState(pos).getBlock() == nitorBlock) {
                world.setBlockToAir(pos);
                BlockProtectHandler.unProtect(world, pos);
            }
        });
    }

    private void callRunnableOnLightOffsets(Consumer<PosMc3IM> run) {
        PosMc3IM blockPos = PosMc3IM.obtain();

        blockPos.set(this.tilePos).addY(3);
        run.accept(blockPos);
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            blockPos.pushPos();
            blockPos.offset(facing, 3);
            run.accept(blockPos);
            blockPos.popPos();
        }

        BlockPos[] backLights = new BlockPos[] {
                new BlockPos(3, 0, 4),
                new BlockPos(4, 0, 3),
                new BlockPos(10, 0, 3),
                new BlockPos(11, 0, 4),
                new BlockPos(11, 0, 10),
                new BlockPos(10, 0, 11),
                new BlockPos(4, 0, 11),
                new BlockPos(3, 0, 10),
        };

        blockPos.setChunk(this.pos);
        for (int i = 0; i != 2; i++) {
            int y;
            if (i == 0) {
                y = tilePos.getY() - 1;
            } else {
                y = tilePos.getY() + 2;
            }
            blockPos.setY(y);

            for (BlockPos pos : backLights) {
                blockPos.pushPos();
                blockPos.add(pos);
                run.accept(blockPos);
                blockPos.popPos();
            }
        }

        PosMc3IM.release(blockPos);
    }

    protected Type getType() {
        return type;
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
        this.type.writeToNBT(nbt);
        if (!this.type.isBroken() && tilePos != null) {
            nbt.setLong("tilePos", tilePos.toLong());
        }
        nbt.setInteger("dimension", world.provider.getDimension());
        nbt.setBoolean("isBuild", isBuild);
        nbt.setBoolean("active", active);
        nbt.setInteger("posX", pos.x);
        nbt.setInteger("posZ", pos.z);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        this.world = server.getWorld(nbt.getInteger("dimension"));
        this.type = Type.getTypeFromNBT(nbt);
        this.pos = new PosMc2I(nbt.getInteger("posX"), nbt.getInteger("posZ"));
        this.isBuild = nbt.getBoolean("isBuild");

        if (!this.type.isBroken()) {
            this.bindTile(BlockPos.fromLong(nbt.getLong("tilePos")));
            this.active = nbt.getBoolean("active");
        }
    }

    protected enum Type implements IWriteToNBT {
        NORMAL("ancient_sanctuary", false, false),
        BROKEN("ancient_sanctuary_broken", true, true),
        CULTIST("ancient_sanctuary_cultist", true, false),
        SCORCHED("ancient_sanctuary_broken", true, true);

        private final String structureName;
        private final boolean isBrokenArch;
        private final boolean isBroken;

        Type(String structureName, boolean isBroken, boolean isBrokenArch) {
            this.structureName = structureName;
            this.isBrokenArch = isBrokenArch;
            this.isBroken = isBroken;
        }

        public BuildRequest createBuildRequest(World world, BlockPos pos) {
            if (this == CULTIST) {
                return StructuresBuildManager.createBuildRequest(world, pos, this.getStructureName()).setIgnoreAir().setNeedProtect().addBlockProtectHook(((state, pos1) -> state.getBlock() != Blocks.TORCH && state.getBlock() != BlocksTC.bannerCrimsonCult));
            } else {
                return StructuresBuildManager.createBuildRequest(world, pos, this.getStructureName()).setIgnoreAir().setNeedProtect();
            }
        }

        public String getStructureName() {
            return structureName;
        }

        public boolean isBroken() {
            return isBroken;
        }

        public boolean isBrokenArch() {
            return isBrokenArch;
        }

        @Override
        public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            nbt.setInteger("AncientSanctuaryType", this.ordinal());
            return nbt;
        }

        public static Type getTypeFromNBT(NBTTagCompound nbt) {
            int id = nbt.getInteger("AncientSanctuaryType");

            for (Type type : values()) {
                if (type.ordinal() == id) {
                    return type;
                }
            }

            return NORMAL;
        }

        public static List<Type> getTypesList() {
            return Arrays.asList(values());
        }
    }
}
