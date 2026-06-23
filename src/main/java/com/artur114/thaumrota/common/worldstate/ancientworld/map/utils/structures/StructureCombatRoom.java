package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.bananalib.math.m3d.box.IBox3IM;
import com.artur114.bananalib.math.m3d.matrix.Matrix3FM;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.common.network.ClientPacketCreateFX;
import com.artur114.thaumrota.common.tileentity.TileEntityAncientDoor8X6;
import com.artur114.thaumrota.common.tileentity.TileEntityDummy;
import com.artur114.thaumrota.common.util.math.AreasCombiner;
import com.artur114.thaumrota.common.util.math.BoundingBox;
import com.artur114.thaumrota.common.util.math.IArea;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.CombatWave;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumRotate;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.StrPos;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.maps.InteractiveMap;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.utils.AncientWorldPlayer;
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

public abstract class StructureCombatRoom extends StructureMultiChunk implements IStructureInteractive, IStructureEntityManager {
    private final Map<Class<? extends TileEntity>, List<BlockPos>> tilesMap = new HashMap<>();
    private final Map<UUID, Class<? extends EntityLiving>> aliveEntities = new HashMap<>();
    private List<IBox3IM> triggerBoxes = null;
    private boolean isTriggerReversed = false;
    private ArrayDeque<CombatWave> waves = null;
    private int timeToStartSpawn = 0;
    private boolean triggered = false;
    private boolean allDead = false;
    protected ChunkPos chunkPos = null;
    private IArea spawnArea = null;
    private long sessionId = -1;
    protected Random rand = null;
    protected World world = null;

    public StructureCombatRoom(EnumRotate rotate, EnumMultiChunkStrType type, StrPos pos) {
        super(rotate, type, pos);
    }

    protected StructureCombatRoom(StructureMultiChunk parent) {
        super(parent);
    }

    protected void closeBigDoors(PosMc3IM... poses) {
        this.transform(Arrays.asList(poses)).forEach(pos -> {
            TileEntity tileRaw = this.world.getTileEntity(pos);

            if (tileRaw instanceof TileEntityDummy) {
                tileRaw = this.world.getTileEntity(((TileEntityDummy) tileRaw).parentPos());
            }

            if (tileRaw instanceof TileEntityAncientDoor8X6) {
                ((TileEntityAncientDoor8X6) tileRaw).close();
                ((TileEntityAncientDoor8X6) tileRaw).syncTile(false);
            } else {
                this.world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            }
        });
    }

    protected void openBigDoors(PosMc3IM... poses) {
        this.transform(Arrays.asList(poses)).forEach(pos -> {
            TileEntity tileRaw = this.world.getTileEntity(pos);

            if (tileRaw instanceof TileEntityDummy) {
                tileRaw = this.world.getTileEntity(((TileEntityDummy) tileRaw).parentPos());
            }

            if (tileRaw instanceof TileEntityAncientDoor8X6) {
                ((TileEntityAncientDoor8X6) tileRaw).open();
                ((TileEntityAncientDoor8X6) tileRaw).syncTile(false);
            } else {
                this.world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            }
        });
    }

    protected List<BlockPos> tiles(Class<? extends TileEntity> clazz) {
        return this.tilesMap.getOrDefault(clazz, Collections.emptyList());
    }

    protected <T extends TileEntity> Optional<T> tile(BlockPos pos, Class<T> clazz) {
        TileEntity tile = this.world.getTileEntity(pos);

        if (tile == null) {
            return Optional.empty();
        }

        if (clazz.isInstance(tile)) {
            return Optional.of(clazz.cast(tile));
        }

        return Optional.empty();
    }

    protected List<PosMc3IM> transform(List<PosMc3IM> list) {
        Matrix3FM matrix = Matrix3FM.obtain();
        matrix.rotateYAround(7.5, 0, 7.5, this.rotate.degrees());
        matrix.translate(this.chunkPos.x << 4, this.y, this.chunkPos.z << 4);
        for (PosMc3IM pos : list) matrix.transform(pos);
        Matrix3FM.release(matrix);
        return list;
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        PosMc3IM blockPos = PosMc3IM.obtain();
        blockPos.setChunk(pos).add(8, 0, 8).setY(this.y);
        BiFunction<TileEntity, NBTTagCompound, TileEntity> hook = (tile, data) -> {
            List<BlockPos> list = this.tilesMap.computeIfAbsent(tile.getClass(), k -> new ArrayList<>());
            list.add(tile.getPos());
            return tile;
        };
        StructuresBuildManager.createBuildRequest(world, blockPos, this.type.stringId(this.rotate)).setIgnoreAir().setPosAsXZCenter().addTileEntityHook(hook).build();
        PosMc3IM.release(blockPos);
    }

    @Override
    public boolean loadEntity(EntityLiving entity) {
        return this.aliveEntities.containsKey(entity.getUniqueID());
    }

    @Override
    public void unloadEntity(EntityLiving entity) {}

    @Override
    public void onEntityDead(EntityLiving entity) {
        this.aliveEntities.remove(entity.getUniqueID());
    }

