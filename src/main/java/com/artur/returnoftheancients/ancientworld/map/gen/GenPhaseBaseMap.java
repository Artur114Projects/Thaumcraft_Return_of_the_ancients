package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.*;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenPhaseBaseMap extends GenPhase {
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

        List<Integer> indexes = new ArrayList<>(size * size);
        int minDistance = (size / 2) / 2 + 1;
        int maxDistance = (size / 2) - 1;

        for (int i = 0; i != size * size; i++) {
            int x = i % size;
            int y = i / size;

            if (x >= (size / 2) - minDistance && y >= (size / 2) - minDistance && x <= (size / 2) + minDistance && y <= (size / 2) + minDistance) {
                continue;
            }

            if (x <= (size / 2) - maxDistance || y <= (size / 2) - maxDistance || x >= (size / 2) + maxDistance || y >= (size / 2) + maxDistance) {
                continue;
            }

            indexes.add(i);
        }

        int index = indexes.get(rand.nextInt(indexes.size()));

        int bossX = index % size;
        int bossY = index / size;

        EnumFace face = EnumFace.fromVector(center.getX() - bossX, center.getY() - bossY);
        
        map.insetStructure(EnumMultiChunkStrType.BOSS.create(face.rotateFromDefFace(EnumFace.RIGHT), new StrPos(bossX, bossY)));

        return map;
    }
}
