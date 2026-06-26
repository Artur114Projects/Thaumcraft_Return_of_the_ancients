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

public class StructureHotRoom extends StructureCombatRoom {
    public StructureHotRoom(EnumRotate rotate, StrPos pos) {
        super(rotate, EnumMultiChunkStrType.HOT_ROOM, pos);

        this.down(16);
    }

    protected StructureHotRoom(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    protected void onAllDead() {
        this.openBigDoors(
            new PosMc3IM(-16, 18, 4),
            new PosMc3IM(4, 18, 31),
            new PosMc3IM(31, 18, 4)
        );
    }

    @Override
    protected int onTriggered() {
        this.closeBigDoors(
            new PosMc3IM(-16, 18, 4),
            new PosMc3IM(4, 18, 31),
            new PosMc3IM(31, 18, 4)
        );
        return 24;
    }

    @Override
    protected void loadWaves(List<CombatWave> waves) {
        switch (this.rand.nextInt(2)) {
            case 0:
                waves.add(new CombatWave(CombatWave.thenLeft(EntityEldritchGuardian.class, 1), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 2);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 6);
                })));
                waves.add(new CombatWave(CombatWave.thenLeft(4), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 1);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 3);
                })));
                waves.add(new CombatWave(CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 1);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 8);
                })));
                break;
            case 1:
                waves.add(new CombatWave(CombatWave.thenLeft(EntityEldritchGuardian.class, 1), CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 2);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 6);
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.SPEED));
                })));
                waves.add(new CombatWave(CombatWave.thenLeft(4), CombatWave.computeList(list1 -> {
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.SPEED));
                    CombatWave.add(list1, EntityEldritchGuardian.class, 1);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 3);
                })));
                waves.add(new CombatWave(CombatWave.computeList(list1 -> {
                    CombatWave.add(list1, EntityEldritchGuardian.class, 1);
                    CombatWave.add(list1, EntityInhabitedZombie.class, 2);
                    list1.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.SPEED));
                })));
                break;
        }

    }

    @Override
    protected void loadSpawnArea(List<IBox3IM> add, List<IBox3IM> subtract) {
        add.add(new Box3IM(-8, 18, -4, 24, 19, 24));

        subtract.add(new Box3IM(5, 18, -2, 11, 19, 4));
        subtract.add(new Box3IM(14, 18, 14, 19, 19, 19));
        subtract.add(new Box3IM(-3, 18, 14, 2, 19, 19));
    }

    @Override
    protected void loadTriggerBoxes(List<IBox3IM> list, AtomicBoolean isReversed) {
        list.add(new Box3IM(-17, 17, 0, -3, 36, 16));
        list.add(new Box3IM(0, 17, 19, 16, 36, 33));
        list.add(new Box3IM(19, 17, 0, 33, 36, 16));
        isReversed.set(true);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureHotRoom(this);
    }

    @Override
    protected void addLights(List<ILightSource> list) {
        list.add(new LineLightSource(new PosMc3IM(-1, 35, 16), new PosMc3IM(-1, 2, 16), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(16, 2, 16), new PosMc3IM(16, 35, 16), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 36, 0), new PosMc3IM(8, 1, 1), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(25, 2, -6), new PosMc3IM(25, 35, -6), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-10, 35, -6), new PosMc3IM(-10, 2, -6), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-10, 2, 25), new PosMc3IM(-10, 35, 25), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(25, 35, 25), new PosMc3IM(25, 2, 25), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-12, 34, 7), new PosMc3IM(-12, 27, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-12, 27, 8), new PosMc3IM(-12, 34, 8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 34, 27), new PosMc3IM(7, 27, 27), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(8, 27, 27), new PosMc3IM(8, 34, 27), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(27, 34, 7), new PosMc3IM(27, 27, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(27, 27, 8), new PosMc3IM(27, 34, 8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(5, 36, 15), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 36, 15), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 36, 17), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 36, 17), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(9, 36, 8), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(6, 36, 8), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-9, 36, 6), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-9, 36, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(9, 36, 24), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(6, 36, 24), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 36, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 36, 6), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
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
                {' ',' ',' ',' ',' '}
            };
        }
    }
}
