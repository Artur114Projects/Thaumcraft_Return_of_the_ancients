package com.artur.returnoftheancients.generation.portal;

import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.generation.portal.base.AncientPortal;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.generation.portal.generators.GenAncientArch;
import com.artur.returnoftheancients.generation.portal.generators.GenAncientSpire;
import com.artur.returnoftheancients.util.ArrayListWriteToNBT;
import com.artur.returnoftheancients.util.TerrainAnalyzer;
import com.artur.returnoftheancients.util.context.MethodContext;
import com.artur.returnoftheancients.util.context.MethodParams4;
import com.artur.returnoftheancients.util.interfaces.IIsNeedWriteToNBT;
import com.artur.returnoftheancients.util.interfaces.IWriteToNBT;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.blocks.BlocksTC;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AncientPortalNaturalGeneration extends AncientPortal {
    private ArrayListWriteToNBT<AncientSanctuary> sanctuaries;
    private final PortalGenManager genManager;

    public AncientPortalNaturalGeneration(MinecraftServer server, int dimension, int chunkX, int chunkZ) {
        super(server, dimension, chunkX, chunkZ, 0, AncientPortalsProcessor.getFreeId());

        this.genManager = new PortalGenManager(this, 16, 8);

        this.posY = genManager.getPortalY();
    }

    public AncientPortalNaturalGeneration(MinecraftServer server, NBTTagCompound nbt) {
        super(server, nbt);

        this.genManager = new PortalGenManager(this, 16, 8);
    }

    @Override
    protected void onChunkPopulatePre(int chunkX, int chunkZ) {
        if (this.isOnPortalRange(chunkX, chunkZ)) {
            this.setGenerated();
            this.build();

            this.sanctuaries = genManager.getSanctuaries();
        }
    }

    @Override
    public boolean isOnPortalRange(int chunkX, int chunkZ) {
        return this.genManager.isOnRange(chunkX, chunkZ);
    }

    @Override
    public boolean isLoaded() {
        return this.genManager.isAnyChunkOnRangeLoaded();
    }

    @Override
    public void build() {
        this.genManager.generate();
    }

    @Override
    public @Nullable NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        if (nbt == null) {
            return null;
        }
        if (isGenerated()) {
            nbt.setTag("sanctuaries", sanctuaries.writeToNBT(null));
        }
        return nbt;
    }

    @Override
    protected int getPortalTypeID() {
        return 0;
    }

    private static class PortalGenManager {
        private AncientSanctuary.Type[] sanctuaryTypes = new AncientSanctuary.Type[0];
        private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();
        private final AncientSanctuary[] sanctuaries = new AncientSanctuary[4];
        private final BlockPos[] sanctuaryPosForArch = new BlockPos[4];
        private final GenAncientSpire genSpire = new GenAncientSpire();
        private final GenAncientArch genArch = new GenAncientArch();
        private final BlockPos[] basePosForArch = new BlockPos[4];
        private final AncientPortalNaturalGeneration portal;
        private final int minSanctuaryGenRange;
        private final TerrainAnalyzer analyzer;
        private BlockPos[] sanctuaryPillars;
        private final int genRange;
        private final Random rand;
        private final World world;
        private final int chunkX;
        private final int chunkZ;

        protected PortalGenManager(AncientPortalNaturalGeneration portal, int genRange, int minSanctuaryGenRange) {
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
            this.genBase();
            this.genSanctuary();
            this.genArches();

            System.out.println("Portal id:" + portal.id + " is successfully generated!");
        }

        private void genBase() {
            blockPos.setPos(chunkX, chunkZ);
            analyzer.startAnalyzing(portal.portalPos);

            blockPos.setY(analyzer.getAverageHeight());

            portal.genAncientPortal();
            CustomGenStructure.gen(world, blockPos, "ancient_portal_hub");
            genSpire.generate(world, portal.portalPos, 255);

            blockPos.pushPos();
            basePosForArch[0] = blockPos.add(0, 0, 7).setWorldY(world).addY(-1).toImmutable();
            blockPos.popPos();

            blockPos.pushPos();
            basePosForArch[1] = blockPos.add(7, 0, 0).setWorldY(world).addY(-1).toImmutable();
            blockPos.popPos();

            blockPos.pushPos();
            basePosForArch[2] = blockPos.add(15, 0, 7).setWorldY(world).addY(-1).toImmutable();
            blockPos.popPos();

            blockPos.pushPos();
            basePosForArch[3] = blockPos.add(7, 0, 15).setWorldY(world).addY(-1).toImmutable();
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

                ChunkPos min = new ChunkPos(start.x + minSanctuaryGenRange * facing.getFrontOffsetX(), start.z + minSanctuaryGenRange * facing.getFrontOffsetZ());
                ChunkPos end = new ChunkPos(start.x + genRange * facing.getFrontOffsetX(), start.z + genRange * facing.getFrontOffsetZ());

                analyzer.startAnalyzing(min, end);

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

                    int flow;

                    flow = analyzer.getTerrainFlow(x, z, x1, z1);

                    if (flow < 256) {
                        index = j;
                        break;
                    }
                }

                if (index == -1) {
                    MethodContext<Integer, MethodParams4<Integer, Integer, Integer, Integer>> context = analyzer.get4IntRetIntMethodContext("getTerrainFlow");
                    if (context != null) {
                        List<Integer> ret = context.getAllReturns();
                        int minFlow = ret.get(0);
                        for (int j = 0; j != genRange - minSanctuaryGenRange; j++) {
                            if (minFlow > ret.get(j)) {
                                minFlow = ret.get(j);
                                index = j;
                            }
                        }
                    }
                }

                ChunkPos res = new ChunkPos(end.x - index * Math.abs(facing.getFrontOffsetX()), end.z - index * Math.abs(facing.getFrontOffsetZ()));

                this.genSanctuaryStructure(res, facing, i);
            }
        }

        private void genArches() {
            for (int i = 0; i != basePosForArch.length; i++) {
                BlockPos start = basePosForArch[i];
                BlockPos end = sanctuaryPosForArch[i];

                genArch.generate(world, start, end, EnumFacing.AxisDirection.POSITIVE, sanctuaryTypes[i].isBroken());
            }
        }

        private void genSanctuaryStructure(ChunkPos pos, EnumFacing facing, int id) {
            analyzer.startAnalyzing(pos);
            AncientSanctuary.Type type = sanctuaryTypes[id];

            AncientSanctuary sanctuary = new AncientSanctuary(world, facing.getOpposite(), pos, type);

            sanctuaryPosForArch[id] = sanctuary.generate(analyzer, sanctuaryPillars);
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

        protected ArrayListWriteToNBT<AncientSanctuary> getSanctuaries() {
            ArrayListWriteToNBT<AncientSanctuary> list = new ArrayListWriteToNBT<>();
            list.addAll(sanctuaries);
            return list;
        }

        protected boolean isOnRange(int chunkX, int chunkZ) {
            return Math.abs(this.chunkX - chunkX) <= genRange && Math.abs(this.chunkZ - chunkZ) <= genRange;
        }

        protected boolean isAnyChunkOnRangeLoaded() {
            for (int x = chunkX - genRange; x != chunkX + genRange + 1; x++) {
                for (int z = chunkZ - genRange; z != chunkZ + genRange + 1; z++) {
                    if (isChunkLoaded(x, z)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean isChunkLoaded(int chunkX, int chunkZ) {
            UltraMutableBlockPos blockPos0 = UltraMutableBlockPos.getBlockPosFromPoll();
            UltraMutableBlockPos blockPos1 = UltraMutableBlockPos.getBlockPosFromPoll();

            boolean flag = world.isAreaLoaded(blockPos0.setPos(chunkX, chunkZ), blockPos1.setPos(chunkX, chunkZ).add(16, 0, 16));

            UltraMutableBlockPos.returnBlockPosToPoll(blockPos1);
            UltraMutableBlockPos.returnBlockPosToPoll(blockPos0);
            return flag;
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

    public static class AncientSanctuary implements IIsNeedWriteToNBT {
        private final EnumFacing archFacing;
        private final ChunkPos pos;
        private final World world;
        private final Type type;

        protected AncientSanctuary(World world, EnumFacing archFacing, ChunkPos pos, Type type) {
            this.archFacing = archFacing;
            this.world = world;
            this.type = type;
            this.pos = pos;
        }

        protected BlockPos generate(TerrainAnalyzer analyzer, BlockPos[] sanctuaryPillars) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();

            blockPos.setPos(pos).setY(analyzer.getMaxHeight());
            CustomGenStructure.gen(world, blockPos.addY(2), type.getStructureName());
            blockPos.addY(-1);

            for (BlockPos offset : sanctuaryPillars) {
                blockPos.pushPos();

                for (blockPos.add(offset); (world.isAirBlock(blockPos) || world.getBlockState(blockPos).getMaterial().isLiquid()); blockPos.down()) {
                    world.setBlockState(blockPos, BlocksTC.stoneEldritchTile.getDefaultState());
                }

                blockPos.popPos();
            }

            BlockPos offset = getSanctuaryArchOffset(archFacing);
            BlockPos ret = blockPos.add(offset).setWorldY(world).addY(-1).toImmutable();

            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);

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

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            return null;
        }

        @Override
        public boolean isNeedWriteToNBT() {
            return false;
        }

        protected enum Type implements IWriteToNBT {
            NORMAL("ancient_sanctuary", 0, false),
            BROKEN("ancient_sanctuary", 1, true),
            CULTIST("ancient_sanctuary", 2, true),
            SCORCHED("ancient_sanctuary", 3, true);

            private final String structureName;
            private final boolean isBroken;
            private final int id;
            Type(String structureName, int id, boolean isBroken) {
                this.structureName = structureName;
                this.isBroken = isBroken;
                this.id = id;
            }
            public String getStructureName() {
                return structureName;
            }

            public boolean isBroken() {
                return isBroken;
            }

            @Override
            public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
                nbt.setInteger("AncientSanctuaryType", id);
                return nbt;
            }

            public static Type getTypeFromNBT(NBTTagCompound nbt) {
                int id = nbt.getInteger("AncientSanctuaryType");

                for (Type type : values()) {
                    if (type.id == id) {
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
}
