package com.artur.returnoftheancients.generation.portal.naturalgen;

import com.artur.returnoftheancients.blockprotect.BlockProtectHandler;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.structurebuilder.BuildRequest;
import com.artur.returnoftheancients.structurebuilder.StructuresBuildManager;
import com.artur.returnoftheancients.generation.portal.util.OffsetsUtil;
import com.artur.returnoftheancients.events.ServerEventsHandler;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.tileentity.TileEntityAncientSanctuaryController;
import com.artur.returnoftheancients.util.TerrainAnalyzer;
import com.artur.returnoftheancients.util.interfaces.IIsNeedWriteToNBT;
import com.artur.returnoftheancients.util.interfaces.IWriteToNBT;
import com.artur.returnoftheancients.util.interfaces.RunnableWithParam;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
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
import thaumcraft.api.blocks.BlocksTC;

import java.util.Arrays;
import java.util.List;

public class AncientSanctuary implements IIsNeedWriteToNBT {
    private final Block nitorBlock = BlocksTC.nitor.get(EnumDyeColor.BLACK);
    private AncientPortalNaturalGeneration portal = null;
    private TileEntityAncientSanctuaryController tile;
    private boolean isBuild = false;
    private boolean needSave = true;
    private boolean active = false;
    private final ChunkPos pos;
    private final World world;
    private BlockPos tilePos;
    private final Type type;

    protected AncientSanctuary(World world, ChunkPos pos, Type type) {
        this.world = world;
        this.type = type;
        this.pos = pos;
    }

    protected AncientSanctuary(NBTTagCompound nbt) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        this.world = server.getWorld(nbt.getInteger("dimension"));
        this.type = Type.getTypeFromNBT(nbt);
        this.pos = new ChunkPos(nbt.getInteger("posX"), nbt.getInteger("posZ"));
        this.isBuild = nbt.getBoolean("isBuild");

        if (!type.isBroken()) {
            this.bindTile(BlockPos.fromLong(nbt.getLong("tilePos")));
            this.active = nbt.getBoolean("active");
        }
    }

    protected BlockPos generate(TerrainAnalyzer analyzer, EnumFacing archFacing, BlockPos[] sanctuaryPillars) {
        if (isBuild) {
            return this.getSanctuaryArchOffset(archFacing);
        }

        UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();

        blockPos.setPos(pos).setY(analyzer.getMaxHeight());
        this.type.createBuildRequest(world, blockPos.addY(2)).build();

        if (!type.isBroken()) {
            blockPos.pushPos();
            this.bindTile(blockPos.add(7, 2, 7).toImmutable());
            this.updateActiveState(this.tile.isDone());
            blockPos.popPos();
        }

        blockPos.addY(-1);

        for (BlockPos offset : sanctuaryPillars) {
            blockPos.pushPos();

            for (blockPos.add(offset); (world.isAirBlock(blockPos) || world.getBlockState(blockPos).getMaterial().isLiquid()); blockPos.down()) {
                world.setBlockState(blockPos, BlocksTC.stoneEldritchTile.getDefaultState());
                BlockProtectHandler.protect(world, blockPos);
            }

            blockPos.popPos();
        }

        BlockPos offset = getSanctuaryArchOffset(archFacing);
        BlockPos ret = blockPos.add(offset).setWorldY(world).addY(-1).toImmutable();

        UltraMutableBlockPos.release(blockPos);

        this.needSave = true;
        this.isBuild = true;

        if (TRAConfigs.Any.debugMode) System.out.println("Generated new ancient sanctuary pos:" + pos);

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

        TileEntity tileRaw = world.getTileEntity(pos);

        if (tileRaw instanceof TileEntityAncientSanctuaryController) {
            this.tile = (TileEntityAncientSanctuaryController) tileRaw;
            this.tile.bindSanctuary(this);
        } else {
            this.world.setBlockState(pos, InitBlocks.ANCIENT_SANCTUARY_CONTROLLER.getDefaultState());
            this.bindTile(pos);
        }
    }

    protected void bindPortal(AncientPortalNaturalGeneration portal) {
        this.portal = portal;
        if (!type.isBroken()) {
            this.portal.updateActiveState(active);
        }
    }

    public void onTileLoad(boolean state, BlockPos pos) {
        if (!pos.equals(tilePos) || type.isBroken()) {
            return;
        }
        this.bindTile(tilePos);
        this.updateActiveState(state);
    }

    public void onTileStateChanged(boolean state) {
        ServerEventsHandler.TIMER_TASKS_MANAGER.addTask(29, () -> {
            ServerEventsHandler.SHORT_CHUNK_LOAD_MANAGER.loadArea(world, portal.portalPos, 1);
            ServerEventsHandler.SHORT_CHUNK_LOAD_MANAGER.loadArea(world, pos, 1);
            ServerEventsHandler.TIMER_TASKS_MANAGER.addTask(1, () -> {
                this.updateActiveState(state);
                this.portal.updateActiveState(state);

                UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
                this.world.playSound(null, blockPos.setPos(tilePos).add(0, 2, 0), InitSounds.SPOTLIGHT.SOUND, SoundCategory.AMBIENT, 1, 1);

                blockPos.setPos(portal.portalPos).setY(portal.posY + 10);
                blockPos.offsetAndCallRunnable(OffsetsUtil.getCornerOffsets(2, 13), pos -> this.world.playSound(null, pos, InitSounds.SPOTLIGHT.SOUND, SoundCategory.AMBIENT, 1, 1));

                UltraMutableBlockPos.release(blockPos);
            });
        });
    }

    protected void updateActiveState(boolean newState) {
        if (newState == active || type.isBroken() || tilePos == null) {
            return;
        }

        this.setLightState(newState);

        this.active = newState;
        this.needSave = true;
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

    private void callRunnableOnLightOffsets(RunnableWithParam<UltraMutableBlockPos> run) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();

        blockPos.setPos(tilePos).addY(3);
        run.run(blockPos);
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            blockPos.pushPos();
            blockPos.offset(facing, 3);
            run.run(blockPos);
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

        blockPos.setPos(pos);
        for (int i = 0; i != 2; i++) {
            int y;
            if (i == 0) {
                y = tilePos.getY() - 1;
            } else {
                y = tilePos.getY() + 2;
            }
            blockPos.setY(y);
            blockPos.offsetAndCallRunnable(backLights, run);
        }

        UltraMutableBlockPos.release(blockPos);
    }

    protected Type getType() {
        return type;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        needSave = false;
        type.writeToNBT(nbt);
        if (!type.isBroken() && tilePos != null) {
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
    public boolean isNeedWriteToNBT() {
        return needSave;
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
        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
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
