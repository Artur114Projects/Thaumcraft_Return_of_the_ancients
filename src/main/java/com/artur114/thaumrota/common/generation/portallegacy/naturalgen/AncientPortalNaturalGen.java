package com.artur114.thaumrota.common.generation.portallegacy.naturalgen;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.bananalib.mc.nbt.IReadFromNBT;
import com.artur114.bananalib.mc.nbt.IWriteToNBT;
import com.artur114.thaumrota.common.worldstate.blockprotect.BlockProtectHandler;
import com.artur114.thaumrota.common.util.TeleportHandler;
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager;
import com.artur114.thaumrota.common.generation.portallegacy.base.AncientPortal;
import com.artur114.thaumrota.common.generation.portallegacy.base.AncientPortalsProcessor;
import com.artur114.thaumrota.common.generation.portallegacy.generators.GenAncientArch;
import com.artur114.thaumrota.common.generation.portallegacy.generators.GenAncientSpire;
import com.artur114.thaumrota.common.generation.portallegacy.util.PortalOffsets;
import com.artur114.thaumrota.common.util.TerrainAnalyzer;

import java.util.*;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.blocks.BlocksTC;


// TODO: 22.02.2025 Доделать структуры
// TODO: 22.02.2025 Сделать сломанные контролеры святилища
// TODO: 25.02.2025 Сделать исследования
// TODO: переписать!
public class AncientPortalNaturalGen extends AncientPortal {
    private static final Logger log = LogManager.getLogger("ThaumRotA/PortalsLegacy");
    private static final int portalSize = 16;

    private List<AncientSanctuary> sanctuaries;
    private final PortalGenManager genManager;
    private final LightManager lightManager;
    private boolean isActive = false;

    public AncientPortalNaturalGen(MinecraftServer server, int dimension, int chunkX, int chunkZ) {
        super(server, dimension, chunkX, chunkZ, 0, AncientPortalsProcessor.getFreeId());

        this.genManager = new PortalGenManager(this, portalSize, 8);

        this.posY = genManager.getPortalY();

        this.lightManager = new LightManager(this);
    }

    public AncientPortalNaturalGen(MinecraftServer server, NBTTagCompound nbt) {
        super(server, nbt);

        this.genManager = new PortalGenManager(this, portalSize, 8);
        this.isActive = nbt.getBoolean("isActive");

        if (isGenerated()) {
            try {
                this.sanctuaries = IReadFromNBT.instantiateNBTList(nbt.getTagList("sanctuaries", 10), AncientSanctuary::new);
                for (AncientSanctuary sanctuary : this.sanctuaries) sanctuary.bindPortal(this);
            } catch (Exception e) {
                log.warn("It was not possible to download the portal of the ID:{} disruption...", this.id, e);
                this.explosion();
            }
        }

        this.lightManager = new LightManager(this);
    }

    public AncientSanctuary getNotBrokenSanctuary() {
        if (sanctuaries == null) {
            return null;
        }

        for (AncientSanctuary sanctuary : this.sanctuaries) {
            if (!sanctuary.getType().isBroken()) {
                return sanctuary;
            }
        }

        return null;
    }

    protected void updateActiveState(boolean newState) {
        if (this.isActive == newState) {
            return;
        }

        this.setCapState(newState);
        this.lightManager.setLightState(newState);

        this.isActive = newState;
        this.requestToUpdateOnClient();
    }

    private void setCapState(boolean state) {
        PosMc3IM blockPos = PosMc3IM.obtain();

        blockPos.setChunk(portalPos).setY(posY);

        for (BlockPos off : PortalOffsets.portalCollideOffsetsArray) {
            blockPos.pushPos();
            blockPos.add(off);
            world.setBlockState(blockPos, state ? Blocks.AIR.getDefaultState() : BlocksTC.stoneEldritchTile.getDefaultState());
            BlockProtectHandler.setProtectState(world, blockPos, !state);
            blockPos.popPos();
        }

        PosMc3IM.release(blockPos);
    }

