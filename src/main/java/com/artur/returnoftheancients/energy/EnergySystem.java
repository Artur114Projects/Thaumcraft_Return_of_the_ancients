package com.artur.returnoftheancients.energy;

import com.artur.returnoftheancients.energy.intefaces.ITileEnergy;
import com.artur.returnoftheancients.energy.intefaces.ITileEnergyProvider;
import com.artur.returnoftheancients.misc.TRAConfigs;

import java.util.Set;

public class EnergySystem {
    private final Set<ITileEnergyProvider> energyStorages;
    private final Set<ITileEnergy> energyLines;

    public final int id;

    public EnergySystem(Set<ITileEnergyProvider> energyStorages, Set<ITileEnergy> energyLines, int id) {
        this.energyStorages = energyStorages;
        this.energyLines = energyLines;
        this.id = id;
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
        if (TRAConfigs.Any.debugMode) System.out.println("A merger has occurred main system:" + id + " merged system:" + system.id);
    }

    public void add(ITileEnergy tileEnergy) {
        if (tileEnergy.isEnergyLine()) {
            tileEnergy.setNetworkId(id);
            addLine(tileEnergy);
        } else {
            tileEnergy.setNetworkId(id);
            addStorage((ITileEnergyProvider) tileEnergy);
        }
    }

    public void addLine(ITileEnergy tileEnergy) {
        energyLines.add(tileEnergy);
    }

    public void addStorage(ITileEnergyProvider tileEnergyProvider) {
        energyStorages.add(tileEnergyProvider);
    }

    public void remove(ITileEnergy tileEnergy) {
        if (tileEnergy.isEnergyLine()) {
            energyLines.remove(tileEnergy);
        } else {
            energyStorages.remove((ITileEnergyProvider) tileEnergy);
        }
    }

    public void update() {

    }

    @Override
    public String toString() {
        return "Energy storages count:" + energyStorages.size() + ", energy lines count:" + energyLines.size();
    }
}
