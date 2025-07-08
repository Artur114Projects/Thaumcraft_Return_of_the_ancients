package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.*;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.StructureEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GenPhaseCreateBaseMap extends GenPhase {
    @Override
    public @NotNull ImmutableMap getMap(long seed, int size) {
        if (size % 2 == 0) {
            throw new IllegalStateException("The size must not be even!");
        }

        ImmutableMap map = new ImmutableMap(size);
        Random rand = new Random(seed);

        StrPos center = new StrPos(size / 2, size / 2);

        map.insetStructure(EnumMultiChunkStrType.ENTRY.create(EnumRotate.NON, center).up(16));

        for (int i = 0; i != 4; i++) {
            map.insetStructure(EnumStructureType.LADDER.create(EnumRotate.values()[i], center.offset(EnumFace.values()[i + 1 >= 4 ? 0 : i + 1], 2)).up(8));
            map.insetStructure(EnumStructureType.LADDER.create(EnumRotate.values()[i], center.offset(EnumFace.values()[i + 1 >= 4 ? 0 : i + 1], 3)));
        }

        int maxDistance = (size / 2) - 1;
        int minDistance = 4;

        int bossX = (rand.nextInt((maxDistance - minDistance) + 1) + minDistance) * (rand.nextBoolean() ? 1 : -1) + size / 2;
        int bossY = (rand.nextInt((maxDistance - minDistance) + 1) + minDistance) * (rand.nextBoolean() ? 1 : -1) + size / 2;
        // ^ Говно, переделать!
        
        map.insetStructure(EnumMultiChunkStrType.BOSS.create(EnumRotate.NON, new StrPos(bossX, bossY)));

        return map;
    }
}