    @Override
    protected void onChunkPopulatePre(int chunkX, int chunkZ) {
        if (this.isOnPortalRange(chunkX, chunkZ)) {
            this.setGenerated();
            this.build();

            this.sanctuaries = genManager.sanctuaries();

            for (AncientSanctuary sanctuary : sanctuaries) {
                sanctuary.bindPortal(this);
            }
        }
    }

    @Override
    public boolean isOnPortalRange(int chunkX, int chunkZ) {
        return Math.abs(this.chunkX - chunkX) <= portalSize && Math.abs(this.chunkZ - chunkZ) <= portalSize;
    }

    @Override
    public boolean isOnPortalRange(BlockPos pos) {
        return this.isOnPortalRange(pos.getX() >> 4, pos.getZ() >> 4);
    }

    @Override
    public boolean isLoaded() {
        return this.isGenerated() && this.isAnyChunkOnRangeLoaded(portalSize);
    }

    @Override
    public void build() {
        this.genManager.generate();
    }

    @Override
    public void teleportToOverworld(EntityPlayerMP player) {
        if (this.isActive) {
            super.teleportToOverworld(player);
        } else {
            TeleportHandler.teleportToDimension(player, 0, new BlockPos(0, 100, 0));
        }
    }

    @Override
    public @Nullable NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        if (nbt == null) {
            return null;
        }
        if (isGenerated()) {
            nbt.setTag("sanctuaries", IWriteToNBT.writeToNBTList(this.sanctuaries));
        }
        nbt.setBoolean("isActive", this.isActive);
        return nbt;
    }

    @Override
    public @Nullable NBTTagCompound createClientUpdateNBT() {
        NBTTagCompound nbt = super.createClientUpdateNBT();
        if (nbt == null) {
            return null;
        }
        nbt.setBoolean("isActive", this.isActive);
        return nbt;
    }

    @Override
    protected int getPortalTypeID() {
        return 0;
    }

    private boolean isAnyChunkOnRangeLoaded(int range) {
        for (int x = this.chunkX - range; x != this.chunkX + range + 1; x++) {
            for (int z = this.chunkZ - range; z != this.chunkZ + range + 1; z++) {
                if (isChunkLoaded(x, z)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isChunkLoaded(int chunkX, int chunkZ) {
        return ((WorldServer) this.world).getChunkProvider().chunkExists(chunkX, chunkZ);
    }

    private static class PortalGenManager {
        private AncientSanctuary.Type[] sanctuaryTypes = new AncientSanctuary.Type[0];
        private final AncientSanctuary[] sanctuaries = new AncientSanctuary[4];
        private final BlockPos[] sanctuaryPosForArch = new BlockPos[4];
        private final GenAncientSpire genSpire = new GenAncientSpire();
        private final GenAncientArch genArch = new GenAncientArch();
        private final BlockPos[] basePosForArch = new BlockPos[4];
        private final PosMc3IM blockPos = new PosMc3IM();
        private final AncientPortalNaturalGen portal;
        private final int minSanctuaryGenRange;
        private final TerrainAnalyzer analyzer;
        private BlockPos[] sanctuaryPillars;
        private final int genRange;
        private final Random rand;
        private final World world;
        private final int chunkX;
        private final int chunkZ;

        protected PortalGenManager(AncientPortalNaturalGen portal, int genRange, int minSanctuaryGenRange) {
            this.rand = new Random(portal.world.getSeed() + portal.chunkX + portal.chunkZ);
            this.minSanctuaryGenRange = minSanctuaryGenRange;
            this.chunkX = portal.chunkX;
            this.chunkZ = portal.chunkZ;
            this.world = portal.world;
            this.genRange = genRange;
            this.portal = portal;

            this.analyzer = new TerrainAnalyzer(world);

            this.initOffsets();
        }

        protected void generate() {
            log.info("Portal [{}, {}] start generate", chunkX, chunkZ);
            this.genBase();
            this.genSanctuary();
            this.genArches();
            log.info("Portal [{}, {}] is successfully generated", chunkX, chunkZ);
        }

        private void genBase() {
            blockPos.setChunk(chunkX, chunkZ);
            analyzer.startAnalyzing(portal.portalPos);

            blockPos.setY(analyzer.getAverageHeight());

            portal.genAncientPortal(true);
            StructuresBuildManager.createBuildRequest(world, blockPos, "ancient_portal_hub").setNeedProtect().build();
            genSpire.generate(world, portal.portalPos, 255);

            blockPos.pushPos();
            basePosForArch[0] = blockPos.add(0, 0, 7).setY(BananaMC.findHighestBlock(this.world, blockPos)).addY(-1).toImmutable();
            blockPos.popPos();

            blockPos.pushPos();
            basePosForArch[1] = blockPos.add(7, 0, 0).setY(BananaMC.findHighestBlock(this.world, blockPos)).addY(-1).toImmutable();
            blockPos.popPos();

            blockPos.pushPos();
            basePosForArch[2] = blockPos.add(15, 0, 7).setY(BananaMC.findHighestBlock(this.world, blockPos)).addY(-1).toImmutable();
            blockPos.popPos();

            blockPos.pushPos();
            basePosForArch[3] = blockPos.add(7, 0, 15).setY(BananaMC.findHighestBlock(this.world, blockPos)).addY(-1).toImmutable();
            blockPos.popPos();
        }

        private void genSanctuary() {
            EnumFacing[] facings = new EnumFacing[] {
                    EnumFacing.WEST,
                    EnumFacing.NORTH,
                    EnumFacing.EAST,
                    EnumFacing.SOUTH,
            };

            this.fillSanctuaryTypes();

            for (int i = 0; i != facings.length; i++) {
                EnumFacing facing = facings[i];
                ChunkPos start = portal.portalPos;

                ChunkPos min = new ChunkPos(start.x + (minSanctuaryGenRange * facing.getFrontOffsetX()), start.z + (minSanctuaryGenRange * facing.getFrontOffsetZ()));
                ChunkPos end = new ChunkPos(start.x + (genRange * facing.getFrontOffsetX()), start.z + (genRange * facing.getFrontOffsetZ()));

                analyzer.startAnalyzing(min, end);

                int lastFlow = Integer.MAX_VALUE;
                int index = -1;

                for (int j = genRange - minSanctuaryGenRange; j > 0; j--) {
                    int x = (16 * (j - 1)) * Math.abs(facing.getFrontOffsetX());
                    int z = (16 * (j - 1)) * Math.abs(facing.getFrontOffsetZ());

                    int x1 = (16 * j) * Math.abs(facing.getFrontOffsetX());
                    int z1 = (16 * j) * Math.abs(facing.getFrontOffsetZ());

                    if (facing.getAxis() == EnumFacing.Axis.X) {
                        z1 = 16;
                    } else if (facing.getAxis() == EnumFacing.Axis.Z) {
                        x1 = 16;
                    }

                    int flow = analyzer.getTerrainFlow(x, z, x1, z1);

                    if (flow < lastFlow) {
                        lastFlow = flow;
                        index = j;
                    }
                }

                ChunkPos res = new ChunkPos(min.x + index * facing.getFrontOffsetX(), min.z + index * facing.getFrontOffsetZ());

                this.genSanctuaryStructure(res, facing, i);
            }
        }

        private void genArches() {
            for (int i = 0; i != basePosForArch.length; i++) {
                BlockPos start = basePosForArch[i];
                BlockPos end = sanctuaryPosForArch[i];

                genArch.generate(world, start, end, EnumFacing.AxisDirection.POSITIVE, sanctuaryTypes[i].isBrokenArch());
            }
        }

        private void genSanctuaryStructure(ChunkPos pos, EnumFacing facing, int id) {
            analyzer.startAnalyzing(pos);
            AncientSanctuary.Type type = sanctuaryTypes[id];

            AncientSanctuary sanctuary = new AncientSanctuary(world, pos, type);

            sanctuaryPosForArch[id] = sanctuary.generate(analyzer, facing.getOpposite(), sanctuaryPillars);
            sanctuaries[id] = sanctuary;
        }

        private void fillSanctuaryTypes() {
            List<AncientSanctuary.Type> types = AncientSanctuary.Type.getTypesList();
            Collections.shuffle(types, rand);
            sanctuaryTypes = types.toArray(new AncientSanctuary.Type[0]);
        }

        protected int getPortalY() {
            analyzer.startAnalyzing(portal.portalPos);

            return analyzer.getAverageHeight();
        }

        protected List<AncientSanctuary> sanctuaries() {
            return new ArrayList<>(Arrays.asList(this.sanctuaries));
        }

        private void initOffsets() {
            sanctuaryPillars = new BlockPos[] {
                    new BlockPos(2, 0, 4),
                    new BlockPos(4, 0, 2),

                    new BlockPos(10, 0, 2),
                    new BlockPos(12, 0, 4),

                    new BlockPos(2, 0, 10),
                    new BlockPos(4, 0, 12),

                    new BlockPos(10, 0, 12),
                    new BlockPos(12, 0, 10),
            };
        }
    }

    public static class LightManager {
        private final Block nitorBlock = BlocksTC.nitor.get(EnumDyeColor.BLACK);
        private final ChunkPos portalPos;
        private final World world;
        private final int portalY;

        private LightManager(AncientPortalNaturalGen portal) {
            this.portalPos = portal.portalPos;
            this.portalY = portal.posY;
            this.world = portal.world;
        }

        private void setLightState(boolean state) {
            if (state) {
                this.enableLight();
            } else {
                this.disableLight();
            }
        }

        public void disableLight() {
            this.callRunnableInLightOffsets((pos) -> {
                if (world.getBlockState(pos).getBlock() == nitorBlock) {
                    world.setBlockToAir(pos);
                    BlockProtectHandler.unProtect(world, pos);
                }
            });
        }

        public void enableLight() {
            this.callRunnableInLightOffsets((pos) -> {
                if (world.isAirBlock(pos)) {
                    world.setBlockState(pos, nitorBlock.getDefaultState());
                    BlockProtectHandler.protect(world, pos);
                }
            });
        }

        private void callRunnableInLightOffsets(Consumer<PosMc3IM> run) {
            PosMc3IM blockPos = PosMc3IM.obtain();

            blockPos.setX(portalPos.x << 4).setZ(portalPos.z << 4).setY(portalY);

            this.center2Offsets(blockPos, run);
            this.inside4CornerOffsets(blockPos, run);
            this.outside4CornerOffsets(blockPos, run);
            this.base24LightOffsets(blockPos, run);

            PosMc3IM.release(blockPos);
        }

        private void center2Offsets(PosMc3IM pos, Consumer<PosMc3IM> run) {
            pos.pushPos();
            run.accept(pos.add(7, 10, 8));
            run.accept(pos.add(1, 0, -1));
            pos.popPos();
        }

        private void inside4CornerOffsets(PosMc3IM pos, Consumer<PosMc3IM> run) {
            pos.pushPos();
            BlockPos[] lightOffsets = PortalOffsets.getCornerOffsets(2, 13);
            for (int i = 0; i != 2; i++) {
                int y;
                if (i == 0) {
                    y = portalY + 1;
                } else {
                    y = portalY + 10;
                }
                pos.setY(y);
                for (BlockPos off : lightOffsets) {
                    pos.pushPos();
                    pos.add(off);
                    run.accept(pos);
                    pos.popPos();
                }
            }
            pos.popPos();
        }

        private void outside4CornerOffsets(PosMc3IM pos, Consumer<PosMc3IM> run) {
            pos.pushPos().addY(11);
            for (BlockPos off : PortalOffsets.getCornerOffsets(0, 15)) {
                pos.pushPos();
                pos.add(off);
                run.accept(pos);
                pos.popPos();
            }
            pos.popPos();
        }

        private void base24LightOffsets(PosMc3IM pos, Consumer<PosMc3IM> run) {
            pos.pushPos();
            for (int i = 0; i != 3; i++) {
                int range;
                int y;

                switch (i) {
                    case 0:
                        y = portalY + 4;
                        range = 0;
                        break;
                    case 1:
                        y = portalY + 13;
                        range = 4;
                        break;
                    default:
                        y = portalY + 14;
                        range = 3;
                        break;
                }
                pos.setY(y);
                for (BlockPos off : PortalOffsets.portalLightOffsets[range]) {
                    pos.pushPos();
                    pos.add(off);
                    run.accept(pos);
                    pos.popPos();
                }
            }
            pos.popPos();
        }
    }
}
