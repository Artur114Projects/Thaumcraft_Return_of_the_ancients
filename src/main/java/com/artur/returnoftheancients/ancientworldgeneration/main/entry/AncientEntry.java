package com.artur.returnoftheancients.ancientworldgeneration.main.entry;

import com.artur.returnoftheancients.ancientworldgeneration.genmap.AncientLabyrinthMap;
import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.ancientworldgeneration.util.BuildPhase;
import com.artur.returnoftheancients.ancientworldgeneration.util.StructureMap;
import com.artur.returnoftheancients.ancientworldgeneration.util.interfaces.IBuild;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

import static com.artur.returnoftheancients.misc.TRAConfigs.AncientWorldSettings;
import static com.artur.returnoftheancients.utils.interfaces.IStructure.settings;

public abstract class AncientEntry implements IBuild, IALGS {
    private final BuildPhase buildPhase = new BuildPhase();
    private boolean delete = false;
    private boolean requestSave = false;
    protected StructureMap map;
    protected boolean isBossSpawn;
    protected boolean isBossDead;
    protected boolean isBuild;

    protected final int pos;

    public AncientEntry(int pos) {
        isBossSpawn = false;
        isBossDead = false;

        this.pos = pos;
    }

    public AncientEntry(NBTTagCompound nbt) {
        if (!nbt.hasKey("pos")) throw new RuntimeException("AncientEntry.class, transferred incorrect NBTTag EC:0");
        pos = nbt.getInteger("pos");

        if (!nbt.hasKey("map")) throw new RuntimeException("AncientEntry.class, transferred incorrect NBTTag EC:1");
        map = new StructureMap(nbt.getCompoundTag("map"));

        if (!nbt.hasKey("isBossSpawn")) throw new RuntimeException("AncientEntry.class, transferred incorrect NBTTag EC:2");
        isBossSpawn = nbt.getBoolean("isBossSpawn");
        if (!nbt.hasKey("isBossDead")) throw new RuntimeException("AncientEntry.class, transferred incorrect NBTTag EC:3");
        isBossDead = nbt.getBoolean("isBossDead");
        if (!nbt.hasKey("isBuild")) throw new RuntimeException("AncientEntry.class, transferred incorrect NBTTag EC:4");
        isBuild = nbt.getBoolean("isBuild");
    }

    public abstract void update();
    public abstract void dead(UUID id);

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("pos", pos);
        nbt.setTag("map", map.toNBT());

