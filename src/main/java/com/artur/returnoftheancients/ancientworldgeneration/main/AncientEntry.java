package com.artur.returnoftheancients.ancientworldgeneration.main;

import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.ancientworldgeneration.util.StructureMap;
import com.artur.returnoftheancients.ancientworldgeneration.util.interfaces.IBuild;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.artur.returnoftheancients.misc.TRAConfigs.AncientWorldSettings;
import static com.artur.returnoftheancients.utils.interfaces.IStructure.settings;

public abstract class AncientEntry implements IBuild, IALGS {
    protected StructureMap map;
    protected boolean isBossSpawn;
    protected boolean isBossDead;
    protected boolean isBuild;

    protected AncientEntry() {
        isBossSpawn = false;
        isBossDead = false;
        isBuild = false;
    }

    protected AncientEntry(NBTTagCompound nbt) {

    }

    protected void update() {

    }

    protected void toNBT() {

    }



    protected void startGen() {
        onClearStart();
        clearArea();
    }

    private void clearArea() {
        clear = true;
    }

    private void genStructuresInWorld() {
        please = true;
    }

    private void reloadLight() {
        reloadLight = true;
    }

    private void stop() {
        reloadLight = false;
        please = false;
        clear = false;
        xtp = 0;
        ytp = 0;
        xtc = 0;
        ytc = 0;
        xtl = -128;
        ytl = -128;
        t = 0;
    }

    private byte xtp = 0;
    private byte ytp = 0;
    private byte xtc = 0;
    private byte ytc = 0;
    private int xtl = -128;
    private int ytl = -128;
    private static byte t = 0;

    private boolean reloadLight = false;
    private boolean please = false;
    private boolean clear = false;
    protected int bossGen = 0;

    @Override
    public void build(World world) {
        if (!world.isRemote) {
            if (please) {
                if (t == AncientWorldSettings.AncientWorldGenerationSettings.structuresGenerationDelay) {
                    t = 0;
                    if (xtp == map.SIZE) {
                        ytp++;
                        xtp = 0;
                        onPlease();
                    }
                    if (ytp == map.SIZE) {
                        ytp = 0;
                        xtp = 0;
                        settings.setRotation(Rotation.NONE);
                        please = false;
                        onReloadLightStart();
                        System.out.println("Reload light");
                        reloadLight();
                        return;
                    }
                    byte structure = map.getStructure(xtp, ytp);
                    byte structureRotate = map.getRotate(xtp, ytp);
                    int cx = 128 - 16 * xtp;
                    int cz = 128 - 16 * ytp;
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
                            bossGen++;
                            if (bossGen == 4) {
                                CustomGenStructure.please(world, cx, 79, cz, BOSS_STRING_ID);
                                bossGen = 0;
                            }
                            break;
                        case 0:
                            break;
                        default:
                            System.out.println("WTF????? " + structure);
                            break;
                    }

                    xtp++;
                }
                t++;
            }
            if (clear) {
                for (byte i = 0; i != AncientWorldSettings.AncientWorldGenerationSettings.numberSetClearPerTick; i++) {
                    if (xtc == map.SIZE) {
                        ytc++;
                        xtc = 0;
                        onClear();
                    }
                    if (ytc == map.SIZE) {
                        ytc = 0;
                        xtc = 0;
                        clear = false;
//                        genAncientEntryWay();
                        onPleaseStart();
                        System.out.println("Generate structures");
                        genStructuresInWorld();
                        return;
                    }
                    int cx = 128 - 16 * xtc;
                    int cz = 128 - 16 * ytc;
                    CustomGenStructure.please(world, cx, 80, cz, AIR_CUBE_STRING_ID);
                    CustomGenStructure.please(world, cx, 80 - 31, cz, AIR_CUBE_STRING_ID);
//                        System.out.println("clear x:" + cx + " z:" + cz);

                    xtc++;
                }
            }
            if (reloadLight) {
                for (int i = 0; i != AncientWorldSettings.AncientWorldGenerationSettings.numberSetReloadLightPerTick; i++) {
                    if (xtl == 144) {
                        ytl++;
                        xtl = -128;
                    }
                    if (ytl == 144) {
                        ytl = -128;
                        xtl = -128;
                        reloadLight = false;
//                        genFinish();
                        onFinish();
                        return;
                    }
                    world.checkLight(new BlockPos(xtl, 84, ytl));
                    xtl++;
                }
            }
        }
    }

    @Override
    public boolean isBuild() {
        return isBuild;
    }
}
