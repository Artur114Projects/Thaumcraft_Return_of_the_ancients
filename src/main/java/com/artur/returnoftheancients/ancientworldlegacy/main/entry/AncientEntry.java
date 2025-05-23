package com.artur.returnoftheancients.ancientworldlegacy.main.entry;

import com.artur.returnoftheancients.ancientworldlegacy.genmap.AncientEntryMapProvider;
import com.artur.returnoftheancients.ancientworldlegacy.main.AncientWorld;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.structurebuilderlegacy.CustomGenStructure;
import com.artur.returnoftheancients.ancientworldlegacy.util.BuildPhase;
import com.artur.returnoftheancients.ancientworldlegacy.genmap.util.StructureMap;
import com.artur.returnoftheancients.ancientworldlegacy.util.interfaces.IBuild;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.init.InitTileEntity;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.misc.WorldDataFields;
import com.artur.returnoftheancients.util.interfaces.IALGS;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.entities.monster.boss.EntityCultistPortalGreater;
import thaumcraft.common.entities.monster.boss.EntityEldritchGolem;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.Random;
import java.util.UUID;

import static com.artur.returnoftheancients.misc.TRAConfigs.AncientWorldSettings;

public abstract class AncientEntry implements IBuild, IALGS {
    protected static final byte MAX_LOADING = 4;
    protected static final UUID nullUUId = new UUID(0, 0);
    protected static final BlockPos nullPos = new BlockPos(0, 0, 0);
    protected UUID bossUUID = nullUUId;
    protected Entity boss = null;
    private final BuildPhase buildPhase = new BuildPhase();

    protected final Random random = new Random();
    protected BlockPos bossPos = new BlockPos(0 ,0, 0);
    private boolean requestSave = false;
    private boolean delete = false;
    protected boolean isBossSpawn;
    protected int finalTimer = 0;
    protected boolean isBossDead;
    protected byte fogTime = 0;
    protected StructureMap map;
    protected boolean isFinal;
    protected boolean isBuild;
    protected boolean isSleep;
    private byte loadCount;
    protected long mapSeed;

    protected final int pos;

    public AncientEntry(int pos) {
        this.mapSeed = random.nextLong();
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
        if (!nbt.hasKey("finalizing")) error("AncientEntry.class, transferred incorrect NBTTag EC:7");
        isFinal = nbt.getBoolean("finalizing");
        if (!nbt.hasKey("bossPos")) error("AncientEntry.class, transferred incorrect NBTTag EC:5");
        bossPos = NBTToBlockPos(nbt.getCompoundTag("bossPos"));
        if (!nbt.hasKey("isBuild")) error("AncientEntry.class, transferred incorrect NBTTag EC:4");
        isBuild = nbt.getBoolean("isBuild");
        if (!nbt.hasKey("isSleep")) error("AncientEntry.class, transferred incorrect NBTTag EC:8");
        isSleep = nbt.getBoolean("isSleep");
        if (!nbt.hasKey("loadCount")) error("AncientEntry.class, transferred incorrect NBTTag EC:9");
        loadCount = nbt.getByte("loadCount");
        if (!nbt.hasKey("mapSeed")) error("AncientEntry.class, transferred incorrect NBTTag EC:10");
        mapSeed = nbt.getLong("mapSeed");

        if (loadCount > MAX_LOADING) {
            requestToDelete();
        }
    }

    public void update(World world) {
        if (isFinal) {
            finalTimer++;
            if (finalTimer >= 120) {
                requestToDelete();
                finalTimer = 0;
            }
            return;
        }
        if (isSleep) {
            return;
        }
        if (delete) {
            return;
        }
        if (!world.isRemote) {
            if (isBossDead) {
                if (!world.isAnyPlayerWithinRangeAt(bossPos.getX(), bossPos.getY(), bossPos.getZ(), 4)) {
                    CustomGenStructure.gen(world, bossPos.getX() - 3, bossPos.getY() - 30, bossPos.getZ() - 2, "ancient_exit");
                    isFinal = true;
                    requestToSave();
                }
            }
            if (!bossPos.equals(nullPos) && !isBossSpawn) {
                EntityPlayer player = world.getClosestPlayer(bossPos.getX(), bossPos.getY(), bossPos.getZ(), 17, false);
                if (player != null) {
                    isBossSpawn = true;
                    onBossTiger(player, world);
                }
            }
                fogTime++;
            if (fogTime >= 4) {
                fogTime = 0;
                addFog();
            }
        }
    };

    protected abstract void onBossTiger(EntityPlayer player, World world);
    public abstract boolean dead(UUID id);
    protected abstract void onRequestToDelete();
    protected abstract void onBossDead();
    protected abstract void addFog();
    public abstract boolean isBelong(EntityPlayerMP player);
    public abstract boolean sleepPlayer(UUID id);

