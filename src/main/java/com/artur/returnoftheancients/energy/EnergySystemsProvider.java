package com.artur.returnoftheancients.energy;

import com.artur.returnoftheancients.energy.intefaces.ITileEnergy;
import com.artur.returnoftheancients.energy.intefaces.ITileEnergyProvider;
import com.artur.returnoftheancients.handlers.HandlerR;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.*;

public class EnergySystemsProvider {

    public static final Map<Integer, EnergySystem> energySystems = new HashMap<>();

    public static void onBlockDestroyed(World world, BlockPos pos) {
        if (world.isRemote) return;
        ITileEnergy tileEnergy = (ITileEnergy) world.getTileEntity(pos);
        if (tileEnergy == null) return;
        List<ITileEnergy> connectedTiles = getNeighbors(world, tileEnergy);
        int tileNetworkId = tileEnergy.getNetworkId();

        if (connectedTiles.isEmpty()) {
            energySystems.remove(tileNetworkId);
            return;
        }

        if (connectedTiles.size() == 1) {
            EnergySystem system = energySystems.get(tileNetworkId);
            if (system != null) {
                system.remove(tileEnergy);
            } else {
                energySystems.remove(connectedTiles.get(0).getNetworkId());
                buildNetwork(world, connectedTiles.get(0));
            }
            world.removeTileEntity(pos);
            return;
        }

        world.removeTileEntity(pos);
        Set<Integer> ids = new HashSet<>();
        for (ITileEnergy tile : connectedTiles) {
            int currentId = tile.getNetworkId();
            if (!ids.contains(currentId)) {
                ids.add(buildNetwork(world, tile, foundFreeIdWithBlackList(currentId)));
                energySystems.remove(currentId);
            }
        }
    }


    public static void onBlockAdded(World world, ITileEnergy tileEnergy) {
        if (world.isRemote) return;
        List<ITileEnergy> connectedTiles = getNeighbors(world, tileEnergy);

        if (connectedTiles.isEmpty()) {
            EnergySystem system = createEmptyEnergyNetwork();
            system.add(tileEnergy);
            return;
        }

        if (connectedTiles.stream().anyMatch(t -> t.getNetworkId() == -1)) {
            buildNetwork(world, tileEnergy);
            return;
        }

        int id0 = connectedTiles.get(0).getNetworkId();
        if (connectedTiles.stream().allMatch(t -> t.getNetworkId() == id0)) {
            EnergySystem system = energySystems.get(id0);
            if (system != null) {
                system.add(tileEnergy);
            } else {
                buildNetwork(world, tileEnergy);
            }
            return;
        }

        int id = connectedTiles.get(0).getNetworkId();
        Set<Integer> ids = new HashSet<>(6);
        for (ITileEnergy tile : connectedTiles) {
            ids.add(tile.getNetworkId());
            if (id > tile.getNetworkId()) {
                id = tile.getNetworkId();
            }
        }
        ids.remove(id);

        if (ids.contains(-1)) {
            buildNetwork(world, tileEnergy);
            return;
        }

        EnergySystem system = energySystems.get(id);
        if (system != null && ids.stream().allMatch(energySystems::containsKey)) {
            system.add(tileEnergy);
            unite(id, ids);
        } else {
            buildNetwork(world, tileEnergy);
        }
    }

    public static int buildNetwork(World world, ITileEnergy startTile) {
        return buildNetwork(world, startTile, foundFreeId());
    }

    public static int buildNetwork(World world, ITileEnergy startTile, int id) {
        if (energySystems.containsKey(id)) return -1;
        long time = System.nanoTime();
        int queueSize = 0;
        ArrayDeque<BlockPos> queue = new ArrayDeque<>(100);
        Set<ITileEnergy> lines = new HashSet<>(100);
        Set<ITileEnergyProvider> energyStorages = new HashSet<>(10);
        startTile.setNetworkId(id);

        if (startTile.isEnergyLine()) {
            lines.add(startTile);
        } else {
            energyStorages.add((ITileEnergyProvider) startTile);
        }

        queue.addLast(startTile.getPos());

        while (!queue.isEmpty()) {
            queueSize++;
            BlockPos pos = queue.poll();

            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offsetPos = pos.offset(facing);
                TileEntity tileRaw = world.getTileEntity(offsetPos);
                if (tileRaw instanceof ITileEnergy) {
                    ITileEnergy tileEnergy = (ITileEnergy) tileRaw;
                    if (lines.contains(tileEnergy)) continue;
                    if (tileEnergy.isCanConnect(facing.getOpposite())) {
                        if (tileEnergy.isEnergyLine()) {
                            queue.addLast(tileEnergy.getPos());
                            lines.add(tileEnergy);
                        } else {
                            if (energyStorages.contains((ITileEnergyProvider) tileEnergy)) continue;
                            energyStorages.add((ITileEnergyProvider) tileEnergy);
                        }
                        if (tileEnergy.getNetworkId() != id && tileEnergy.getNetworkId() != -1) {
                            energySystems.remove(tileEnergy.getNetworkId());
                            tileEnergy.setNetworkId(id);
                        }
                    }
                }
            }
        }

        createEnergyNetwork(energyStorages, lines, id);

        System.out.println("Build network is finis, is took:" + ((System.nanoTime() - time) / 1000000.0D) + "ms, queue size:" + queueSize);
        return id;
    }

    public static EnergySystem createEmptyEnergyNetwork() {
        return createEnergyNetwork(new HashSet<>(), new HashSet<>(), foundFreeId());
    }

    public static EnergySystem createEnergyNetwork(Set<ITileEnergyProvider> tileEnergyProviders, Set<ITileEnergy> energyLines, int id) {
        EnergySystem system = new EnergySystem(tileEnergyProviders, energyLines, id);
        energySystems.put(id, system);
        return system;
    }


    public static List<ITileEnergy> getNeighbors(World world, ITileEnergy tile) {
        List<ITileEnergy> list = new ArrayList<>(6);
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offsetPos = tile.getPos().offset(facing);
            TileEntity tileRaw = world.getTileEntity(offsetPos);
            if (tileRaw instanceof ITileEnergy) {
                ITileEnergy tileEnergy = (ITileEnergy) tileRaw;
                if (tileEnergy.isCanConnect(facing.getOpposite())) {
                    list.add(tileEnergy);
                }
            }
        }
        return list;
    }

    public static List<ITileEnergy> getNeighbors(IBlockAccess world, BlockPos pos) {
        List<ITileEnergy> list = new ArrayList<>(6);
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offsetPos = pos.offset(facing);
            TileEntity tileRaw = world.getTileEntity(offsetPos);
            if (tileRaw instanceof ITileEnergy) {
                ITileEnergy tileEnergy = (ITileEnergy) tileRaw;
                if (tileEnergy.isCanConnect(facing.getOpposite())) {
                    list.add(tileEnergy);
                }
            }
        }
        return list;
    }

    public static int foundFreeId() {
        return HandlerR.foundMostSmallUniqueIntInSet(energySystems.keySet());
    }

    public static int foundFreeIdWithBlackList(int... not) {
        Set<Integer> set = new HashSet<>(energySystems.keySet());
        for (int i : not) {
            set.add(i);
        }
        return HandlerR.foundMostSmallUniqueIntInSet(set);
    }


    public static void unite(int id0, Set<Integer> ids) {
        for (int id : ids) {
            energySystems.get(id0).addSystem(energySystems.get(id));
            energySystems.remove(id);
        }
    }
}
