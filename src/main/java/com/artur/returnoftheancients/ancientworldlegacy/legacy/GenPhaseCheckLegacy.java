package com.artur.returnoftheancients.ancientworldlegacy.legacy;

import com.artur.returnoftheancients.ancientworld.map.gen.GenPhase;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.handlers.GraphHandler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class GenPhaseCheckLegacy extends GenPhase {

    public GenPhaseCheckLegacy(GenPhase parent) {
        super(parent);
    }

    @Override
    public @NotNull ImmutableMap getMap(long seed, int size) {
        ImmutableMap map;
        do {
            map = this.parent.getMap(seed++, size);
        } while (!checkWayToBoss(map));
        return map;
    }

    private boolean checkWayToBoss(ImmutableMap map) {
        StrPos start = new StrPos(AncientLayer1LegacyGen.SIZE / 2, AncientLayer1LegacyGen.SIZE / 2);
        AtomicBoolean found = new AtomicBoolean();
        GraphHandler.startBFS(start, (map::connectedStructures), (pos -> true), (pos -> {
            IStructure structure = map.structure(pos);
//            found.set(structure != null);
            return found.get();
        }));
        return found.get();
    }
}
