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

        this.addEntry(map, rand, size);
        this.addBoss(map, rand, size);
        this.addRooms(map, rand, size);

        return map;
    }

    private void addEntry(ImmutableMap map, Random rand, int size) {
        StrPos center = new StrPos(size / 2, size / 2);

        map.insetStructure(EnumMultiChunkStrType.ENTRY.create(EnumRotate.NON, center).up(16));

        for (int i = 0; i != 4; i++) {
            map.insetStructure(EnumStructureType.LADDER.create(EnumRotate.values()[i], center.offset(EnumFace.values()[i + 1 >= 4 ? 0 : i + 1], 2)).up(8));
            map.insetStructure(EnumStructureType.LADDER.create(EnumRotate.values()[i], center.offset(EnumFace.values()[i + 1 >= 4 ? 0 : i + 1], 3)));
        }
    }

    private void addBoss(ImmutableMap map, Random rand, int size) {
        StrPos center = new StrPos(size / 2, size / 2);

        final List<Integer> indexes = this.createIndexes(size);

        int index = indexes.get(rand.nextInt(indexes.size()));

        int bossX = index % size;
        int bossY = index / size;

        EnumFace face = EnumFace.fromVector(center.getX() - bossX, center.getY() - bossY);

        map.insetStructure(EnumMultiChunkStrType.BOSS.create(face.rotateFromDefFace(EnumFace.RIGHT), new StrPos(bossX, bossY)));
    }

    private void addRooms(ImmutableMap map, Random rand, int size) {
        for (int i = 0; i < (size / 2); i++) {
            StrPos pos = new StrPos(rand.nextInt(size), rand.nextInt(size));

            if (map.structure(pos) != null) continue;

            IMultiChunkStrForm.IOffset[] offsets = EnumMultiChunkStrType.WATER_ROOM.form().offsets(pos);

            boolean flag = false;
            for (IMultiChunkStrForm.IOffset offset : offsets) {
                if (map.structure(offset.globalPos()) != null) {
                    flag = true;
                    break;
                }

                for (EnumFace face : EnumFace.values()) {
                    if (map.structure(offset.globalPos().offset(face)) != null) {
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    break;
                }

                if (offset.ports().length != 0 && map.structure(offset.globalPos().offset(offset.ports()[0])) != null) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                continue;
            }

            map.insetStructure(EnumMultiChunkStrType.WATER_ROOM.create(EnumRotate.NON, pos));
        }
    }

    private List<Integer> createIndexes(int size) {
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
        return indexes;
    }
}
