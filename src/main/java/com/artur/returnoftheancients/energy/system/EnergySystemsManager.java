package com.artur.returnoftheancients.energy.system;

import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.energy.bases.tile.ITileEnergy;
import com.artur.returnoftheancients.energy.bases.tile.ITileEnergyProvider;
import com.artur.returnoftheancients.handlers.CollectionsHandler;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.Callable;

public class EnergySystemsManager {
    private final Map<Long, EnergySystem> ENERGY_SYSTEMS = new HashMap<>();
    private final Random rand = new Random();
    private final World world;

    public EnergySystemsManager(World world) {
        this.world = world;
    }

    public void onTileLoad(ITileEnergy tile) {
        if (tile.networkId() == -1) {
            this.onBlockAdded(tile);
        } else {
            this.loadTile(tile);
        }
    }

    public void onTileUnload(ITileEnergy tile) {
        EnergySystem system = this.getSystem(tile.networkId());
        if (system != null) {
            system.remove(tile);
        }
        if (TRAConfigs.Any.debugMode) System.out.println("Unloaded tile, system id:" + tile.networkId());
    }

    public void onBlockDestroyed(ITileEnergy tile) {
        List<ITileEnergy> connectedTiles = this.getNeighbors(tile);

        if (connectedTiles == null) {
            EnergySystem system = this.getSystem(tile.networkId());
            if (system != null) system.remove(tile);
            return;
        }

        if (connectedTiles.size() == 1) {
            EnergySystem system = this.getSystem(tile.networkId());
            if (system != null) {
                system.remove(tile);
            } else {
                this.world.removeTileEntity(tile.pos());
                this.buildNetwork(connectedTiles.get(0));
            }
            return;
        }

        this.world.removeTileEntity(tile.pos());
        Set<Long> ids = new HashSet<>();
        for (ITileEnergy tileN : connectedTiles) {
            if (!ids.contains(tileN.networkId())) {
                ids.add(buildNetwork(tileN).id);
            }
        }
    }

    public void update(boolean isStart) {
        Iterator<EnergySystem> iterator = ENERGY_SYSTEMS.values().iterator();

        while (iterator.hasNext()) {
            EnergySystem system = iterator.next();

            if (system.isEmpty()) {
                iterator.remove();
                if (TRAConfigs.Any.debugMode) System.out.println("Removed system:" + system.id);
            }

            system.update(isStart);
        }
    }

