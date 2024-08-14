package com.artur.returnoftheancients.ancientworldgeneration.main.entry;

import com.artur.returnoftheancients.ancientworldgeneration.genmap.AncientLabyrinthMap;
import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.ancientworldgeneration.util.BuildPhase;
import com.artur.returnoftheancients.ancientworldgeneration.util.StructureMap;
import com.artur.returnoftheancients.ancientworldgeneration.util.interfaces.IBuild;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import thaumcraft.common.entities.monster.boss.EntityCultistPortalGreater;
import thaumcraft.common.entities.monster.boss.EntityEldritchGolem;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;

import java.util.UUID;
import java.util.logging.Logger;

import static com.artur.returnoftheancients.misc.TRAConfigs.AncientWorldSettings;
import static com.artur.returnoftheancients.utils.interfaces.IStructure.settings;

public abstract class AncientEntry implements IBuild, IALGS {
    protected static final byte MAX_LOADING = 2;
    protected static final BlockPos nullPos = new BlockPos(0, 0, 0);
    protected static final UUID nullUUId = new UUID(0, 0);
    private final BuildPhase buildPhase = new BuildPhase();
    protected BlockPos bossPos = new BlockPos(0 ,0, 0);
    protected UUID bossUUID = new UUID(0, 0);
    protected boolean isSleep;
    private byte loadCount;
    private boolean delete = false;
    private boolean requestSave = false;
    protected StructureMap map;
    protected boolean isFinal;
    protected int finalTimer = 0;
    protected boolean isBossSpawn;
    protected boolean isBossDead;
    protected boolean isBuild;

    protected final int pos;

    public AncientEntry(int pos) {
        this.isBossSpawn = false;
        this.isBossDead = false;
        this.isFinal = false;
        this.isSleep = false;

        this.pos = pos;
    }

    public AncientEntry(NBTTagCompound nbt) {
        if (!nbt.hasKey("pos")) error("AncientEntry.class, transferred incorrect NBTTag EC:0");
        pos = nbt.getInteger("pos");

        if (!nbt.hasKey("bossMost") || !nbt.hasKey("bossLeast")) error("AncientEntry.class, transferred incorrect NBTTag EC:6");
        bossUUID = nbt.getUniqueId("boss");
        if (!nbt.hasKey("isBossSpawn")) error("AncientEntry.class, transferred incorrect NBTTag EC:2");
        isBossSpawn = nbt.getBoolean("isBossSpawn");
        if (!nbt.hasKey("isBossDead")) error("AncientEntry.class, transferred incorrect NBTTag EC:3");
        isBossDead = nbt.getBoolean("isBossDead");
        if (!nbt.hasKey("isFinal")) error("AncientEntry.class, transferred incorrect NBTTag EC:7");
        isFinal = nbt.getBoolean("isFinal");
        if (!nbt.hasKey("bossPos")) error("AncientEntry.class, transferred incorrect NBTTag EC:5");
        bossPos = NBTToBlockPos(nbt.getCompoundTag("bossPos"));
        if (!nbt.hasKey("isBuild")) error("AncientEntry.class, transferred incorrect NBTTag EC:4");
        isBuild = nbt.getBoolean("isBuild");
        if (!nbt.hasKey("isSleep")) error("AncientEntry.class, transferred incorrect NBTTag EC:8");
        isSleep = nbt.getBoolean("isSleep");
        if (!nbt.hasKey("loadCount")) error("AncientEntry.class, transferred incorrect NBTTag EC:9");
        loadCount = nbt.getByte("loadCount");

        if (loadCount >= MAX_LOADING) {
            requestToDelete();
        }
    }

    public void update(World world) {
        if (delete) {
            return;
        }
        if (isFinal) {
            if (finalTimer >= 120) {
                requestToDelete();
            }
            finalTimer++;
        }
        if (!world.isRemote) {
            if (isBossDead) {
                if (!world.isAnyPlayerWithinRangeAt(bossPos.getX(), bossPos.getY(), bossPos.getZ(), 4)) {
                    CustomGenStructure.gen(world, bossPos.getX() - 3, bossPos.getY() - 30, bossPos.getZ() - 2, "ancient_exit");
                    isFinal = true;
                }
            }
            if (!bossPos.equals(nullPos) && !isBossSpawn) {
                EntityPlayer player = world.getClosestPlayer(bossPos.getX(), bossPos.getY(), bossPos.getZ(), 17, false);
                if (player != null) {
                    isBossSpawn = true;
                    onBossTiger(player, world);
                }
            }
        }
    };

    protected abstract void onBossTiger(EntityPlayer player, World world);
    public abstract boolean dead(UUID id);
    public boolean deadBoss(UUID id) {
        if (!bossUUID.equals(nullUUId) && bossUUID.equals(id)) {
            isBossDead = true;
            return true;
        }
        return false;
    }

    public boolean bossJoin(EntityJoinWorldEvent event) {
        if (((int) (event.getEntity().posX + 300) / 10000) == pos && isBossSpawn && !isBossDead) {
            bossUUID = event.getEntity().getUniqueID();
            return true;
        }
        return false;
    }

    public abstract boolean interrupt(UUID id);

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("pos", pos);

