package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.bananalib.mc.math.m3d.vec.VecMc3D;
import com.artur114.thaumrota.client.light.ILightSource;
import com.artur114.thaumrota.client.light.LineLightSource;
import com.artur114.thaumrota.client.light.PointLightSource;
import com.artur114.thaumrota.client.render.fx.HeatRenderer;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumRotate;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.MultiChunkStrForm;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.StrPos;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.maps.InteractiveMap;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.utils.AncientWorldPlayer;
import com.artur114.thaumrota.common.init.InitItems;
import com.artur114.thaumrota.common.items.ItemPhantomTablet;
import com.artur114.thaumrota.common.tileentity.TileEntityAncientProjector;
import com.artur114.thaumrota.common.tileentity.TileEntityDoorBase;
import com.artur114.thaumrota.common.util.math.BoundingBox;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;

import java.util.List;

public class StructureBoss extends StructureMultiChunk implements IStructureInteractive, IStructureEntityManager, IStructureSerializable {
    private BoundingBox boundingBox = null;
    private boolean isBossSpawn = false;
    private boolean isBossDead = false;
    private ChunkPos chunkPos = null;
    private World world = null;
    private long sessionId;

    public StructureBoss(StrPos pos) {
        super(EnumRotate.NON, EnumMultiChunkStrType.BOSS, pos);
    }

    protected StructureBoss(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureBoss(this);
    }

    @Override
    public InteractiveMap map() {
        return (InteractiveMap) this.map;
    }

    @Override
    public void bindWorld(World world) {
        this.world = world;
    }

    @Override
    public void bindRealPos(ChunkPos pos) {
        this.chunkPos = pos;

        pos = new ChunkPos(pos.x - 1, pos.z - 1);

        this.boundingBox = new BoundingBox(pos.getBlock(0, this.y, 0), new BlockPos(pos.getXStart() + 15, this.y + 32, pos.getZStart() + 20)).rotate(new VecMc3D(this.chunkPos.getBlock(7, 0, 7)).add(0.5, 0.0, 0.5), this.rotate.toBanana());
    }

    @Override
    public void update(List<AncientWorldPlayer> players) {
        if (!this.isBossSpawn && !this.isBossDead && !this.world.isRemote) {
            boolean flag = false;
            for (AncientWorldPlayer player : this.boundingBox.sortCollided(players, (p) -> p.player)) {
                if (!player.isSleep() && player.player.inventory.hasItemStack(new ItemStack(InitItems.PHANTOM_TABLET))) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                this.closeDoor();
                this.updateProjectorState(false);
                this.spawnBoss();

                this.removePhantomTablet(players);
            }
        }
    }

    private void updateProjectorState(boolean state) {
        TileEntity tile = this.world.getTileEntity(this.chunkPos.getBlock(9, this.y + 15, 7));

        if (tile instanceof TileEntityAncientProjector) {
            ((TileEntityAncientProjector) tile).setState(state);
            ((TileEntityAncientProjector) tile).syncTile(false);
        }
    }

    private void sendBossDeadToClient() {
        NBTTagCompound data = new NBTTagCompound();
        data.setBoolean("bossDead", true);
        this.sendToClient(data);
    }

    private void spawnBoss() {
        BlockPos pos = this.chunkPos.getBlock(9, this.y + 3, 7);
        this.spawnEntity(this.world, pos, new EntityEldritchWarden(this.world));
    }

    private void closeDoor() {
        ChunkPos pos = new ChunkPos(this.chunkPos.x - 1, this.chunkPos.z - 1);
        TileEntity tile = this.world.getTileEntity(pos.getBlock(1, this.y + 2, 4));

        if (tile instanceof TileEntityDoorBase) {
            ((TileEntityDoorBase) tile).close();
            ((TileEntityDoorBase) tile).syncTile(false);
        }
    }

    private void removePhantomTablet(List<AncientWorldPlayer> players) {
        for (AncientWorldPlayer player : players) {
            if (!player.isSleep()) {
                for (int i = 0; i != player.player.inventory.getSizeInventory(); i++) {
                    ItemStack stack = player.player.inventory.getStackInSlot(i);
                    if (stack.getItem() instanceof ItemPhantomTablet) {
                        player.player.inventory.deleteStack(stack);
                    }
                }
            }
        }
    }

