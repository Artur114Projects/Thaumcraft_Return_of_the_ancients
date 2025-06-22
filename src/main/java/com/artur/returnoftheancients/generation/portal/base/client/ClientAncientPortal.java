package com.artur.returnoftheancients.generation.portal.base.client;

import com.artur.returnoftheancients.client.event.ClientEventsHandler;
import com.artur.returnoftheancients.client.event.managers.movement.IMovementTask;
import com.artur.returnoftheancients.client.fx.particle.ParticleAncientPortal;
import com.artur.returnoftheancients.generation.portal.util.OffsetsUtil;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class ClientAncientPortal {

    protected final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

    public final Random random = new Random(System.currentTimeMillis());
    private boolean isPlayerCollideToPortal = false;
    private double particlesSpeed = 0.2D;
    public final String[] movementIds;
    public final ChunkPos portalPos;

    protected final Minecraft mc;

    public final int dimension;
    public final int chunkX;
    public final int chunkZ;

    public final int posX;

    public final int posY;

    public final int posZ;

    public final int id;

    public ClientAncientPortal(NBTTagCompound data) {
        this.dimension = data.getInteger("dimension");
        this.chunkX = data.getInteger("chunkX");
        this.chunkZ = data.getInteger("chunkZ");
        this.portalPos = new ChunkPos(chunkX, chunkZ);
        this.posY = data.getInteger("posY");
        this.id = data.getInteger("id");
        this.mc = Minecraft.getMinecraft();
        this.posX = chunkX << 4;
        this.posZ = chunkZ << 4;

        movementIds = new String[] {"ELEVATOR_" + id, "RETENTION_Y_" + id};
    }

    public boolean isLoaded() {
        if (mc.world == null) {
            return false;
        }
        return !mc.world.getChunkProvider().provideChunk(chunkX, chunkZ).isEmpty();
    }

    public void onUnload() {
        ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.removeMovementTask(movementIds);
    }

    public boolean isCollide(BlockPos pos) {
        UltraMutableBlockPos lBlockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        lBlockPos.setPos(pos);
        if (lBlockPos.getChunkX() == chunkX && lBlockPos.getChunkZ() == chunkZ) {
            lBlockPos.setPos(portalPos).setY(0);
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

    public void update(EntityPlayer player, World world) {
        if (this.isCollide(blockPos.setPos(player)) && player.dimension == dimension) {
            this.updateCollidedPlayer(player);
            isPlayerCollideToPortal = true;
        } else if (isPlayerCollideToPortal) {
            ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.removeMovementTask(movementIds);
            isPlayerCollideToPortal = false;
            particlesSpeed = 0.2D;
        }

        addParticles(player, world);
    }

    public void updateCollidedPlayer(EntityPlayer player) {
        boolean hasElevator = ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.hasTask(movementIds[0]);
        int currentY = MathHelper.floor(player.posY);

        if (player.isSneaking() && !hasElevator && (player.posY - 1.5D) <= posY) {
            ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.addMovementTask(new MovementElevator(MovementElevator.ElevatingType.DOWN, 4, 1.25F).setFastEnd(), movementIds[0]);
            particlesSpeed = 0.2D;
        }

        if (!hasElevator) {
            particlesSpeed = 0.2D;
        }

        if (posY > currentY && !hasElevator) {
            ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.addMovementTask(new MovementElevator(MovementElevator.ElevatingType.UP, posY, 1.25F), movementIds[0]);
            particlesSpeed = 0.5D;
        } else if (posY == currentY && !hasElevator && !ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.hasTask(movementIds[1])) {
            ClientEventsHandler.PLAYER_MOVEMENT_MANAGER.addMovementTask(new MovementRetentionY(posY + 1.1), movementIds[1]);
            particlesSpeed = 0.2D;
        }
    }

    protected void addParticles(EntityPlayer player, World world) {
        blockPos.setPos(player);
        if (blockPos.distance(portalPos) == 0) {
            blockPos.setPos(portalPos).setY(posY);
            for (int i = 0; i != (player.posY - 16 < posY ? 32 : 0); i++) {
                blockPos.setPos(portalPos).setY(MathHelper.floor(player.posY - (i + 1) + 16));
                if (blockPos.getY() >= posY || blockPos.getY() < 3) {
                    continue;
                }
                for (BlockPos offset : OffsetsUtil.portalCollideOffsetsArray) {
                    if (random.nextInt(16) == 0) {
                        blockPos.offsetAndCallRunnable(offset, offsetPos -> {
                            mc.effectRenderer.addEffect(new ParticleAncientPortal(world, offsetPos.getX() + random.nextDouble(), offsetPos.getY() + random.nextDouble(), offsetPos.getZ() + random.nextDouble(), particlesSpeed));
                        });
                    }
                }
            }
        }
    }

    public static class MovementElevator implements IMovementTask {
        private float speedPercent = 0.25F;
        private final ElevatingType type;
        private boolean fastEnd = false;
        private final float speed;
        private int lastY = -100;
        private final int toY;


        public MovementElevator(ElevatingType type, int toY, float speed) {
            this.speed = type == ElevatingType.UP ? speed : -speed;
            this.type = type;
            this.toY = toY;
        }

        @Override
        public void move(EntityPlayer player) {
            int currentY = MathHelper.floor(player.posY);

            if (Math.abs(toY - currentY) <= 20 && !this.fastEnd) {
                speedPercent = MiscHandler.interpolate(speedPercent, 0.25F, 0.12F);
            } else {
                speedPercent = MiscHandler.interpolate(speedPercent, 1.0F, 0.05F);
            }

            player.motionY = speed * speedPercent;

            lastY = currentY;
        }

        public MovementElevator setFastStart() {
            this.speedPercent = 1.0F;
            return this;
        }

        public MovementElevator setFastEnd() {
            this.fastEnd = true;
            return this;
        }

        @Override
        public boolean isDoneWork() {
            return (type == ElevatingType.UP ? lastY >= toY : lastY <= toY) && lastY != -100;
        }

        @Override
        public boolean isNeedToWorkAlone() {
            return true;
        }

        public boolean isUp() {
            return type == ElevatingType.UP;
        }

        public enum ElevatingType {
            UP, DOWN
        }
    }

    public static class MovementRetentionY implements IMovementTask {
        private boolean isDoneWork = false;
        private int tickToLevitate = 0;
        private final double needY;
        private float tick = 0;

        public MovementRetentionY(double needY) {
            this.needY = needY;
        }

        @Override
        public void move(EntityPlayer player) {
            isDoneWork = player.isSneaking();
            double localNeedY = needY;


            if (tickToLevitate >= 0) {
                localNeedY += (Math.cos((tick % (Math.PI * 2))) / 5.0F) + 0.1;
            } else {
                tickToLevitate++;
            }

            if (Math.abs(player.posY - localNeedY) > 2) {
                isDoneWork = true;
                return;
            }

            if (player.posY != localNeedY) {
                player.motionY = (localNeedY - player.posY) / 12.0D;
            }

            tick += 0.14F;
        }

        @Override
        public boolean isDoneWork() {
            return isDoneWork;
        }

        @Override
        public boolean isNeedToWorkAlone() {
            return true;
        }
    }
}
