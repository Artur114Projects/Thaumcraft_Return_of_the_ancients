package com.artur.returnoftheancients.energy;

import com.artur.returnoftheancients.energy.intefaces.ITileEnergy;
import com.artur.returnoftheancients.energy.intefaces.ITileEnergyProvider;
import com.artur.returnoftheancients.misc.TRAConfigs;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class EnergySystem {
    private final Set<ITileEnergyProvider> canTake = new HashSet<>();
    private final Set<ITileEnergyProvider> canAdd = new HashSet<>();
    private final Set<ITileEnergyProvider> energyStorages;
    private final Set<ITileEnergy> energyLines;
    private boolean isChanged = true;
    private boolean isWork = false;
    private byte workTick = 0;

    public final int id;

    public EnergySystem(Set<ITileEnergyProvider> energyStorages, Set<ITileEnergy> energyLines, int id) {
        this.energyStorages = energyStorages;
        this.energyLines = energyLines;
        this.id = id;
    }

    public boolean containsTile(ITileEnergy tileEnergy) {
        if (tileEnergy.isEnergyLine()) {
            return energyLines.contains(tileEnergy);
        } else {
            return energyStorages.contains((ITileEnergyProvider) tileEnergy);
        }
    }

    public boolean containsEnergyProviderPos(BlockPos pos) {
        for (ITileEnergyProvider t : energyStorages) {
            if (pos.equals(t.getPosE())) {
                return true;
            }
        }
        return false;
    }

    public void addSystem(EnergySystem system) {
        energyStorages.addAll(system.energyStorages);
        energyLines.addAll(system.energyLines);
        for (ITileEnergy tileEnergy : energyLines) {
            tileEnergy.setNetworkId(id);
        }
        for (ITileEnergy tileEnergy : energyStorages) {
            tileEnergy.setNetworkId(id);
        }
        isChanged = true;
        if (TRAConfigs.Any.debugMode) System.out.println("A merger has occurred main system:" + id + " merged system:" + system.id);
    }

    public void add(ITileEnergy tileEnergy) {
        if (tileEnergy.isEnergyLine()) {
            tileEnergy.setNetworkId(id);
            energyLines.add(tileEnergy);
        } else {
            tileEnergy.setNetworkId(id);
            energyStorages.add((ITileEnergyProvider) tileEnergy);
        }
        isChanged = true;
    }

    public void remove(ITileEnergy tileEnergy) {
        if (tileEnergy.isEnergyLine()) {
            energyLines.remove(tileEnergy);
        } else {
            energyStorages.remove((ITileEnergyProvider) tileEnergy);
        }
        isChanged = true;
    }

    public void update() {
        if (!isWork) {
            workTick++;
            if (workTick >= 10) {
                workTick = 0;
                if (energyStorages.stream().anyMatch(ITileEnergyProvider::isNeedAdd)) {
                    isWork = true;
                } else {
                    isWork = false;
                    return;
                }
            }
            if (!isWork) {
                return;
            }
        }

        if (isChanged) {
            canTake.clear();
            canAdd.clear();

            for (ITileEnergyProvider tile : energyStorages) {
                if (tile.canAdd()) {
                    canAdd.add(tile);
                }
                if (tile.canTake()) {
                    canTake.add(tile);
                }
            }

            isChanged = false;
        }

        if (canTake.isEmpty()) {
            return;
        }

        boolean flag = false;
        for (ITileEnergyProvider tile : canAdd) {
            if (tile.isLoaded() && tile.isNeedAdd()) {
                tile.add(takeFromAll(tile.canAdd(tile.maxInput())));
                flag = true;
            }
        }

        isWork = flag;
    }

    private float takeFromAll(float count) {
        float localCount = count;
        Set<ITileEnergyProvider> localCanTake = new HashSet<>();
        for (ITileEnergyProvider tile : canTake) {
            if (tile.isLoaded() && !tile.isEmpty()) {
                localCanTake.add(tile);
            }
        }

        Set<ITileEnergyProvider> tilesWithMaxOutputIsLessThanCount = getTilesWithMaxOutputIsLessThanCount(localCount / localCanTake.size());
        if (tilesWithMaxOutputIsLessThanCount != null) {
            localCanTake.removeAll(tilesWithMaxOutputIsLessThanCount);
            for (ITileEnergyProvider tileEnergyProvider : tilesWithMaxOutputIsLessThanCount) {
                localCount -= tileEnergyProvider.take(tileEnergyProvider.maxOutput());
            }
        }

        float takeCount = localCount / localCanTake.size();

        for (ITileEnergyProvider tile : localCanTake) {
            localCount -= tile.take(takeCount);
        }

        return count - localCount;
    }

    @Nullable
    private Set<ITileEnergyProvider> getTilesWithMaxOutputIsLessThanCount(float count) {
        Set<ITileEnergyProvider> tiles = null;
        for (ITileEnergyProvider tile : canTake) {
            if (tile.maxOutput() < count) {
                if (tiles == null) {
                    tiles = new HashSet<>();
                }
                tiles.add(tile);
            }
        }
        return tiles;
    }

    @Override
    public String toString() {
        return "Energy storages count:" + energyStorages.size() + ", energy lines count:" + energyLines.size();
    }
}