    private void onBossDead() {
        if (!this.world.isRemote) {
            this.updateProjectorState(true);
        }
        this.isBossSpawn = true;
    }

    @Override
    public void handleServerSyncData(NBTTagCompound data) {
        if (data.hasKey("bossDead")) {
            this.onBossDead();
        }
    }

    @Override
    public boolean loadEntity(EntityLiving entity) {
        boolean flag = entity instanceof EntityEldritchWarden;
        if (flag) this.isBossSpawn = true;
        return flag;
    }

    @Override
    public void unloadEntity(EntityLiving entity) {}

    @Override
    public void onEntityDead(EntityLiving entity) {
        if (entity instanceof EntityEldritchWarden) {
            this.sendBossDeadToClient();
            this.onBossDead();
        }
    }

    @Override
    public void bindSessionId(long id) {
        this.sessionId = id;
    }

    @Override
    public long sessionId() {
        return this.sessionId;
    }

    @Override
    public void onPlayerEntered(EntityPlayer player) {}

    @Override
    public void onPlayerWentOut(EntityPlayer player) {}

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.isBossDead = nbt.getBoolean("isBossDead");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("isBossDead", this.isBossDead);
        return nbt;
    }

    @Override
    protected void addLights(List<ILightSource> list) {
        // this is generated, don't scare
        list.add(new LineLightSource(new PosMc3IM(-1, 22, -15), new PosMc3IM(-1, 4, -15), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(19, 4, -15), new PosMc3IM(19, 22, -15), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(29, 4, -14), new PosMc3IM(29, 23, -14), HeatRenderer.HEAT_COLOR, 0.2F, 4.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(9, 4, -15), new PosMc3IM(9, 22, -15), HeatRenderer.HEAT_COLOR, 0.2F, 4.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(30, 4, 7), new PosMc3IM(30, 22, 7), HeatRenderer.HEAT_COLOR, 0.2F, 4.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(29, 4, 28), new PosMc3IM(29, 22, 28), HeatRenderer.HEAT_COLOR, 0.2F, 4.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(9, 4, 29), new PosMc3IM(9, 22, 29), HeatRenderer.HEAT_COLOR, 0.2F, 4.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-11, 4, 28), new PosMc3IM(-11, 22, 28), HeatRenderer.HEAT_COLOR, 0.2F, 4.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-12, 4, 7), new PosMc3IM(-12, 22, 7), HeatRenderer.HEAT_COLOR, 0.2F, 4.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(30, 4, 18), new PosMc3IM(30, 22, 18), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(30, 22, -4), new PosMc3IM(30, 4, -4), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(19, 4, 29), new PosMc3IM(19, 22, 29), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-1, 22, 29), new PosMc3IM(-1, 4, 29), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-12, 4, 18), new PosMc3IM(-12, 22, 18), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(13, 24, 3), new PosMc3IM(11, 21, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(11, 21, 9), new PosMc3IM(13, 24, 11), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 21, 9), new PosMc3IM(5, 24, 11), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 21, 5), new PosMc3IM(5, 24, 3), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-14, 6, -4), new PosMc3IM(-14, 2, -4), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-14, 2, -13), new PosMc3IM(-14, 6, -13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(12, 3, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(12, 3, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(6, 3, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(6, 3, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(7, 19, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(7, 19, 9), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(11, 19, 9), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(11, 19, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-12, 2, -7), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-12, 2, -10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-10, 2, -12), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-10, 2, -5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(7, 24, -8), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(11, 24, -8), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(17, 24, -8), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 24, -8), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 24, -2), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 24, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 24, 9), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 24, 16), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 24, 22), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(17, 24, 22), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(11, 24, 22), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(7, 24, 22), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(1, 24, 22), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-6, 24, 22), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-6, 24, 16), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-6, 24, 9), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-6, 24, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-6, 24, -8), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(1, 24, -8), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
    }

    public static class Form extends MultiChunkStrForm {
        @Override
        public char[][] form() {
            return new char[][] {
                    {' ',' ',' ',' ',' '},
                    {' ','s','s','s',' '},
                    {' ','s','c','s',' '},
                    {' ','s','s','s','p'},
                    {' ',' ',' ',' ',' '}
            };
        }
    }
}
