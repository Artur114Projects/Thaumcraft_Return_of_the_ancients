package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
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
import com.artur114.thaumrota.client.event.ClientEventsHandler;
import com.artur114.thaumrota.client.fx.particle.ParticleAncientPortal;
import com.artur114.thaumrota.common.generation.portallegacy.base.client.ClientAncientPortal;
import com.artur114.thaumrota.common.generation.portallegacy.util.PortalOffsets;
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager;
import com.artur114.thaumrota.common.tileentity.interf.ITileDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class StructureEntry extends StructureMultiChunk implements IStructureInteractive {
    private boolean isPlayerCollideToWay = false;
    private ChunkPos chunkPos = null;
    private Random rand = null;
    private World world = null;

    public StructureEntry(StrPos pos) {
        super(EnumRotate.NON, EnumMultiChunkStrType.ENTRY, pos);
    }

    protected StructureEntry(StructureEntry parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureEntry(this);
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        super.build(world, pos, rand);

        PosMc3IM blockPos = PosMc3IM.obtain();
        blockPos.setChunk(pos).add(8, 0, 8);

        for (int y = this.y + 32; y < world.getHeight(); y += 11) {
            blockPos.setY(y);
            StructuresBuildManager.createBuildRequest(world, blockPos, "ancient_entry_way").setIgnoreAir().setPosAsXZCenter().build();
        }

        for (int y = this.y - 10; y > -12; y -= 11) {
            blockPos.setY(y);
            StructuresBuildManager.createBuildRequest(world, blockPos, "ancient_entry_way").setIgnoreAir().setPosAsXZCenter().build();
        }

        blockPos.setY(255);
        StructuresBuildManager.createBuildRequest(world, blockPos, "ancient_border_cap").setIgnoreAir().setPosAsXZCenter().build();
        blockPos.setY(0);
        StructuresBuildManager.createBuildRequest(world, blockPos, "ancient_exit").setIgnoreAir().setPosAsXZCenter().build();

        PosMc3IM.release(blockPos);
    }

    @Override
    public InteractiveMap map() {
        return (InteractiveMap) this.map;
    }

    @Override
    public void bindWorld(World world, long seed) {
        this.rand = new Random();
        this.world = world;
    }

    @Override
    public void bindRealPos(ChunkPos pos) {
        this.chunkPos = pos;
    }

    @Override
    public void onPlayerEntered(EntityPlayer player) {}

    @Override
    public void onPlayerWentOut(EntityPlayer player) {}

    @Override
    public void update(List<AncientWorldPlayer> players) {
        if (this.world == null || this.chunkPos == null) {
            return;
        }

        if (!this.world.isRemote) {
            this.updateServer(players);
        } else {
            this.updateClient(players.get(0));
        }
    }

    @SideOnly(Side.CLIENT)
    protected void updateClient(AncientWorldPlayer player) {
        PosMc3IM blockPos = PosMc3IM.obtain();
        this.addParticles(player.player, this.world, blockPos);
        this.manageMovement(player.player, this.world, blockPos);
        PosMc3IM.release(blockPos);
    }

    protected void updateServer(List<AncientWorldPlayer> players) {
        PosMc3IM blockPos = PosMc3IM.obtain();
        for (AncientWorldPlayer player : players) {
            if (this.isCollideToWay(blockPos.set(player.player))) {
                player.player.fallDistance = 0;
            }
        }
        PosMc3IM.release(blockPos);
    }

    @SideOnly(Side.CLIENT)
    protected void addParticles(EntityPlayer player, World world, PosMc3IM util) {
        Minecraft mc = Minecraft.getMinecraft();
        util.setChunk(chunkPos).setY(y);
        for (int i = 0; i != 32; i++) {
            util.setChunk(chunkPos).setY(MathHelper.floor(player.posY - (i + 1) + 16));
            if (util.getY() > world.getHeight() || util.getY() < 0 || (util.getY() < y + 3 && !this.isDoorOpen())) {
                continue;
            }
            if (util.getY() > y + 2 && util.getY() < y + 16) {
                continue;
            }
            for (BlockPos offset : PortalOffsets.portalCollideOffsetsArray) {
                if (rand.nextInt(16) == 0) {
                    util.pushPos().add(offset);
                    mc.effectRenderer.addEffect(new ParticleAncientPortal(world, util.getX() + rand.nextDouble(), util.getY() + rand.nextDouble(), util.getZ() + rand.nextDouble(), 0.2));
                    util.popPos();
                }
            }
        }
    }

    protected void manageMovement(EntityPlayer player, World world, PosMc3IM util) {
        if (this.isCollideToWay(util.set(player))) {
            boolean hasElevator = ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.hasTask("ELEVATOR_" + this.chunkPos);

            if (player.posY > this.y + 16 && !hasElevator) {
                ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.addMovementTask(new ClientAncientPortal.MovementElevator(ClientAncientPortal.MovementElevator.ElevatingType.DOWN, this.y + 4, 1.5F).setFastStart(), "ELEVATOR_" + this.chunkPos);
            }

            if (player.isSneaking() && player.posY < this.y + 4 && !hasElevator && this.isDoorOpen()) {
                ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.addMovementTask(new ClientAncientPortal.MovementElevator(ClientAncientPortal.MovementElevator.ElevatingType.DOWN, 4, 1.5F).setFastEnd(), "ELEVATOR_" + this.chunkPos);
            }

            if (!hasElevator && Math.floor(player.posY) == this.y + 2 && this.isDoorOpen() && !ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.hasTask("RETENTION_Y_" + this.chunkPos)) {
                ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.addMovementTask(new ClientAncientPortal.MovementRetentionY(this.y + 3.1), "RETENTION_Y_" + this.chunkPos);
            }

            this.isPlayerCollideToWay = true;
        } else if (this.isPlayerCollideToWay) {
            ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.removeMovementTask("RETENTION_Y_" + this.chunkPos);
            ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.removeMovementTask("ELEVATOR_" + this.chunkPos);
            this.isPlayerCollideToWay = false;
        }
    }

    protected boolean isDoorOpen() {
        PosMc3IM blockPos = PosMc3IM.obtain().setChunk(this.chunkPos).add(6, this.y + 2, 6);

        TileEntity tile = this.world.getTileEntity(blockPos);

        boolean ret = (tile instanceof ITileDoor) && ((ITileDoor) tile).isOpenOrOpening();

        PosMc3IM.release(blockPos);

        return ret;
    }

    public boolean isCollideToWay(BlockPos pos) {
        PosMc3IM lBlockPos = PosMc3IM.obtain();
        lBlockPos.set(pos);
        if (lBlockPos.chunkX() == this.chunkPos.x && lBlockPos.chunkZ() == this.chunkPos.z) {
            lBlockPos.setChunk(this.chunkPos).setY(0);
            for (BlockPos offset : PortalOffsets.portalCollideOffsetsArray) {
                lBlockPos.pushPos().add(offset);
                if (lBlockPos.getX() == pos.getX() && lBlockPos.getZ() == pos.getZ()) {
                    lBlockPos.popPos();
                    PosMc3IM.release(lBlockPos);
                    return true;
                }
                lBlockPos.popPos();
            }
        }
        PosMc3IM.release(lBlockPos);
        return false;
    }

    @Override
    protected void addLights(List<ILightSource> list) {
        // this is generated, don't scare
        list.add(new LineLightSource(new PosMc3IM(-5, 1, 21), new PosMc3IM(4, 1, 21), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(5, 1, 22), new PosMc3IM(5, 1, 31), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(10, 1, 31), new PosMc3IM(10, 1, 22), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(12, 1, 21), new PosMc3IM(20, 1, 21), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(21, 1, 20), new PosMc3IM(21, 1, 11), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(22, 1, 10), new PosMc3IM(31, 1, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(31, 1, 5), new PosMc3IM(22, 1, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(21, 1, 4), new PosMc3IM(21, 1, -5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(20, 1, -6), new PosMc3IM(11, 1, -6), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(10, 1, -7), new PosMc3IM(10, 1, -16), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(5, 1, -16), new PosMc3IM(5, 1, -7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(4, 1, -6), new PosMc3IM(-5, 1, -6), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-6, 1, -5), new PosMc3IM(-6, 1, 4), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-7, 1, 5), new PosMc3IM(-16, 1, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-16, 1, 10), new PosMc3IM(-7, 1, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-6, 1, 11), new PosMc3IM(-6, 1, 20), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-8, 18, 7), new PosMc3IM(-8, 11, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-8, 11, 8), new PosMc3IM(-8, 18, 8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-7, 18, -6), new PosMc3IM(-7, 3, -6), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-6, 3, -7), new PosMc3IM(-6, 18, -7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-7, 3, 21), new PosMc3IM(-7, 18, 21), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-6, 18, 22), new PosMc3IM(-6, 3, 22), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 18, 23), new PosMc3IM(7, 11, 23), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(8, 11, 23), new PosMc3IM(8, 18, 23), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 11, -8), new PosMc3IM(7, 18, -8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(8, 18, -8), new PosMc3IM(8, 11, -8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(22, 3, -6), new PosMc3IM(22, 18, -6), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(21, 18, -7), new PosMc3IM(21, 3, -7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(23, 18, 7), new PosMc3IM(23, 11, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(23, 11, 8), new PosMc3IM(23, 18, 8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(22, 3, 21), new PosMc3IM(22, 18, 21), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(21, 18, 22), new PosMc3IM(21, 3, 22), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(15, 17, 15), new PosMc3IM(15, 20, 15), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(15, 17, 0), new PosMc3IM(15, 20, 0), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(0, 17, 0), new PosMc3IM(0, 20, 0), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(0, 17, 15), new PosMc3IM(0, 20, 15), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(10, 20, 10), new PosMc3IM(5, 19, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(5, 20, 5), new PosMc3IM(10, 19, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(-5, 20, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-5, 20, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 20, 20), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 20, 20), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(20, 20, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(20, 20, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 20, -5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 20, -5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
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
