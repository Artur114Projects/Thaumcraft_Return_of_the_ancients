package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import com.artur.returnoftheancients.ancientworld.system.utils.AncientWorldPlayer;
import com.artur.returnoftheancients.client.event.ClientEventsHandler;
import com.artur.returnoftheancients.client.fx.particle.ParticleAncientPortal;
import com.artur.returnoftheancients.generation.portal.base.client.ClientAncientPortal;
import com.artur.returnoftheancients.generation.portal.util.OffsetsUtil;
import com.artur.returnoftheancients.structurebuilder.StructureBuildersManager;
import com.artur.returnoftheancients.tileentity.interf.ITileDoor;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
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
    @SideOnly(Side.CLIENT) private boolean isPlayerCollideToWay = false;
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

        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        blockPos.setPos(pos).add(8, 0, 8);

        for (int y = this.y + 32; y < world.getHeight(); y += 11) {
            blockPos.setY(y);
            StructureBuildersManager.createBuildRequest(world, blockPos, "ancient_entry_way").setIgnoreAir().setPosAsXZCenter().build();
        }

        for (int y = this.y - 10; y > -12; y -= 11) {
            blockPos.setY(y);
            StructureBuildersManager.createBuildRequest(world, blockPos, "ancient_entry_way").setIgnoreAir().setPosAsXZCenter().build();
        }

        blockPos.setY(255);
        StructureBuildersManager.createBuildRequest(world, blockPos, "ancient_border_cap").setIgnoreAir().setPosAsXZCenter().build();
        blockPos.setY(0);
        StructureBuildersManager.createBuildRequest(world, blockPos, "ancient_exit").setIgnoreAir().setPosAsXZCenter().build();

        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
    }

    @Override
    protected char[][] structureForm() {
        return new char[][] {
            {' ',' ','p',' ',' '},
            {' ','s','s','s',' '},
            {'p','s','c','s','p'},
            {' ','s','s','s',' '},
            {' ',' ','p',' ',' '}
        };
    }

    @Override
    public void bindWorld(World world) {
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
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        this.addParticles(player.player, this.world, blockPos);
        this.manageMovement(player.player, this.world, blockPos);
        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
    }

    protected void updateServer(List<AncientWorldPlayer> players) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        for (AncientWorldPlayer player : players) {
            if (this.isCollideToWay(blockPos.setPos(player.player))) {
                player.player.fallDistance = 0;
            }
        }
        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
    }

    @SideOnly(Side.CLIENT)
    protected void addParticles(EntityPlayer player, World world, UltraMutableBlockPos util) {
        Minecraft mc = Minecraft.getMinecraft();
        util.setPos(chunkPos).setY(y);
        for (int i = 0; i != 32; i++) {
            util.setPos(chunkPos).setY(MathHelper.floor(player.posY - (i + 1) + 16));
            if (util.getY() > world.getHeight() || util.getY() < 0 || (util.getY() < y + 3 && !this.isDoorOpen())) {
                continue;
            }
            if (util.getY() > y + 2 && util.getY() < y + 16) {
                continue;
            }
            for (BlockPos offset : OffsetsUtil.portalCollideOffsetsArray) {
                if (rand.nextInt(16) == 0) {
                    util.offsetAndCallRunnable(offset, offsetPos -> {
                        mc.effectRenderer.addEffect(new ParticleAncientPortal(world, offsetPos.getX() + rand.nextDouble(), offsetPos.getY() + rand.nextDouble(), offsetPos.getZ() + rand.nextDouble(), 0.2));
                    });
                }
            }
        }
    }

    protected void manageMovement(EntityPlayer player, World world, UltraMutableBlockPos util) {
        if (this.isCollideToWay(util.setPos(player))) {
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
        }
    }

    protected boolean isDoorOpen() {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(this.chunkPos).add(6, this.y + 2, 6);

        TileEntity tile = this.world.getTileEntity(blockPos);

        boolean ret = (tile instanceof ITileDoor) && ((ITileDoor) tile).isOpenOrOpening();

        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);

        return ret;
    }

    public boolean isCollideToWay(BlockPos pos) {
        UltraMutableBlockPos lBlockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        lBlockPos.setPos(pos);
        if (lBlockPos.getChunkX() == this.chunkPos.x && lBlockPos.getChunkZ() == this.chunkPos.z) {
            lBlockPos.setPos(this.chunkPos).setY(0);
            for (BlockPos offset : OffsetsUtil.portalCollideOffsetsArray) {
                lBlockPos.pushPos();
                if (lBlockPos.add(offset).equalsXZ(pos)) {
                    lBlockPos.popPos();
                    UltraMutableBlockPos.returnBlockPosToPoll(lBlockPos);
                    return true;
                }
                lBlockPos.popPos();
            }
        }
        UltraMutableBlockPos.returnBlockPosToPoll(lBlockPos);
        return false;
    }

}
