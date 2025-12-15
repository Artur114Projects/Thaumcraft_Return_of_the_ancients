package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.MultiChunkStrForm;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.InteractiveMap;
import com.artur.returnoftheancients.ancientworld.system.utils.AncientWorldPlayer;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.items.ItemPhantomTablet;
import com.artur.returnoftheancients.tileentity.TileEntityAncientProjector;
import com.artur.returnoftheancients.tileentity.TileEntityDoorBase;
import com.artur.returnoftheancients.tileentity.interf.ITileDoor;
import com.artur.returnoftheancients.util.math.BoundingBox;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

        this.boundingBox = new BoundingBox(pos.getBlock(0, this.y, 0), new BlockPos(pos.getXStart() + 15, this.y + 32, pos.getZStart() + 20));
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