    public boolean deadBoss(UUID id) {
        if (!bossUUID.equals(nullUUId) && bossUUID.equals(id)) {
            isBossDead = true;
            if (boss != null && (!WorldDataFields.isPrimalBladeDrop || !TRAConfigs.Any.isPrimalBladeOneToWorld)) {
                EntityUtils.entityDropSpecialItem(boss, new ItemStack(InitItems.PRIMAL_BLADE), boss.height / 2.0f);
                WorldData.get().saveData.setBoolean(isPrimalBladeDropKey, true);
                WorldData.get().markDirty();
                WorldDataFields.reload();
            }
            onBossDead();
            requestToSave();
            return true;
        }
        return false;
    }

    public boolean bossJoin(EntityJoinWorldEvent event) {
        if (((int) (event.getEntity().posX + 300) / 10000) == pos && isBossSpawn) {
            if (bossUUID.equals(nullUUId)) {
                bossUUID = event.getEntity().getUniqueID();
                boss = event.getEntity();
            }
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
        nbt.setLong("mapSeed", mapSeed);
        nbt.setBoolean("finalizing", isFinal);
        nbt.setUniqueId("boss", bossUUID);
        nbt.setTag("bossPos", blockPosToNBT(bossPos));
        nbt.setBoolean("isBossSpawn", isBossSpawn);
        nbt.setBoolean("isBossDead", isBossDead);
        nbt.setBoolean("isBuild", isBuild);
        return nbt;
    }

    protected void gen(World world, int x, int y, int z, String name) {
        CustomGenStructure.gen(world, x, y, z, name);
    }

    protected void genBossDoors(World world, BlockPos pos) {
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

    protected Entity getRandomBoss(World world, BlockPos pos) {
        byte q = (byte) random.nextInt(4);
        switch (q) {
            case 0:
                return ItemMonsterPlacer.spawnCreature(world, EntityList.getKey(EntityCultistPortalGreater.class), pos.getX(), pos.getY() + 2, pos.getZ() + 1);
            case 1:
                return ItemMonsterPlacer.spawnCreature(world, EntityList.getKey(EntityEldritchGolem.class), pos.getX(), pos.getY() + 2, pos.getZ() + 1);
            default:
                return ItemMonsterPlacer.spawnCreature(world, EntityList.getKey(EntityEldritchWarden.class), pos.getX(), pos.getY() + 2, pos.getZ() + 1);
        }
    }

    protected ItemStack getPrimordialPearl() {
        return new ItemStack(ItemsTC.primordialPearl, 1, random.nextInt(8));
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
        isSleep = false;
        if (!isBuild) {
            startGen(mapSeed);
        } else {
            map = AncientEntryMapProvider.createAncientEntryMap(new Random(mapSeed));
        }
        if (TRAConfigs.Any.debugMode) System.out.println("AncientEntry pos:" + getPos() + " is wake up!");
        return true;
    }

    public long getMapSeed() {
        return mapSeed;
    }

    protected void requestToDelete() {
        onRequestToDelete();
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

    public void saveFinis() {
        requestSave = false;
    }

    public int getPos() {
        return pos;
    }

    protected void startGen(long seed) {
        System.out.println("Generate ancient entry start pos:" + pos);
        onStart();
        AncientWorld.build(this);
        this.map = AncientEntryMapProvider.createAncientEntryMap(new Random(seed));
        isBuild = false;
        buildPhase.clearArea();
    }
    protected void genAncientEntryWay(World world) {
        for (int y = 0, cordY = 0; cordY < world.getHeight(); y++) {
            cordY = 95 + (32 * y);
            gen(world, 10000 * pos, cordY, 0, ENTRY_WAY_STRING_ID);
        }
        gen(world, 6 + (10000 * pos), 255, 6, "ancient_border_cap");
    }
    @Override
    public void build(World world) {
        if (!world.isRemote) {
            if (!isSleep) {
                if (!buildPhase.isClearStart) {
                    onClearStart();
                    buildPhase.isClearStart = true;
                }
                if (buildPhase.gen) {
                    if (buildPhase.t == TRAConfigs.PerformanceSettings.structuresGenerationDelay) {
                        buildPhase.t = 0;

                        if (buildPhase.xtg == map.SIZE) {
                            buildPhase.ytg++;
                            buildPhase.xtg = 0;
                            onGen(buildPhase.xtg, buildPhase.ytg);
                        }

                        if (buildPhase.ytg == map.SIZE) {
                            buildPhase.ytg = 0;
                            buildPhase.xtg = 0;
                            GenStructure.settings.setRotation(Rotation.NONE);
                            buildPhase.gen = false;
                            onFinalizing();
                            System.out.println("Finalizing pos:" + pos);
                            buildPhase.finalizing();
                            return;
                        }

                        byte deformation = map.getDeformation(buildPhase.xtg, buildPhase.ytg);
                        byte structure = map.getStructure(buildPhase.xtg, buildPhase.ytg);
                        byte structureRotate = map.getRotate(buildPhase.xtg, buildPhase.ytg);
                        int cx = (128 - (16 * buildPhase.xtg)) + (10000 * pos);
                        int cz = (128 - (16 * buildPhase.ytg));
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

                        if (deformation > 0) {
                            CustomGenStructure.addTaskToFirstBuild(state -> {
                                if (MiscHandler.getChance((int) (deformation / 1.5), random)) {
                                    if (state.getBlock().equals(BlocksTC.stoneEldritchTile)) {
                                        return BlocksTC.stoneAncient.getDefaultState();
                                    } else if (state.getBlock().equals(BlocksTC.stoneAncient)) {
                                        return BlocksTC.stoneEldritchTile.getDefaultState();
                                    }
                                }
                                return state;
                            });
                        }

                        switch (structure) {
                            case WAY_ID:
                                gen(world, cx, 80, cz, WAY_STRING_ID + rotate);
                                break;
                            case CROSSROADS_ID:
                                if (random.nextInt(AncientWorldSettings.AncientWorldGenerationSettings.eldritchTrapGenerateChange) == 0) {
                                    gen(world, cx, 80, cz, CROSSROADS_STRING_ID);
                                } else {
                                    gen(world, cx, 80, cz, CROSSROADS_TRAP_STRING_ID);
                                }
                                break;
                            case ENTRY_ID:
                                gen(world, cx, 80, cz, ENTRY_STRING_ID);
                                break;
                            case TURN_ID:
                                gen(world, cx, 80, cz, TURN_STRING_ID + rotate);
                                break;
                            case FORK_ID:
                                gen(world, cx, 80, cz, FORK_STRING_ID + rotate);
                                break;
                            case END_ID:
                                gen(world, cx, 80, cz, END_STRING_ID + rotate);
                                break;
                            case BOSS_ID:
                                buildPhase.bossGen++;
                                if (buildPhase.bossGen == 4) {
                                    CustomGenStructure.addTaskToFirstBuild(state -> {
                                        if (MiscHandler.getChance(10, random)) {
                                            if (state.getBlock().equals(BlocksTC.stoneEldritchTile)) {
                                                return BlocksTC.stoneAncient.getDefaultState();
                                            } else if (state.getBlock().equals(BlocksTC.stoneAncient)) {
                                                return BlocksTC.stoneEldritchTile.getDefaultState();
                                            }
                                        }
                                        return state;
                                    });
                                    gen(world, cx, 79, cz, BOSS_STRING_ID);
                                    buildPhase.bossGen = 0;
                                }
                                break;
                            case 0:
                                break;
                            default:
                                System.out.println("WTF????? " + structure);
                                break;
                        }
                        buildPhase.xtg++;
                    }
                    buildPhase.t++;
                }
                if (buildPhase.clear) {
                    for (byte i = 0; i != TRAConfigs.PerformanceSettings.numberSetClearPerTick; i++) {
                        if (buildPhase.xtc == map.SIZE) {
                            buildPhase.ytc++;
                            buildPhase.xtc = 0;
                            onClear(buildPhase.xtc, buildPhase.ytc);
                        }
                        if (buildPhase.ytc == map.SIZE) {
                            buildPhase.ytc = 0;
                            buildPhase.xtc = 0;
                            buildPhase.clear = false;
                            onGenStart();
                            System.out.println("Generate structures pos:" + pos);
                            buildPhase.genStructuresInWorld();
                            return;
                        }
                        int cx = (128 - 16 * buildPhase.xtc) + (10000 * pos);
                        int cz = (128 - 16 * buildPhase.ytc);
                        CustomGenStructure.clearChunk(world, cx >> 4, cz >> 4);
                        buildPhase.xtc++;
                    }
                }
                if (buildPhase.finalizing) {
                    for (int i = 0; i != TRAConfigs.PerformanceSettings.numberSetReloadLightPerTick; i++) {
                        if (buildPhase.xtf == 144) {
                            buildPhase.ytf++;
                            buildPhase.xtf = -128;
                        }
                        if (buildPhase.ytf == 144) {
                            buildPhase.ytf = -128;
                            buildPhase.xtf = -128;
                            buildPhase.finalizing = false;
                            genAncientEntryWay(world);
                            System.out.println("Generate ancient entry finish pos:" + pos);
                            isBuild = true;
                            requestToSave();
                            onFinal();
                            return;
                        }
                        int x = buildPhase.xtf + (10000 * pos);
                        world.checkLight(buildPhase.pos.setPos(x, 84, buildPhase.ytf));
                        BlockPos pos = buildPhase.pos.setPos(x, 80, buildPhase.ytf);
                        if (world.getBlockState(pos).equals(BlocksTC.stoneAncient.getDefaultState())) {
                            if (world.rand.nextInt(AncientWorldSettings.AncientWorldGenerationSettings.incineratorGenerateChange) == 0) {
                                world.setBlockState(pos, InitTileEntity.FIRE_TRAP.getDefaultState());
                            }
                        }
                        buildPhase.xtf++;
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
