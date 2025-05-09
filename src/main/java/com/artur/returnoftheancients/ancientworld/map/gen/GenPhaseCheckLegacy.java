package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.legacy.AncientLayer1LegacyGen;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.StructurePos;
import com.artur.returnoftheancients.ancientworld.map.utils.StructuresMap;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureMultiChunk;
import com.artur.returnoftheancients.handlers.GraphHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class GenPhaseCheckLegacy extends GenPhase {

    public GenPhaseCheckLegacy(GenPhase parent) {
        super(parent);
    }

    @Override
    public @NotNull StructuresMap getMap(long seed, int size) {
        StructuresMap map;
        do {
            map = this.parent.getMap(seed++, size);
        } while (!checkWayToBoss(map));
        return map;
    }

    private boolean checkWayToBoss(StructuresMap map) {
        StructurePos start = new StructurePos(AncientLayer1LegacyGen.SIZE / 2, AncientLayer1LegacyGen.SIZE / 2);
        AtomicBoolean found = new AtomicBoolean();
        GraphHandler.startBFS(start, (map::connectedStructures), (pos -> true), (pos -> {
            IStructure structure = map.structure(pos);
            found.set(structure != null && structure.type() == EnumStructure.BOSS);
            return found.get();
        }));
        return found.get();
    }
}
