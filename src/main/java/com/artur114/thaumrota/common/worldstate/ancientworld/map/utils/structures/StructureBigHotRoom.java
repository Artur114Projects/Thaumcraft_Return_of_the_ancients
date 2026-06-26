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

public class StructureBigHotRoom extends StructureCombatRoom {
    public StructureBigHotRoom(StrPos pos) {
        super(EnumRotate.NON, EnumMultiChunkStrType.BIG_HOT_ROOM, pos);

        this.down(16);
    }

    protected StructureBigHotRoom(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    protected void onAllDead() {
        this.openBigDoors(
            new PosMc3IM(-16, 18, 4),
            new PosMc3IM(4, 18, 31),
            new PosMc3IM(31, 18, 4),
            new PosMc3IM(4, 18, -16)
        );
    }

    @Override
    protected int onTriggered() {
        this.closeBigDoors(
            new PosMc3IM(-16, 18, 4),
            new PosMc3IM(4, 18, 31),
            new PosMc3IM(31, 18, 4),
            new PosMc3IM(4, 18, -16)
        );
        return 24;
    }

    @Override
    protected void loadWaves(List<CombatWave> waves) {
        switch (this.rand.nextInt(2)) {
            case 0:
                waves.add(new CombatWave(CombatWave.thenLeft(EntityEldritchGuardian.class, 1), CombatWave.computeCList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 2);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 8);
                })));
                waves.add(new CombatWave(CombatWave.thenLeft(4), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 1);
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.STRENGTH));
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.STRENGTH));
                })));
                waves.add(new CombatWave(CombatWave.thenLeft(3), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 1);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 6);
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.STRENGTH));
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.STRENGTH));
                })));
                waves.add(new CombatWave(CombatWave.ALL_DEAD, CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityInhabitedZombie.class, 4);
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.STRENGTH));
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.STRENGTH));
                })));
                break;
            case 1:
                waves.add(new CombatWave(CombatWave.thenLeft(EntityEldritchGuardian.class, 4), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 4);
                })));
                waves.add(new CombatWave(CombatWave.thenLeft(4), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 1);
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.STRENGTH));
                    list1.add(EntityEntry.of(EntityInhabitedZombie.class, MobEffects.STRENGTH));
                })));
                waves.add(new CombatWave(CombatWave.ALL_DEAD, CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 1);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 6);
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.STRENGTH));
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.STRENGTH));
                })));
                waves.add(new CombatWave(CombatWave.ALL_DEAD, CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityInhabitedZombie.class, 2);
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.REGENERATION));
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.RESISTANCE));
                })));
                break;
        }
    }

    @Override
    protected void loadSpawnArea(List<IBox3IM> add, List<IBox3IM> subtract) {
        // this is generated, don't scare
        add.add(new Box3IM(-6, 18, -6, 22, 19, 22));
        subtract.add(new Box3IM(-3, 18, 14, 2, 19, 19));
        subtract.add(new Box3IM(-3, 18, -3, 2, 19, 2));
        subtract.add(new Box3IM(14, 18, -3, 19, 19, 2));
        subtract.add(new Box3IM(14, 18, 14, 19, 19, 19));
    }

    @Override
    protected void loadTriggerBoxes(List<IBox3IM> list, AtomicBoolean isReversed) {
        // this is generated, don't scare
        list.add(new Box3IM(0, 17, 18, 16, 36, 33));
        list.add(new Box3IM(-17, 17, 0, -2, 36, 16));
        list.add(new Box3IM(0, 17, -17, 16, 36, -2));
        list.add(new Box3IM(18, 17, 0, 33, 36, 16));
        isReversed.set(true);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureBigHotRoom(this);
    }

    @Override
    protected void addLights(List<ILightSource> list) {
        // this is generated, don't scare
        list.add(new LineLightSource(new PosMc3IM(-1, 35, 16), new PosMc3IM(-1, 3, 16), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(16, 3, 16), new PosMc3IM(16, 35, 16), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(16, 35, -1), new PosMc3IM(16, 3, -1), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-1, 3, -1), new PosMc3IM(-1, 35, -1), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 34, -10), new PosMc3IM(7, 27, -10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(8, 27, -10), new PosMc3IM(8, 34, -10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-10, 34, 7), new PosMc3IM(-10, 27, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-10, 27, 8), new PosMc3IM(-10, 34, 8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 34, 25), new PosMc3IM(7, 27, 25), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(8, 27, 25), new PosMc3IM(8, 34, 25), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(25, 34, 7), new PosMc3IM(25, 27, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(25, 27, 8), new PosMc3IM(25, 34, 8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(24, 7, 8), new PosMc3IM(10, 7, 8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(8, 7, -9), new PosMc3IM(8, 7, -2), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 8, 24), new PosMc3IM(7, 8, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-9, 7, 7), new PosMc3IM(-2, 7, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(15, 4, -1), new PosMc3IM(0, 4, -1), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-1, 4, 15), new PosMc3IM(-1, 4, 0), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(16, 5, 15), new PosMc3IM(16, 5, 0), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(8, 1, 7), new PosMc3IM(7, 7, 9), HeatRenderer.HEAT_COLOR, 0.5F, 4.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-8, 35, 23), new PosMc3IM(-8, 1, 23), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(23, 35, -8), new PosMc3IM(23, 1, -8), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-8, 35, -8), new PosMc3IM(-8, 1, -8), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(23, 35, 23), new PosMc3IM(23, 1, 23), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(7, 1, 8), HeatRenderer.HEAT_COLOR, 0.8F, 4.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(13, 36, 13), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(13, 36, 2), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(2, 36, 2), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(2, 36, 13), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 36, 22), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 36, 22), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(22, 36, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(22, 36, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 36, -7), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 36, -7), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-7, 36, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-7, 36, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(8, 38, 8), HeatRenderer.HEAT_COLOR, 0.3F, 4.0F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(3, 1, 13), HeatRenderer.HEAT_COLOR, 0.2F, 6.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(12, 1, 12), HeatRenderer.HEAT_COLOR, 0.2F, 6.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(12, 1, 3), HeatRenderer.HEAT_COLOR, 0.2F, 6.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(2, 1, 2), HeatRenderer.HEAT_COLOR, 0.2F, 6.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(-4, 1, 19), HeatRenderer.HEAT_COLOR, 0.2F, 8.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(-4, 1, -4), HeatRenderer.HEAT_COLOR, 0.2F, 8.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(19, 1, -4), HeatRenderer.HEAT_COLOR, 0.2F, 8.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(19, 1, 19), HeatRenderer.HEAT_COLOR, 0.2F, 8.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(8, 1, 21), HeatRenderer.HEAT_COLOR, 0.2F, 8.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(-6, 1, 7), HeatRenderer.HEAT_COLOR, 0.2F, 8.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(7, 1, -6), HeatRenderer.HEAT_COLOR, 0.2F, 8.0F, 1.5F));
        list.add(new PointLightSource(new PosMc3IM(21, 1, 8), HeatRenderer.HEAT_COLOR, 0.2F, 8.0F, 1.5F));
    }

    @Override
    public void onPlayerEntered(EntityPlayer player) {

    }

    @Override
    public void onPlayerWentOut(EntityPlayer player) {

    }

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
