package com.artur.returnoftheancients.client.event.managers;

import com.artur.returnoftheancients.client.audio.SoundTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SoundsManager {
    public final Map<BlockPos, TileSoundEntry<?>> soundTileEntryMap = new HashMap<>();
    public final Minecraft mc = Minecraft.getMinecraft();
    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (e.phase != TickEvent.Phase.START || player == null || mc.isGamePaused()) {
            return;
        }

        Iterator<TileSoundEntry<?>> iterator = this.soundTileEntryMap.values().iterator();

        while (iterator.hasNext()) {
            TileSoundEntry<?> entry = iterator.next();

            if (entry.isInvalid(this.mc.world)) {
                entry.remove(); iterator.remove(); continue;
            }

            double sd = entry.sound == null ? 18.0 : entry.sound.getVolume() > 1.0F ? 18.0 * entry.sound.getVolume() : 18.0;
            double d = entry.pos.distanceSq(player.posX, player.posY, player.posZ);

            if (d > (sd * sd)) {
                if (entry.isPlaying()) {
                    entry.stop();
                }
            } else {
                if (!entry.isPlaying()) {
                    entry.play();
                }
            }
        }
    }

    public <T extends TileEntity> void playTileSound(T tile, SoundFactory<T> factory) {
        TileSoundEntry<?> sound = this.soundTileEntryMap.get(tile.getPos());

        if (sound == null || sound.isInvalid(this.mc.world)) {
            this.soundTileEntryMap.put(tile.getPos(), new TileSoundEntry<>(factory, tile));
        }
    }

    public static class TileSoundEntry<T extends TileEntity> {
        private final SoundFactory<T> factory;
        private final BlockPos pos;
        private SoundTile<T> sound;
        private final int tileDim;
        private final T tile;

        public TileSoundEntry(SoundFactory<T> factory, T tile) {
            this.tileDim = tile.getWorld().provider.getDimension();
            this.factory = factory;
            this.tile = tile;

            this.pos = tile.getPos();
        }

        public SoundTile<T> sound() {
            return this.sound;
        }

        public BlockPos pos() {
            return this.pos;
        }

        public boolean isInvalid(World currentWorld) {
            return this.tile.isInvalid() || currentWorld == null || this.tileDim != currentWorld.provider.getDimension();
        }

        public boolean isPlaying() {
            return this.sound != null;
        }

        public void play() {
            if (this.isPlaying()) {
                this.stop();
            }

            this.sound = this.factory.create(this.tile);

            Minecraft.getMinecraft().getSoundHandler().playSound(this.sound);
        }

        public void stop() {
            this.sound.stop();

            this.sound = null;
        }

        public void remove() {
            if (this.isPlaying()) {
                this.stop();
            }
        }
    }

    public interface SoundFactory<T extends TileEntity> {
        SoundTile<T> create(T tile);
    }
}