    @Override
    public void update(List<AncientWorldPlayer> players) {
        if (this.world.isRemote) {
            return;
        }
        if (this.allDead) {
            return;
        }
        if (this.aliveEntities.isEmpty() && this.waves.isEmpty()) {
            this.allDead = true;
            this.onAllDead();
            return;
        }
        if (this.world.getMinecraftServer() != null && this.world.getMinecraftServer().getTickCounter() % 40 == 0) {
            ArrayList<UUID> list = new ArrayList<>();
            this.aliveEntities.forEach((uuid, e) -> {
                Entity entity = this.world.getMinecraftServer().getEntityFromUuid(uuid);
                if (!e.isInstance(entity)) {
                    list.add(uuid);
                }
            });
            list.forEach(this.aliveEntities::remove);
        }
        if (this.triggered) {
            if (this.timeToStartSpawn > 0) {
                this.timeToStartSpawn--;
                return;
            }

            CombatWave wave = this.waves.peekFirst();
            if (wave == null) {
                return;
            }
            if (!wave.isSpawned()) {
                wave.spawn(this.rand, this.world, this.spawnArea, (pos, entity) -> {
                    this.aliveEntities.put(entity.getUniqueID(), entity.getClass());
                    ClientPacketCreateFX.send(this.world, pos, ClientPacketCreateFX.FXType.ENTITY_SPAWN);
                    this.spawnEntity(this.world, pos, entity);
                });
            } else if (wave.shouldNextWave(this.aliveEntities.values())){
                this.waves.removeFirst();
            }
        } else {
            for (AncientWorldPlayer player : players) {
                if (this.isTriggerReversed) {
                    if (this.noContainsInAll(this.triggerBoxes, player.player)) {
                        this.trigger();
                        break;
                    }
                } else {
                    if (this.containsInAny(this.triggerBoxes, player.player)) {
                        this.trigger();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public InteractiveMap map() {
        return (InteractiveMap) this.map;
    }

    @Override
    public void bindWorld(World world) {
        this.world = world;
    }

    @Override
    public void bindRealPos(ChunkPos pos) {
        this.rand = new Random();
        this.chunkPos = pos;
        this.computeBoxes();
    }

    @Override
    public void bindSessionId(long id) {
        this.sessionId = id;
    }

    @Override
    public long sessionId() {
        return this.sessionId;
    }

    private void trigger() {
        this.triggered = true;
        this.timeToStartSpawn = this.onTriggered();
    }

    private void computeBoxes() {
        List<IBox3IM> trigger = new ArrayList<>();
        AtomicBoolean reversed = new AtomicBoolean();
        this.loadTriggerBoxes(trigger, reversed);
        this.rotateAndTranslate(trigger);
        this.isTriggerReversed = reversed.get();
        this.triggerBoxes = trigger;

        List<IBox3IM> spawnAdd = new ArrayList<>();
        List<IBox3IM> spawnSubtract = new ArrayList<>();
        this.loadSpawnArea(spawnAdd, spawnSubtract);
        this.rotateAndTranslate(spawnAdd);
        this.rotateAndTranslate(spawnSubtract);

        AreasCombiner combiner = new AreasCombiner();
        for (IBox3IM box : spawnAdd) {
            combiner.addArea(new BoundingBox(box.minX(), box.minY(), box.minZ(), box.maxX() - 1, box.maxY() - 1, box.maxZ() - 1));
        }
        for (IBox3IM box : spawnSubtract) {
            combiner.subtractArea(new BoundingBox(box.minX(), box.minY(), box.minZ(), box.maxX() - 1, box.maxY() - 1, box.maxZ() - 1));
        }
        this.spawnArea = combiner.bake();

        List<CombatWave> list = new ArrayList<>();
        this.loadWaves(list);
        this.waves = new ArrayDeque<>(list);
    }

    private void rotateAndTranslate(List<IBox3IM> list) {
        Matrix3FM matrix = Matrix3FM.obtain();
        matrix.rotateYAround(7.5, 0, 7.5, this.rotate.degrees());
        matrix.translate(this.chunkPos.x << 4, this.y, this.chunkPos.z << 4);
        for (IBox3IM pos : list) matrix.transform(pos);
        Matrix3FM.release(matrix);
    }

    private boolean noContainsInAll(List<IBox3IM> list, EntityPlayer player) {
        for (IBox3IM box : list) {
            if (box.contains(player.posX, player.posY, player.posZ)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsInAny(List<IBox3IM> list, EntityPlayer player) {
        for (IBox3IM box : list) {
            if (box.contains(player.posX, player.posY, player.posZ)) {
                return true;
            }
        }
        return false;
    }

    protected abstract void onAllDead();
    protected abstract int onTriggered();
    protected abstract void loadWaves(List<CombatWave> waves);
    protected abstract void loadSpawnArea(List<IBox3IM> add, List<IBox3IM> subtract);
    protected abstract void loadTriggerBoxes(List<IBox3IM> list, AtomicBoolean isReversed);
}
