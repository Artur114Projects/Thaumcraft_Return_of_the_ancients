package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.*;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class GenPhaseBaseMap extends GenPhase {
    private final List<Room> rooms;

    public GenPhaseBaseMap() {
        this.rooms = this.initRooms();
    }

    private List<Room> initRooms() {
        return new ArrayList<>(Arrays.asList(
                new Room(EnumMultiChunkStrType.WATER_ROOM, (size) -> size),
                new Room(EnumMultiChunkStrType.HOT_ROOM, (size) -> size),
                new Room(EnumMultiChunkStrType.BIG_HOT_ROOM, (size) -> size)
        ));
    }

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

        final List<Integer> indexes = this.createIndexesForBoss(size);

        int index = indexes.get(rand.nextInt(indexes.size()));

        int bossX = index % size;
        int bossY = index / size;

        EnumFace face = EnumFace.fromVector(center.getX() - bossX, center.getY() - bossY);

        map.insetStructure(EnumMultiChunkStrType.BOSS.create(face.rotateFromDefFace(EnumFace.RIGHT), new StrPos(bossX, bossY)));
    }

    private void addRooms(ImmutableMap map, Random rand, int size) {
        List<Room> rooms = new ArrayList<>(this.rooms);
        Collections.shuffle(rooms, rand);
        HashSet<Integer> indexes = new HashSet<>();
        for (int i = 0; i != size * size; i++) {
            indexes.add(i);
        }
        Map<Room, Integer> attempts = new HashMap<>();
        for (Room room : rooms) {
            attempts.put(room, room.function.apply(size));
        }

        this.updateIndexes(map, indexes, size);

        while (!rooms.isEmpty()) {
            Iterator<Room> iterator = rooms.iterator();

            while (iterator.hasNext()) {
                Room room = iterator.next();

                this.addRoomToRandPos(map, room, rand, indexes, size);

                int a = attempts.get(room) - 1;
                attempts.put(room, a);

                if (a <= 0) {
                    iterator.remove();
                }
            }
        }
    }

    private void addRoomToRandPos(ImmutableMap map, Room room, Random rand, Set<Integer> indexes, int size) {
        EnumRotate rotate = EnumRotate.values()[rand.nextInt(EnumRotate.values().length)];
        Integer[] integers = indexes.toArray(indexes.toArray(new Integer[0]));
        int r = integers[rand.nextInt(integers.length)];
        StrPos pos = new StrPos(r % size, r / size);
        Set<StrPos> checked = new HashSet<>();

        while (checked.add(pos)) {
            StrPos.MutableStrPos newPos = new StrPos.MutableStrPos(pos);
            boolean flag = true;

            if (!indexes.contains(this.index(newPos, size))) {
                newPos.offset(EnumFace.values()[rand.nextInt(EnumFace.values().length)], rand.nextInt(3) + 1); flag = false;
            }

            boolean isFindGoodRotate = false;
            EnumRotate rotateF = rotate;

            for (int i = 0; i != EnumRotate.values().length; i++) {
                EnumRotate rotateI = EnumRotate.values()[(rotate.ordinal() + i) % EnumRotate.values().length];
                IMultiChunkStrForm.IOffset[] offsets = room.type.form().offsets(newPos, rotateI);
                boolean flagR = true;
                for (IMultiChunkStrForm.IOffset offset : offsets) {
                    if (!indexes.contains(this.index(offset.globalPos(), size))) {
                        flagR = false; break;
                    }
                }
                if (flagR) {
                    rotateF = rotateI; isFindGoodRotate = true; break;
                }
            }

            if (!isFindGoodRotate) {
                IMultiChunkStrForm.IOffset[] offsets = room.type.form().offsets(newPos, rotateF);

                for (IMultiChunkStrForm.IOffset offset : offsets) {
                    if (!indexes.contains(this.index(offset.globalPos(), size))) {
                        newPos.add(offset.localPos().multiply(-1));
                        flag = false;
                    }
                }
            }

            if (flag) {
                this.insertStructure(map, room.type.create(rotateF, newPos), indexes, size); return;
            }

            pos = newPos.toImmutable();
        }
    }

    private void insertStructure(ImmutableMap map, IStructure str, Set<Integer> indexes, int size) {
        map.insetStructure(str); this.updateIndexes(map, indexes, size);
    }

    protected int index(int x, int y, int size) {
        return x + y * size;
    }

    protected int index(StrPos pos, int size) {
        return index(pos.getX(), pos.getY(), size);
    }

    private void updateIndexes(ImmutableMap map, Set<Integer> indexes, int size) {
        final int checkDistance = 1;
        for (int i = 0; i != size * size; i++) {
            int x = i % size;
            int y = i / size;

            if (x == 0 || y == 0 || x == size - 1 || y == size - 1) {
                indexes.remove(i); continue;
            }

            if (map.structureType(x, y) != null) {
                for (int x1 = x - checkDistance; x1 != x + checkDistance + 1; x1++) {
                    for (int y1 = y - checkDistance; y1 != y + checkDistance + 1; y1++) {
                        indexes.remove(x1 + y1 * size);
                    }
                }
            }
        }
    }

    private List<Integer> createIndexesForBoss(int size) {
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

    private static class Room {
        private final Function<Integer, Integer> function;
        private final EnumMultiChunkStrType type;

        private Room(EnumMultiChunkStrType type, Function<Integer, Integer> function) {
            this.function = function;
            this.type = type;
        }
    }
}