    @Nullable
    private List<ITileEnergy> getNeighbors(ITileEnergy tile) {
        List<ITileEnergy> list = null;
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(tile.pos());
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (tile.canConnect(facing)) {
                blockPos.pushPos();
                TileEntity tileRaw = world.getTileEntity(blockPos.offset(facing));
                blockPos.popPos();
                if (tileRaw instanceof ITileEnergy) {
                    ITileEnergy neighbor = (ITileEnergy) tileRaw;
                    if (neighbor.canConnect(facing.getOpposite())) {
                        if (list == null) {
                            list = new ArrayList<>(6);
                        }
                        list.add(neighbor);
                    }
                }
            }
        }
        return list;
    }

    private void onBlockAdded(ITileEnergy tile) {
        List<ITileEnergy> connectedTiles = this.getNeighbors(tile);

        if (TRAConfigs.Any.debugMode) System.out.println("New tile!");

        if (connectedTiles == null) {
            this.newSystem().bindTile(tile);
            return;
        }

        if (connectedTiles.stream().anyMatch(t -> t.networkId() == -1)) {
            this.buildNetwork(tile);
            return;
        }

        if (connectedTiles.stream().allMatch(t -> t.networkId() == connectedTiles.get(0).networkId())) {
            this.getSystem(connectedTiles.get(0).networkId(), () -> this.buildNetwork(tile)).bindTile(tile);
            return;
        }

        Set<Long> ids = CollectionsHandler.toLongCollection(new HashSet<>(), connectedTiles, ITileEnergy::networkId);
        if (ids.contains(-1L)) {
            this.buildNetwork(tile);
            return;
        }
        this.mergeAll(this.newSystem().bindTile(tile), ids);
    }

    private void loadTile(ITileEnergy tile) {
        this.getSystem(tile.networkId(), () -> this.newSystem(tile.networkId())).bindTile(tile);
        if (TRAConfigs.Any.debugMode) System.out.println("Loaded tile, system id:" + tile.networkId());
    }

    private void addSystem(EnergySystem system) {
        ENERGY_SYSTEMS.put(system.id, system);
    }

    private void removeSystem(EnergySystem system) {
        ENERGY_SYSTEMS.remove(system.id, system);
    }

    private void removeSystem(long id) {
        ENERGY_SYSTEMS.remove(id);
    }

    private EnergySystem getSystem(long id) {
        return getSystem(id, () -> null);
    }

    private EnergySystem getSystem(long id, Callable<EnergySystem> onFall) {
        EnergySystem system = ENERGY_SYSTEMS.get(id);
        if (system != null) {
            return system;
        }
        try {
            return onFall.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private EnergySystem newSystem(Set<ITileEnergyProvider> storages, Set<ITileEnergy> lines, long id) {
        EnergySystem system = new EnergySystem(storages, lines, id);
        this.addSystem(system);
        if (TRAConfigs.Any.debugMode) System.out.println("New system id:" + system.id);
        return system;
    }

    private EnergySystem newSystem(Set<ITileEnergyProvider> storages, Set<ITileEnergy> lines) {
        return this.newSystem(storages, lines, this.rand.nextLong());
    }

    private EnergySystem newSystem(long id) {
        return this.newSystem(new HashSet<>(), new HashSet<>(), id);
    }

    private EnergySystem newSystem() {
        return this.newSystem(new HashSet<>(), new HashSet<>());
    }

    private EnergySystem buildNetwork(ITileEnergy startTile) {
        return this.buildNetwork(startTile, this.rand.nextLong());
    }

    private EnergySystem buildNetwork(ITileEnergy startTile, long id) {
        Set<ITileEnergyProvider> storages = new HashSet<>(10);
        ArrayDeque<BlockPos> queue = new ArrayDeque<>(100);
        Set<ITileEnergy> lines = new HashSet<>(100);
        startTile.setNetworkId(id);

        if (startTile.isEnergyLine()) {
            lines.add(startTile);
        } else {
            storages.add((ITileEnergyProvider) startTile);
        }

        queue.addLast(startTile.pos());

        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            ITileEnergy tile = (ITileEnergy) world.getTileEntity(pos);

            for (EnumFacing facing : EnumFacing.values()) {
                if (tile != null && tile.canConnect(facing)) {
                    BlockPos offsetPos = pos.offset(facing);
                    TileEntity tileRaw = world.getTileEntity(offsetPos);

                    if (tileRaw instanceof ITileEnergy) {
                        ITileEnergy tileEnergy = (ITileEnergy) tileRaw;

                        if (lines.contains(tileEnergy)) continue;
                        if (!tileEnergy.isEnergyLine() && storages.contains((ITileEnergyProvider) tileEnergy)) continue;

                        if (tileEnergy.canConnect(facing.getOpposite())) {
                            if (tileEnergy.isEnergyLine()) {
                                lines.add(tileEnergy);
                            } else {
                                storages.add((ITileEnergyProvider) tileEnergy);
                            }

                            queue.addLast(tileEnergy.pos());

                            if (tileEnergy.networkId() != id) {
                                this.removeSystem(tileEnergy.networkId());
                                tileEnergy.setNetworkId(id);
                            }
                        }
                    }
                }
            }
        }

        if (TRAConfigs.Any.debugMode) System.out.println("Build network:" + id);

        return this.newSystem(storages, lines, id);
    }

    private void mergeAll(EnergySystem base, Collection<Long> ids) {
        for (long id : ids) if (ENERGY_SYSTEMS.containsKey(id)) base.merge(ENERGY_SYSTEMS.get(id));
    }
}
