package com.artur.returnoftheancients.energy.system;


import com.artur.returnoftheancients.energy.bases.tile.ITileEnergy;
import com.artur.returnoftheancients.energy.bases.tile.ITileEnergyProvider;

import java.util.Set;

public class EnergySystem {
    private final Set<ITileEnergyProvider> storages;
    private final Set<ITileEnergy> lines;

    public final long id;


    public EnergySystem(Set<ITileEnergyProvider> storages, Set<ITileEnergy> lines, long id) {
        this.storages = storages;
        this.lines = lines;
        this.id = id;
    }

    public boolean isEmpty() {
        return storages.isEmpty() && lines.isEmpty();
    }

    public EnergySystem bindTile(ITileEnergy tile) {
        tile.setNetworkId(id);

        if (tile.isEnergyLine()) {
            this.lines.add(tile);
        } else {
            this.storages.add((ITileEnergyProvider) tile);
        }

        return this;
    }

    public EnergySystem remove(ITileEnergy tile) {
        if (tile.isEnergyLine()) {
            this.lines.remove(tile);
        } else {
            this.storages.remove((ITileEnergyProvider) tile);
        }

        return this;
    }

    public void merge(EnergySystem system) {
        for (ITileEnergy tile : system.storages) tile.setNetworkId(id);
        for (ITileEnergy tile : system.lines) tile.setNetworkId(id);

        this.storages.addAll(system.storages);
        this.lines.addAll(system.lines);

        system.storages.clear();
        system.lines.clear();
    }

    public void update() {

    }
}
