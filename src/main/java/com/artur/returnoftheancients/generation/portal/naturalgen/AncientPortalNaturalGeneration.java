package com.artur.returnoftheancients.generation.portal.naturalgen;

import com.artur.returnoftheancients.blockprotect.BlockProtectHandler;
import com.artur.returnoftheancients.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.generation.portal.base.AncientPortal;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.generation.portal.generators.GenAncientArch;
import com.artur.returnoftheancients.generation.portal.generators.GenAncientSpire;
import com.artur.returnoftheancients.generation.portal.util.OffsetsUtil;
import com.artur.returnoftheancients.util.ArrayListWriteToNBT;
import com.artur.returnoftheancients.util.TerrainAnalyzer;
import com.artur.returnoftheancients.util.context.MethodContext;
import com.artur.returnoftheancients.util.context.MethodParams4;
import com.artur.returnoftheancients.util.interfaces.RunnableWithParam;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.blocks.BlocksTC;

import java.util.Collections;
import java.util.List;
import java.util.Random;


// TODO: 22.02.2025 Доделать структуры
// TODO: 22.02.2025 Сделать сломанные контролеры святилища
// TODO: 25.02.2025 Сделать исследования
public class AncientPortalNaturalGeneration extends AncientPortal {
    private static final int portalSize = 16;

    private ArrayListWriteToNBT<AncientSanctuary> sanctuaries;
    private final PortalGenManager genManager;
    private final LightManager lightManager;
    private boolean isActive = false;

    public AncientPortalNaturalGeneration(MinecraftServer server, int dimension, int chunkX, int chunkZ) {
        super(server, dimension, chunkX, chunkZ, 0, AncientPortalsProcessor.getFreeId());

        this.genManager = new PortalGenManager(this, portalSize, 8);

        this.posY = genManager.getPortalY();

        this.lightManager = new LightManager(this);
    }

    public AncientPortalNaturalGeneration(MinecraftServer server, NBTTagCompound nbt) {
        super(server, nbt);

        this.genManager = new PortalGenManager(this, portalSize, 8);
        this.isActive = nbt.getBoolean("isActive");

        if (isGenerated()) {
            sanctuaries = new ArrayListWriteToNBT<>(AncientSanctuary.class);
            try {
                sanctuaries.readFromNBT(nbt.getCompoundTag("sanctuaries"));
                for (AncientSanctuary sanctuary : sanctuaries) sanctuary.bindPortal(this);
            } catch (Exception e) {
                System.out.println("It was not possible to download the portal of the ID:" + id + " disruption...");
                e.printStackTrace(System.err);
                this.explosion();
            }
        }

        this.lightManager = new LightManager(this);
    }

    public AncientSanctuary getNotBrokenSanctuary() {
        if (sanctuaries == null) {
            return null;
        }

        for (AncientSanctuary sanctuary : sanctuaries) {
            if (!sanctuary.getType().isBroken()) {
                return sanctuary;
            }
        }

        return null;
    }

    protected void updateActiveState(boolean newState) {
        if (isActive == newState) {
            return;
        }

        this.setCapState(newState);
        this.lightManager.setLightState(newState);

        this.isActive = newState;
        this.requestToUpdateOnClient();
    }

    private void setCapState(boolean state) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();

        blockPos.setPos(portalPos).setY(posY);
        blockPos.offsetAndCallRunnable(OffsetsUtil.portalCollideOffsetsArray, (pos) -> {
            world.setBlockState(pos, state ? Blocks.AIR.getDefaultState() : BlocksTC.stoneEldritchTile.getDefaultState());
            BlockProtectHandler.setProtectState(world, pos, !state);
        });

        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
    }

    @Override
    protected void onChunkPopulatePre(int chunkX, int chunkZ) {
        if (this.isOnPortalRange(chunkX, chunkZ)) {
            this.setGenerated();
            this.build();

            this.sanctuaries = genManager.getSanctuaries();

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
    public @Nullable NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        if (nbt == null) {
            return null;
        }
        if (isGenerated()) {
            nbt.setTag("sanctuaries", sanctuaries.writeToNBT(null));
        }
        nbt.setBoolean("isActive", isActive);
        return nbt;
    }

    @Override
    public @Nullable NBTTagCompound createClientUpdateNBT() {
        NBTTagCompound nbt = super.createClientUpdateNBT();
        if (nbt == null) {
            return null;
        }
        nbt.setBoolean("isActive", isActive);
        return nbt;
    }

    @Override
    protected int getPortalTypeID() {
        return 0;
    }

    private boolean isAnyChunkOnRangeLoaded(int range) {
        for (int x = chunkX - range; x != chunkX + range + 1; x++) {
            for (int z = chunkZ - range; z != chunkZ + range + 1; z++) {
                if (isChunkLoaded(x, z)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isChunkLoaded(int chunkX, int chunkZ) {
        return ((WorldServer) world).getChunkProvider().chunkExists(chunkX, chunkZ);
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

            BlockProtectHandler.protectAllBlocksOnChunk(world, portal.portalPos, BlocksTC.stoneEldritchTile, BlocksTC.stoneAncient, BlocksTC.nitor.get(EnumDyeColor.BLACK));
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
                                index = j + 1;
                            }
                        }
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

        protected ArrayListWriteToNBT<AncientSanctuary> getSanctuaries() {
            ArrayListWriteToNBT<AncientSanctuary> list = new ArrayListWriteToNBT<>(AncientSanctuary.class);
            list.addAll(sanctuaries);
            return list;
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

        private LightManager(AncientPortalNaturalGeneration portal) {
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

        private void callRunnableInLightOffsets(RunnableWithParam<UltraMutableBlockPos> run) {
            UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();

            blockPos.setPos(portalPos).setY(portalY);

            this.center2Offsets(blockPos, run);
            this.inside4CornerOffsets(blockPos, run);
            this.outside4CornerOffsets(blockPos, run);
            this.base24LightOffsets(blockPos, run);

            UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
        }

        private void center2Offsets(UltraMutableBlockPos pos, RunnableWithParam<UltraMutableBlockPos> run) {
            pos.pushPos();
            run.run(pos.add(7, 10, 8));
            run.run(pos.add(1, 0, -1));
            pos.popPos();
        }

        private void inside4CornerOffsets(UltraMutableBlockPos pos, RunnableWithParam<UltraMutableBlockPos> run) {
            pos.pushPos();
            BlockPos[] lightOffsets = OffsetsUtil.getCornerOffsets(2, 13);
            for (int i = 0; i != 2; i++) {
                int y;
                if (i == 0) {
                    y = portalY + 1;
                } else {
                    y = portalY + 10;
                }
                pos.setY(y);

                pos.offsetAndCallRunnable(lightOffsets, run);
            }
            pos.popPos();
        }

        private void outside4CornerOffsets(UltraMutableBlockPos pos, RunnableWithParam<UltraMutableBlockPos> run) {
            pos.pushPos();
            pos.addY(11).offsetAndCallRunnable(OffsetsUtil.getCornerOffsets(0, 15), run);
            pos.popPos();
        }

        private void base24LightOffsets(UltraMutableBlockPos pos, RunnableWithParam<UltraMutableBlockPos> run) {
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
                pos.offsetAndCallRunnable(OffsetsUtil.portalLightOffsets[range], run);
            }
            pos.popPos();
        }
    }
}
