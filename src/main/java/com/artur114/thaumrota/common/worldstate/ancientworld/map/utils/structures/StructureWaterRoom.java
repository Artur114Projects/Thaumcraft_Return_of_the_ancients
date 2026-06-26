package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.bananalib.math.m3d.box.Box3IM;
import com.artur114.bananalib.math.m3d.box.IBox3IM;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.client.light.ILightSource;
import com.artur114.thaumrota.client.light.LineLightSource;
import com.artur114.thaumrota.client.light.PointLightSource;
import com.artur114.thaumrota.client.render.fx.HeatRenderer;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StructureWaterRoom extends StructureCombatRoom {
    public StructureWaterRoom(StrPos pos) {
        super(EnumRotate.NON, EnumMultiChunkStrType.WATER_ROOM, pos);
    }

    public StructureWaterRoom(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    protected void onAllDead() {
        this.openBigDoors(
            new PosMc3IM(4, 2, -15),
            new PosMc3IM(-15, 2, 4),
            new PosMc3IM(4, 2, 30),
            new PosMc3IM(30, 2, 4)
        );
    }

    @Override
    protected int onTriggered() {
        this.closeBigDoors(
            new PosMc3IM(4, 2, -15),
            new PosMc3IM(-15, 2, 4),
            new PosMc3IM(4, 2, 30),
            new PosMc3IM(30, 2, 4)
        );
        return 24;
    }

    @Override
    protected void loadWaves(List<CombatWave> waves) {
        switch (this.rand.nextInt(2)) {
            case 0:
                waves.add(new CombatWave(CombatWave.thenLeft(EntityEldritchGuardian.class, 1), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 2);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 8);
                })));
                waves.add(new CombatWave(CombatWave.thenLeft(4), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 2);
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.STRENGTH));
                })));
                waves.add(new CombatWave(CombatWave.thenLeft(2), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 3);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 6);
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.SPEED));
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.SPEED, MobEffects.SPEED, MobEffects.STRENGTH, MobEffects.REGENERATION));
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.SPEED, MobEffects.SPEED, MobEffects.STRENGTH, MobEffects.REGENERATION));
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.SPEED, MobEffects.SPEED, MobEffects.STRENGTH, MobEffects.REGENERATION));
                })));
                break;
            case 1:
                waves.add(new CombatWave(CombatWave.thenLeft(EntityEldritchGuardian.class, 2), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 2);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 6);
                })));
                waves.add(new CombatWave(CombatWave.thenLeft(4), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 1);
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.STRENGTH, MobEffects.REGENERATION));
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.STRENGTH, MobEffects.REGENERATION));
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.STRENGTH));
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.STRENGTH));
                })));
                waves.add(new CombatWave(CombatWave.thenLeft(3), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 1);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 6);
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.RESISTANCE));
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.STRENGTH));
                })));
                waves.add(new CombatWave(CombatWave.ALL_DEAD, CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityInhabitedZombie.class, 4);
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.STRENGTH));
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.RESISTANCE));
                })));
                break;
        }
    }

    @Override
    protected void loadSpawnArea(List<IBox3IM> add, List<IBox3IM> subtract) {
        // this is generated, don't scare
        add.add(new Box3IM(-6, 2, -6, 22, 3, 22));
        add.add(new Box3IM(-10, 7, -10, -3, 8, -3));
        add.add(new Box3IM(-10, 7, -3, -7, 8, -2));
        add.add(new Box3IM(-3, 7, -10, -2, 8, -7));
        add.add(new Box3IM(-3, 7, 23, -2, 8, 26));
        add.add(new Box3IM(-10, 7, 19, -3, 8, 26));
        add.add(new Box3IM(-10, 7, 18, -7, 8, 19));
        add.add(new Box3IM(19, 7, 19, 26, 8, 26));
        add.add(new Box3IM(18, 7, 23, 19, 8, 26));
        add.add(new Box3IM(23, 7, 18, 26, 8, 19));
        add.add(new Box3IM(19, 7, -10, 26, 8, -3));
        add.add(new Box3IM(23, 7, -3, 26, 8, -2));
        add.add(new Box3IM(18, 7, -10, 19, 8, -7));

        subtract.add(new Box3IM(2, 2, 2, 14, 3, 14));
        subtract.add(new Box3IM(-10, 7, -10, -9, 8, -9));
        subtract.add(new Box3IM(-6, 7, -6, -3, 8, -3));
        subtract.add(new Box3IM(-10, 7, 25, -9, 8, 26));
        subtract.add(new Box3IM(-6, 7, 19, -3, 8, 22));
        subtract.add(new Box3IM(21, 2, 21, 22, 3, 22));
        subtract.add(new Box3IM(25, 7, -10, 26, 8, -9));
        subtract.add(new Box3IM(19, 7, -6, 22, 8, -3));
        subtract.add(new Box3IM(19, 7, 19, 22, 8, 22));
        subtract.add(new Box3IM(25, 7, 25, 26, 8, 26));
    }

    @Override
    protected void loadTriggerBoxes(List<IBox3IM> list, AtomicBoolean isReversed) {
        // this is generated, don't scare
        list.add(new Box3IM(2, 2, -17, 14, 10, -4));
        list.add(new Box3IM(-17, 2, 2, -4, 10, 14));
        list.add(new Box3IM(2, 2, 20, 14, 10, 33));
        list.add(new Box3IM(20, 2, 2, 33, 10, 14));
        isReversed.set(true);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureWaterRoom(this);
    }

    @Override
    protected void addLights(List<ILightSource> list) {
        // this is generated, don't scare
        list.add(new LineLightSource(new PosMc3IM(21, 3, -6), new PosMc3IM(21, 4, -6), HeatRenderer.HEAT_COLOR, 0.2F, 1.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(21, 3, 20), new PosMc3IM(21, 4, 20), HeatRenderer.HEAT_COLOR, 0.2F, 1.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-6, 3, 21), new PosMc3IM(-6, 4, 21), HeatRenderer.HEAT_COLOR, 0.2F, 1.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-6, 3, -6), new PosMc3IM(-6, 4, -6), HeatRenderer.HEAT_COLOR, 0.2F, 1.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-13, 18, -7), new PosMc3IM(-13, 9, -7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-13, 9, -6), new PosMc3IM(-13, 18, -6), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-7, 9, -13), new PosMc3IM(-7, 18, -13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-6, 9, -13), new PosMc3IM(-6, 18, -13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 18, -13), new PosMc3IM(7, 11, -13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(8, 11, -13), new PosMc3IM(8, 18, -13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(21, 18, -13), new PosMc3IM(21, 9, -13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(22, 9, -13), new PosMc3IM(22, 18, -13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(28, 18, -7), new PosMc3IM(28, 9, -7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(28, 9, -6), new PosMc3IM(28, 18, -6), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-13, 18, 8), new PosMc3IM(-13, 11, 8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-13, 11, 7), new PosMc3IM(-13, 18, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-13, 9, 21), new PosMc3IM(-13, 18, 21), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-13, 18, 22), new PosMc3IM(-13, 9, 22), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-7, 9, 28), new PosMc3IM(-7, 18, 28), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-6, 18, 28), new PosMc3IM(-6, 9, 28), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(8, 18, 28), new PosMc3IM(8, 11, 28), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 11, 28), new PosMc3IM(7, 18, 28), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(21, 9, 28), new PosMc3IM(21, 18, 28), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(22, 18, 28), new PosMc3IM(22, 9, 28), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(28, 9, 22), new PosMc3IM(28, 18, 22), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(28, 18, 21), new PosMc3IM(28, 9, 21), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(28, 11, 8), new PosMc3IM(28, 18, 8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(28, 18, 7), new PosMc3IM(28, 11, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(10, 0, 19), new PosMc3IM(18, 0, 19), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(19, 0, 10), new PosMc3IM(19, 0, 18), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(18, 0, 10), new PosMc3IM(18, 0, 18), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(18, 0, 18), new PosMc3IM(10, 0, 18), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(5, 0, 19), new PosMc3IM(-3, 0, 19), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-4, 0, 18), new PosMc3IM(-4, 0, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-3, 0, 18), new PosMc3IM(-3, 0, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-3, 0, 18), new PosMc3IM(5, 0, 18), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-3, 0, 5), new PosMc3IM(-3, 0, -3), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-4, 0, -3), new PosMc3IM(-4, 0, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-3, 0, -3), new PosMc3IM(5, 0, -3), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(5, 0, -4), new PosMc3IM(-3, 0, -4), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(19, 0, 5), new PosMc3IM(19, 0, -3), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(18, 0, -3), new PosMc3IM(18, 0, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(18, 0, -3), new PosMc3IM(10, 0, -3), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(10, 0, -4), new PosMc3IM(18, 0, -4), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(24, 5, 24), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 2, 13), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 2, 13), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(13, 2, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(13, 2, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 2, 2), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 2, 2), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(2, 2, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(2, 2, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 20, 13), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(2, 20, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(2, 20, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 20, 2), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 20, 2), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(13, 20, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(13, 20, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 20, 13), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 1, 31), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 1, 31), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(31, 1, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(31, 1, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 1, -16), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 1, -16), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-16, 1, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-16, 1, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
    }

    @Override
    public void onPlayerEntered(EntityPlayer player) {}

    @Override
    public void onPlayerWentOut(EntityPlayer player) {}

    public static class Form extends MultiChunkStrForm {
        @Override
        public char[][] form() {
            return new char[][] {
                    {' ',' ','p',' ',' '},
                    {' ','s','s','s',' '},
                    {'p','s','c','s','p'},
                    {' ','s','s','s',' '},
                    {' ',' ','p',' ',' '}
            };
        }
    }
}
