package com.artur.returnoftheancients.events.eventmanagers;

import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class TimerTasksManager {
    private final ArrayList<Task> TASKS_BUFFER = new ArrayList<>();
    private final ArrayList<Task> TASKS = new ArrayList<>();
    private boolean isIterating = false;

    public void tickEventServerTickEvent(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.END || TASKS.isEmpty()) {
            return;
        }

        Iterator<Task> iterator = TASKS.iterator();

        isIterating = true;
        while (iterator.hasNext()) {
            Task task = iterator.next();
            task.tick();

            if (task.isFinish()) {
                iterator.remove();
            }
        }
        isIterating = false;

        if (!TASKS_BUFFER.isEmpty()) {
            TASKS.addAll(TASKS_BUFFER);
            TASKS_BUFFER.clear();
        }
    }

    public void unload() {
        TASKS.clear();
    }

    public void addTask(int time, Runnable finish) {
        if (isIterating) {
            TASKS_BUFFER.add(new Task(finish, time));
        } else {
            TASKS.add(new Task(finish, time));
        }
    }

    private static class Task {
        private boolean isFinish = false;
        private final Runnable finish;
        private final int maxTime;
        private int time;

        protected Task(Runnable finish, int time) {
            this.finish = finish;
            this.maxTime = time;
        }

        protected void tick() {
            if (time >= maxTime) {
                finish.run();
                isFinish = true;
            }

            if (!isFinish) {
                time++;
            }
        }

        protected boolean isFinish() {
            return isFinish;
        }
    }
}