        nbt.setByte("loadCount", isSleep ? ((byte) (loadCount + 1)) : 0);
        nbt.setBoolean("isSleep", isSleep);
        nbt.setBoolean("isFinal", isFinal);
        nbt.setUniqueId("boss", bossUUID);
        nbt.setTag("bossPos", blockPosToNBT(bossPos));
        nbt.setBoolean("isBossSpawn", isBossSpawn);
        nbt.setBoolean("isBossDead", isBossDead);
        nbt.setBoolean("isBuild", isBuild);
        return nbt;
    }

    protected void please(World world, int x, int y, int z, String name) {
        CustomGenStructure.gen(world, x, y, z, name);
    }

    protected void pleaseBossDoors(World world, BlockPos pos) {
        CustomGenStructure.gen(world, pos.getX() + 5, pos.getY() + 2, pos.getZ() + 16, "ancient_door");
        CustomGenStructure.gen(world, pos.getX() - 11, pos.getY() + 2, pos.getZ() + 16, "ancient_door");
        CustomGenStructure.gen(world, pos.getX() + 5, pos.getY() + 2, pos.getZ() - 15, "ancient_door");
        CustomGenStructure.gen(world, pos.getX() - 11, pos.getY() + 2, pos.getZ() - 15, "ancient_door");

        CustomGenStructure.gen(world, pos.getX() + 15, pos.getY() + 2, pos.getZ() + 6, "ancient_door1");
        CustomGenStructure.gen(world, pos.getX() + 15, pos.getY() + 2, pos.getZ() - 10, "ancient_door1");
        CustomGenStructure.gen(world, pos.getX() - 16, pos.getY() + 2, pos.getZ() + 6, "ancient_door1");
        CustomGenStructure.gen(world, pos.getX() - 16, pos.getY() + 2, pos.getZ() - 10, "ancient_door1");
    }

    public boolean onBossTriggerBlockAdd(int pos, BlockPos bossPos) {
        if (pos == this.pos) {
            this.bossPos = bossPos;
            return true;
        }
        return false;
    }

    protected EntityLiving getRandomBoss(World world, BlockPos pos) {
        byte q = (byte) HandlerR.genRandomIntRange(0, 2);
        switch (q) {
            case 0:
                EntityCultistPortalGreater p = new EntityCultistPortalGreater(world);
                p.setPositionAndUpdate(pos.getX(), pos.getY() + 2, pos.getZ() + 1);
                return p;
            case 1:
                EntityEldritchGolem g = new EntityEldritchGolem(world);
                g.setPositionAndUpdate(pos.getX(), pos.getY() + 2, pos.getZ() + 1);
                return g;
            case 2:
                EntityEldritchWarden w = new EntityEldritchWarden(world);
                w.setPositionAndUpdate(pos.getX(), pos.getY() + 2, pos.getZ() + 1);
                return w;
        }
        return null;
    }

    protected NBTTagCompound blockPosToNBT(BlockPos pos) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("x", pos.getX());
        nbt.setInteger("y", pos.getY());
        nbt.setInteger("z", pos.getZ());
        return nbt;
    }

    protected BlockPos NBTToBlockPos(NBTTagCompound nbt) {
        if (!nbt.hasKey("x")) error("AncientEntry.NBTToBlockPos(), transferred incorrect NBTTag EC:0");
        if (!nbt.hasKey("y")) error("AncientEntry.NBTToBlockPos(), transferred incorrect NBTTag EC:1");
        if (!nbt.hasKey("z")) error("AncientEntry.NBTToBlockPos(), transferred incorrect NBTTag EC:2");
        return new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
    }

    protected void error(String s) {
        System.out.println(s);
        System.out.println("deleting...");
        requestToDelete();
    }
    public boolean isSleep() {
        return isSleep;
    }
    public boolean wakeUp(EntityPlayerMP player) {
        loadCount = 0;
        return false;
    };

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
        buildPhase.clearArea();
    }
    protected void genAncientEntryWay(World world) {
        for (int y = 0, cordY = 112; cordY < world.getHeight(); y++) {
            cordY = 112 + 32 * y;
            please(world, 10000 * pos, cordY, 0, ENTRY_WAY_STRING_ID);
        }
        please(world, 6 + (10000 * pos), 255, 6, "ancient_border_cap");
        please(world, 4 + (10000 * pos), 124, -14, "ancient_developer_platform");
    }
    @Override
    public void build(World world) {
        if (!world.isRemote) {
            if (!isSleep) {
                if (!buildPhase.isClearStart) {
                    onClearStart();
                    buildPhase.isClearStart = true;
                }
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
                                please(world, cx, 80, cz, WAY_STRING_ID + rotate);
                                break;
                            case CROSSROADS_ID:
                                please(world, cx, 80, cz, CROSSROADS_STRING_ID);
                                break;
                            case ENTRY_ID:
                                please(world, cx, 80, cz, ENTRY_STRING_ID);
                                break;
                            case TURN_ID:
                                please(world, cx, 80, cz, TURN_STRING_ID + rotate);
                                break;
                            case FORK_ID:
                                please(world, cx, 80, cz, FORK_STRING_ID + rotate);
                                break;
                            case END_ID:
                                please(world, cx, 80, cz, END_STRING_ID + rotate);
                                break;
                            case BOSS_ID:
                                buildPhase.bossGen++;
                                if (buildPhase.bossGen == 4) {
                                    please(world, cx, 79, cz, BOSS_STRING_ID);
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
                        please(world, cx, 80, cz, AIR_CUBE_STRING_ID);
                        please(world, cx, 80 - 31, cz, AIR_CUBE_STRING_ID);
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
    }
    @Override
    public boolean isBuild() {
        return isBuild;
    }
}
