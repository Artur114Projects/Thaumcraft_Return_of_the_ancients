package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.bananalib.math.m2d.box.IBox2I;
import com.artur114.bananalib.math.m3d.box.Box3IM;
import com.artur114.bananalib.math.m3d.box.IBox3IM;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.client.light.ILightSource;
import com.artur114.thaumrota.client.light.LineLightSource;
import com.artur114.thaumrota.client.light.PointLightSource;
import com.artur114.thaumrota.client.render.fx.HeatRenderer;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.*;
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class StructureLongRoom extends StructureCombatRoom {
    private boolean isSecret = false;

    public StructureLongRoom(EnumRotate rotate, StrPos pos) {
        super(rotate.wrap(EnumRotate.C90), EnumMultiChunkStrType.LONG_ROOM, pos);
    }

    protected StructureLongRoom(StructureLongRoom parent) {
        super(parent);
        this.isSecret = parent.isSecret;
    }

    public void setSecret() {
        this.isSecret = true;
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureLongRoom(this);
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        PosMc3IM blockPos = PosMc3IM.obtain();
        blockPos.setChunk(pos).add(8, 0, 8).setY(this.y);
        if (this.rotate == EnumRotate.NON) {
            blockPos.addX(8);
        } else {
            blockPos.addZ(8);
        }
        StructuresBuildManager.createBuildRequest(world, blockPos, this.type.stringId(this.rotate) + (this.isSecret ? "_secret" : "")).setIgnoreAir().setPosAsXZCenter().build();
        PosMc3IM.release(blockPos);
    }

    @Override
    protected void onAllDead() {
        this.openBigDoors(new PosMc3IM(-15, 2, 4), new PosMc3IM(46, 2, 4));
    }

    @Override
    protected int onTriggered() {
        this.closeBigDoors(new PosMc3IM(-15, 2, 4), new PosMc3IM(46, 2, 4));
        return 24;
    }

    @Override
    protected void loadWaves(List<CombatWave> waves) {
        switch (this.rand.nextInt(2)) {
            case 0:
                waves.add(new CombatWave(CombatWave.thenLeft(EntityEldritchGuardian.class, 1), CombatWave.computeList(list -> {
                    CombatWave.add(list, EntityEldritchGuardian.class, 2);
                    CombatWave.add(list, EntityInhabitedZombie.class, 6);
                })));
                waves.add(new CombatWave(CombatWave.computeList(list -> {
                    CombatWave.add(list, EntityInhabitedZombie.class, 4);
                    CombatWave.add(list, EntityEldritchGuardian.class, 1);
                })));
                break;
            case 1:
                waves.add(new CombatWave(CombatWave.thenLeft(EntityEldritchGuardian.class, 1), CombatWave.computeList(list -> {
                    CombatWave.add(list, EntityEldritchGuardian.class, 2);
                    CombatWave.add(list, EntityInhabitedZombie.class, 2);
                })));
                waves.add(new CombatWave(CombatWave.ALL_DEAD, CombatWave.computeList(list -> {
                    CombatWave.add(list, EntityInhabitedZombie.class, 4);
                    CombatWave.add(list, EntityEldritchGuardian.class, 1);
                })));
                waves.add(new CombatWave(CombatWave.computeList(list -> {
                    CombatWave.add(list, EntityEldritchGuardian.class, 2);
                    list.add(EntityEntry.of(EntityEldritchGuardian.class, MobEffects.SPEED, MobEffects.STRENGTH, MobEffects.REGENERATION));
                })));
                break;
        }
    }

    @Override
    protected void loadSpawnArea(List<IBox3IM> add, List<IBox3IM> subtract) {
        // this is generated, don't scare
        add.add(new Box3IM(31, 2, 4, 45, 3, 12));
        add.add(new Box3IM(-13, 2, 3, 1, 3, 12));
        add.add(new Box3IM(3, 4, 3, 5, 5, 13));
        add.add(new Box3IM(27, 4, 3, 29, 5, 13));
        add.add(new Box3IM(7, 6, 4, 25, 7, 12));

        subtract.add(new Box3IM(-10, 2, 4, 1, 3, 5));
        subtract.add(new Box3IM(-10, 2, 11, 1, 3, 12));
        subtract.add(new Box3IM(31, 2, 4, 42, 3, 5));
        subtract.add(new Box3IM(31, 2, 11, 42, 3, 12));

        if (this.rotate != EnumRotate.NON) {
            subtract.forEach(box -> box.offset(0, 0, -1));
            add.forEach(box -> box.offset(0, 0, -1));
        }
    }

    @Override
    protected void loadTriggerBoxes(List<IBox3IM> list, AtomicBoolean isReversed) {
        list.add(new Box3IM(5, 5, 1, 27, 17, 15));

        if (this.rotate != EnumRotate.NON) {
            list.forEach(box -> box.offset(0, 0, -1));
        }
    }

    @Override
    protected void addLights(List<ILightSource> list) {
        // this is generated, don't scare
        list.add(new LineLightSource(new PosMc3IM(23, 5, 13), new PosMc3IM(8, 5, 13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(8, 5, 2), new PosMc3IM(23, 5, 2), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(32, 1, 2), new PosMc3IM(42, 1, 2), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(42, 1, 13), new PosMc3IM(32, 1, 13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-1, 1, 13), new PosMc3IM(-11, 1, 13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-11, 1, 2), new PosMc3IM(-1, 1, 2), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(3, 11, 14), new PosMc3IM(3, 5, 14), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(3, 11, 1), new PosMc3IM(3, 5, 1), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(28, 11, 1), new PosMc3IM(28, 5, 1), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(28, 11, 14), new PosMc3IM(28, 5, 14), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(45, 6, 3), new PosMc3IM(45, 3, 3), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(45, 6, 12), new PosMc3IM(45, 3, 12), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-14, 6, 12), new PosMc3IM(-14, 3, 12), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-14, 6, 3), new PosMc3IM(-14, 3, 3), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(15, 7, 14), new PosMc3IM(15, 13, 14), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(16, 13, 14), new PosMc3IM(16, 7, 14), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(16, 7, 1), new PosMc3IM(16, 13, 1), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(15, 13, 1), new PosMc3IM(15, 7, 1), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(22, 1, 6), new PosMc3IM(9, 1, 6), HeatRenderer.HEAT_COLOR, 0.8F, 2.4F, 2.0F));
        list.add(new LineLightSource(new PosMc3IM(9, 1, 9), new PosMc3IM(22, 1, 9), HeatRenderer.HEAT_COLOR, 0.8F, 2.4F, 2.0F));
        list.add(new PointLightSource(new PosMc3IM(-11, 5, 1), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(-11, 5, 14), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(42, 5, 14), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(42, 5, 1), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(24, 6, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 6, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(7, 6, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(7, 6, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(3, 4, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(3, 4, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-10, 2, 9), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-10, 2, 6), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(28, 4, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(28, 4, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(41, 2, 6), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(41, 2, 9), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(32, 11, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(32, 11, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(36, 11, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(40, 11, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(40, 11, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 15, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-1, 11, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-5, 11, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-9, 11, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-9, 11, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-5, 11, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-1, 11, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(36, 11, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 15, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(7, 15, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(7, 15, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
    }

    @Override
    public void onPlayerEntered(EntityPlayer player) {

    }

    @Override
    public void onPlayerWentOut(EntityPlayer player) {

    }

    public static class Form extends MultiChunkStrForm {

        @Override
        public IBox2I box(EnumRotate rot) {
            return super.box(rot.wrap(EnumRotate.C90));
        }

        @Override
        public char[][] form() {
            return new char[][] {
                {' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' '},
                {'p','s','s','c','s','p'},
                {' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' '}
            };
        }
    }
}
