package com.artur.returnoftheancients.energy;

import com.artur.returnoftheancients.energy.intefaces.ITileEnergy;
import com.artur.returnoftheancients.energy.intefaces.ITileEnergyProvider;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

// TODO: починить проблему с выгрузкой тайлов
// TODO: 17.03.2025 Сделать чтобы провода по которым идет энергия красиво светились 
// TODO: 17.03.2025 Сделать новую систему энергии с случайным long id все это в capability при загрузке тайла либо создается новая система либо он добавляется в существующею, при выгрузке тайла он удаляется из системы если ситама пуста то она удаляется 
@Mod.EventBusSubscriber(modid = Referense.MODID)
public class EnergySystemsProvider {

    public static final Set<Integer> LOADED_NETWORKS = new HashSet<>();
    public static final Map<Integer, EnergySystem> ENERGY_SYSTEMS = new HashMap<>();

    public static void onBlockDestroyed(World world, BlockPos pos) {
        if (world.isRemote) return;
        ITileEnergy tileEnergy = (ITileEnergy) world.getTileEntity(pos);
        if (tileEnergy == null) return;
        List<ITileEnergy> connectedTiles = getNeighbors(world, tileEnergy);
        int tileNetworkId = tileEnergy.getNetworkId();

        if (connectedTiles.isEmpty()) {
            if (ENERGY_SYSTEMS.remove(tileNetworkId) == null) {
                System.out.println("Warning! failed to remove network for broken block id:" + tileNetworkId);
                System.out.println(ENERGY_SYSTEMS);
            }
            return;
        }

        if (connectedTiles.size() == 1) {
            EnergySystem system = ENERGY_SYSTEMS.get(tileNetworkId);
            if (system != null) {
                system.remove(tileEnergy);
            } else {
                ENERGY_SYSTEMS.remove(connectedTiles.get(0).getNetworkId());
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
                ENERGY_SYSTEMS.remove(currentId);
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
            EnergySystem system = ENERGY_SYSTEMS.get(id0);
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

        EnergySystem system = ENERGY_SYSTEMS.get(id);
        if (system != null && ids.stream().allMatch(ENERGY_SYSTEMS::containsKey)) {
            system.add(tileEnergy);
            unite(id, ids);
        } else {
            buildNetwork(world, tileEnergy);
        }
    }

    public static void onTileLoad(ITileEnergyProvider tile) {
        if (tile.getNetworkId() == -1) {
            System.out.println("asdfsdhgjdryewrafdsfgftfwgfnchtsrthjhjterreggfjhg");
            return;
        }
        if (!ENERGY_SYSTEMS.containsKey(tile.getNetworkId())) {
            buildNetwork(tile.getWorldE(), tile, foundFreeId(), false);
        } else if (!ENERGY_SYSTEMS.get(tile.getNetworkId()).containsEnergyProviderPos(tile.getPosE())) {
            buildNetwork(tile.getWorldE(), tile, foundFreeId(), false);
        }
    }

    public static int buildNetwork(World world, ITileEnergy startTile) {
        return buildNetwork(world, startTile, foundFreeId());
    }

    public static int buildNetwork(World world, ITileEnergy startTile, int id) {
        return buildNetwork(world, startTile, id, true);
    }

    public static int buildNetwork(World world, ITileEnergy startTile, int id, boolean isRemoveFoundNetworks) {
        if (ENERGY_SYSTEMS.containsKey(id)) return -1;
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

        queue.addLast(startTile.getPosE());

        while (!queue.isEmpty()) {
            queueSize++;
            BlockPos pos = queue.poll();
            ITileEnergy tile = (ITileEnergy) world.getTileEntity(pos);

            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offsetPos = pos.offset(facing);
                TileEntity tileRaw = world.getTileEntity(offsetPos);
                if (tileRaw instanceof ITileEnergy) {
                    ITileEnergy tileEnergy = (ITileEnergy) tileRaw;
                    if (lines.contains(tileEnergy)) continue;
                    if (tile != null && tile.isCanConnect(facing) && tileEnergy.isCanConnect(facing.getOpposite())) {
                        if (tileEnergy.isEnergyLine()) {
                            queue.addLast(tileEnergy.getPosE());
                            lines.add(tileEnergy);
                        } else {
                            if (energyStorages.contains((ITileEnergyProvider) tileEnergy)) continue;
                            energyStorages.add((ITileEnergyProvider) tileEnergy);
                        }
                        if (tileEnergy.getNetworkId() != id && tileEnergy.getNetworkId() != -1) {
                            if (isRemoveFoundNetworks) {
                                ENERGY_SYSTEMS.remove(tileEnergy.getNetworkId());
                            }
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
        ENERGY_SYSTEMS.put(id, system);
        return system;
    }

    public static List<ITileEnergy> getNeighbors(World world, ITileEnergy tile) {
        List<ITileEnergy> list = new ArrayList<>(6);
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offsetPos = tile.getPosE().offset(facing);
            TileEntity tileRaw = world.getTileEntity(offsetPos);
            if (tileRaw instanceof ITileEnergy) {
                ITileEnergy tileEnergy = (ITileEnergy) tileRaw;
                if (tile.isCanConnect(facing) && tileEnergy.isCanConnect(facing.getOpposite())) {
                    list.add(tileEnergy);
                }
            }
        }
        return list;
    }

    public static int foundFreeId() {
        return MiscHandler.foundMostSmallUniqueIntInSet(ENERGY_SYSTEMS.keySet());
    }

    public static int foundFreeIdWithBlackList(int... not) {
        Set<Integer> set = new HashSet<>(ENERGY_SYSTEMS.keySet());
        for (int i : not) {
            set.add(i);
        }
        return MiscHandler.foundMostSmallUniqueIntInSet(set);
    }

    public static int foundFreeIdWithBlackList(Set<Integer> not) {
        Set<Integer> set = new HashSet<>(ENERGY_SYSTEMS.keySet());
        set.addAll(not);
        return MiscHandler.foundMostSmallUniqueIntInSet(set);
    }



    public static void unite(int id0, Set<Integer> ids) {
        for (int id : ids) {
            ENERGY_SYSTEMS.get(id0).addSystem(ENERGY_SYSTEMS.get(id));
            ENERGY_SYSTEMS.remove(id);
        }
    }

    public static void unload() {
        LOADED_NETWORKS.clear();
        ENERGY_SYSTEMS.clear();
    }

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            for (EnergySystem system : ENERGY_SYSTEMS.values()) {
                system.update();
            }
        }
    }


}
