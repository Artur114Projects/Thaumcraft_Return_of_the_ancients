package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils;

import com.artur114.thaumrota.common.util.math.IArea;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CombatWave {
    private final Predicate<Collection<Class<? extends EntityLiving>>> doNextWave;
    private final ArrayDeque<EntityEntry> entities;
    private int ticksToNextSpawn = 0;

    public CombatWave(Predicate<Collection<Class<? extends EntityLiving>>> doNextWave, Collection<Class<? extends EntityLiving>> entities) {
        this.entities = entities.stream().map(EntityEntry::of).collect(Collectors.toCollection(ArrayDeque::new));
        this.doNextWave = doNextWave;
    }

    public CombatWave(Predicate<Collection<Class<? extends EntityLiving>>> doNextWave, List<EntityEntry> entities) {
        this.entities = new ArrayDeque<>(entities);
        this.doNextWave = doNextWave;
    }

    public boolean shouldNextWave(Collection<Class<? extends EntityLiving>> alive) {
        return this.doNextWave.test(alive);
    }

    public boolean isSpawned() {
        return this.entities.isEmpty();
    }

    public void spawn(Random rand, World world, IArea area, BiConsumer<BlockPos, EntityLiving> doSpawn) {
        if (this.isSpawned()) {
            return;
        }
        if (this.ticksToNextSpawn <= 0) {
            EntityEntry entity = this.entities.poll();
            if (entity == null) return;
            doSpawn.accept(area.fromIndex(rand.nextInt(area.areaSize())), entity.create(world));
            this.ticksToNextSpawn = rand.nextInt(4) + 3;
            return;
        }
        this.ticksToNextSpawn--;
    }

    public static final Predicate<Collection<Class<? extends EntityLiving>>> ALL_DEAD = (Collection::isEmpty);

    public static Predicate<Collection<Class<? extends EntityLiving>>> thenLeft(int count) {
        return classes -> classes.size() <= count;
    }

    public static Predicate<Collection<Class<? extends EntityLiving>>> thenLeft(Class<? extends EntityLiving> type, int count) {
        return classes -> {
            if (count == 1) {
                return !classes.contains(type);
            }
            int find = 0;
            for (Class<? extends EntityLiving> clazz : classes) {
                if (clazz == type) find++;
            }
            return find <= count;
        };
    }

    public static void add(Collection<EntityEntry> list, Class<? extends EntityLiving> clazz, int n) {
        for (int i = 0; i != n; i++) {
            list.add(EntityEntry.of(clazz));
        }
    }

    public static void add(List<Class<? extends EntityLiving>> list, Class<? extends EntityLiving> clazz, int n) {
        for (int i = 0; i != n; i++) {
            list.add(clazz);
        }
    }

    public static List<Class<? extends EntityLiving>> computeCList(Consumer<List<Class<? extends EntityLiving>>> filler) {
        List<Class<? extends EntityLiving>> list = new ArrayList<>();
        filler.accept(list);
        return list;
    }

    public static List<EntityEntry> computeList(Consumer<List<EntityEntry>> filler) {
        List<EntityEntry> list = new ArrayList<>();
        filler.accept(list);
        return list;
    }
}