        nbt.setBoolean("isBossSpawn", isBossSpawn);
        nbt.setBoolean("isBossDead", isBossDead);
        nbt.setBoolean("isBuild", isBuild);
        return nbt;
    }

    protected void requestToDelete() {
        delete = true;
    }

    public boolean isRequestToDelete() {
        return delete;
    }

    protected void requestToSave() {
        requestSave = true;
    }

    public boolean isRequestToSave() {
        return requestSave;
    }

    public void saveFinish() {
        requestSave = false;
    }

    public int getPos() {
        return pos;
    }

    protected void startGen() {
        onStart();
        AncientWorld.build(this);
        this.map = AncientLabyrinthMap.genStructuresMap();
        isBuild = false;
        onClearStart();
        buildPhase.clearArea();
    }
    protected void genAncientEntryWay(World world) {
        for (int y = 0, cordY = 112; cordY < world.getHeight(); y++) {
            cordY = 112 + 32 * y;
            CustomGenStructure.please(world, 10000 * pos, cordY, 0, ENTRY_WAY_STRING_ID);
        }
        CustomGenStructure.please(world, 6, 255, 6, "ancient_border_cap");
    }
    @Override
    public void build(World world) {
        if (!world.isRemote) {
            if (buildPhase.please) {
                if (buildPhase.t == AncientWorldSettings.AncientWorldGenerationSettings.structuresGenerationDelay) {
                    buildPhase.t = 0;
                    if (buildPhase.xtp == map.SIZE) {
                        buildPhase.ytp++;
                        buildPhase.xtp = 0;
                        onPlease(buildPhase.xtp, buildPhase.ytp);
                    }
                    if (buildPhase.ytp == map.SIZE) {
                        buildPhase.ytp = 0;
                        buildPhase.xtp = 0;
                        settings.setRotation(Rotation.NONE);
                        buildPhase.please = false;
                        onReloadLightStart();
                        System.out.println("Reload light");
                        buildPhase.reloadLight();
                        return;
                    }
                    byte structure = map.getStructure(buildPhase.xtp, buildPhase.ytp);
                    byte structureRotate = map.getRotate(buildPhase.xtp, buildPhase.ytp);
                    int cx = (128 - 16 * buildPhase.xtp) + (10000 * pos);
                    int cz = (128 - 16 * buildPhase.ytp);
                    byte rotate = 0;
                    switch (structureRotate) {
                        case 1:
                            rotate = 1;
                            break;
                        case 2:
                            rotate = 2;
                            break;
                        case 3:
                            rotate = 3;
                            break;
                        case 4:
                            rotate = 4;
                            break;
                    }
                    switch (structure) {
                        case WAY_ID:
                            CustomGenStructure.please(world, cx, 80, cz, WAY_STRING_ID + rotate);
                            break;
                        case CROSSROADS_ID:
                            CustomGenStructure.please(world, cx, 80, cz, CROSSROADS_STRING_ID);
                            break;
                        case ENTRY_ID:
                            CustomGenStructure.please(world, cx, 80, cz, ENTRY_STRING_ID);
                            break;
                        case TURN_ID:
                            CustomGenStructure.please(world, cx, 80, cz, TURN_STRING_ID + rotate);
                            break;
                        case FORK_ID:
                            CustomGenStructure.please(world, cx, 80, cz, FORK_STRING_ID + rotate);
                            break;
                        case END_ID:
                            CustomGenStructure.please(world, cx, 80, cz, END_STRING_ID + rotate);
                            break;
                        case BOSS_ID:
                            buildPhase.bossGen++;
                            if (buildPhase.bossGen == 4) {
                                CustomGenStructure.please(world, cx, 79, cz, BOSS_STRING_ID);
                                buildPhase.bossGen = 0;
                            }
                            break;
                        case 0:
                            break;
                        default:
                            System.out.println("WTF????? " + structure);
                            break;
                    }

                    buildPhase.xtp++;
                }
                buildPhase.t++;
            }
            if (buildPhase.clear) {
                for (byte i = 0; i != AncientWorldSettings.AncientWorldGenerationSettings.numberSetClearPerTick; i++) {
                    if (buildPhase.xtc == map.SIZE) {
                        buildPhase.ytc++;
                        buildPhase.xtc = 0;
                        onClear(buildPhase.xtc, buildPhase.ytc);
                    }
                    if (buildPhase.ytc == map.SIZE) {
                        buildPhase.ytc = 0;
                        buildPhase.xtc = 0;
                        buildPhase.clear = false;
                        genAncientEntryWay(world);
                        onPleaseStart();
                        System.out.println("Generate structures");
                        buildPhase.genStructuresInWorld();
                        return;
                    }
                    int cx = (128 - 16 * buildPhase.xtc) + (10000 * pos);
                    int cz = (128 - 16 * buildPhase.ytc);
                    CustomGenStructure.please(world, cx, 80, cz, AIR_CUBE_STRING_ID);
                    CustomGenStructure.please(world, cx, 80 - 31, cz, AIR_CUBE_STRING_ID);
                    buildPhase.xtc++;
                }
            }
            if (buildPhase.reloadLight) {
                for (int i = 0; i != AncientWorldSettings.AncientWorldGenerationSettings.numberSetReloadLightPerTick; i++) {
                    if (buildPhase.xtl == 144) {
                        buildPhase.ytl++;
                        buildPhase.xtl = -128;
                    }
                    if (buildPhase.ytl == 144) {
                        buildPhase.ytl = -128;
                        buildPhase.xtl = -128;
                        buildPhase.reloadLight = false;
                        System.out.println("Generate ancient labyrinth finish");
                        isBuild = true;
                        requestToSave();
                        onFinish();
                        return;
                    }
                    int x = buildPhase.xtc + (10000 * pos);
                    world.checkLight(buildPhase.pos.setPos(x, 84, buildPhase.ytl));
                    buildPhase.xtl++;
                }
            }
        }
    }
    @Override
    public boolean isBuild() {
        return isBuild;
    }
}
