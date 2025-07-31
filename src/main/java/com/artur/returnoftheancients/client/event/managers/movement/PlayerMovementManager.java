package com.artur.returnoftheancients.client.event.managers.movement;

import com.artur.returnoftheancients.misc.TRAConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@SideOnly(Side.CLIENT)
public class PlayerMovementManager {
    private final Map<String, IMovementTask> WORK_ALONE_TASKS = new HashMap<>();
    private final Map<String, IMovementTask> TASKS = new HashMap<>();
    private final List<String> TO_DELETE = new ArrayList<>();


    public void tickEventPlayerTickEvent(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.START || e.side == Side.SERVER) {
            return;
        }
        int playerDimension = e.player.dimension;
    }

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        WorldClient world = Minecraft.getMinecraft().world;
        Minecraft mc = Minecraft.getMinecraft();

        if (player == null && world == null) {
            if (!TASKS.isEmpty() || !WORK_ALONE_TASKS.isEmpty()) {
                System.out.println("Unloading movement tasks");
                WORK_ALONE_TASKS.clear();
                TASKS.clear();
            }
        }

        if (e.phase != TickEvent.Phase.START || player == null || e.side == Side.SERVER || mc.isGamePaused()) {
            return;
        }

        if (!TO_DELETE.isEmpty()) {
            for (String key : TO_DELETE) {
                IMovementTask task = WORK_ALONE_TASKS.get(key);
                if (task == null) {
                    task = TASKS.get(key);
                }

                if (task != null) {
                    task.onDoneWork();
                }

                WORK_ALONE_TASKS.remove(key);
                TASKS.remove(key);
            }
            if (TRAConfigs.Any.debugMode) {
                System.out.println("Tasks is done work:");
                for (String s : TO_DELETE) {
                    System.out.println(s);
                }
            }
            TO_DELETE.clear();
        }

        callMovementTasks(WORK_ALONE_TASKS, player);

        if (WORK_ALONE_TASKS.isEmpty()) {
            callMovementTasks(TASKS, player);
        }
    }

    private void callMovementTasks(Map<String, IMovementTask> tasks, EntityPlayer player) {
        for (String key : tasks.keySet()) {
            IMovementTask task = tasks.get(key);
            if (task.isDoneWork()) {
                TO_DELETE.add(key);
                return;
            }
            task.move(player);
            if (task.isNeedToWorkAlone()) {
                return;
            }
        }
    }

    public void addMovementTask(IMovementTask task, String name) {
        if (task.isNeedToWorkAlone()) {
            WORK_ALONE_TASKS.put(name, task);
        } else {
            TASKS.put(name, task);
        }
        if (TRAConfigs.Any.debugMode) System.out.println("New movement task [" + name + "]");
    }

    public void removeMovementTask(String name) {
        TO_DELETE.add(name);
    }

    public void removeMovementTask(String... names) {
        TO_DELETE.addAll(Arrays.asList(names));
    }

    public boolean hasTask(String name) {
        return TASKS.containsKey(name) || WORK_ALONE_TASKS.containsKey(name);
    }
}
