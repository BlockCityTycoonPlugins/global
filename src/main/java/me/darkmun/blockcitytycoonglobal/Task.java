package me.darkmun.blockcitytycoonglobal;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.time.LocalTime;

public class Task {
    private final static long MILLISECONDS_IN_TICK = 50;
    private final static long TICKS_IN_SECOND = 20;
    private boolean works = false;
    private boolean paused = false;
    private long remainingTimeToEnd = 0;
    private final long timeToEnd;
    private int taskID;
    private long runTimeMS;
    private final Runnable task;
    private final Plugin plugin;

    public Task(Plugin plugin, Runnable task, long timeToEnd) {
        this.task = task;
        this.timeToEnd = timeToEnd;
        this.plugin = plugin;
    }

    public void run() {
        works = true;

        remainingTimeToEnd = timeToEnd;
        taskID = Bukkit.getScheduler().runTaskLater(plugin, task, remainingTimeToEnd).getTaskId();
        runTimeMS = System.currentTimeMillis();
    }

    @SuppressWarnings("unused")
    public void pause() {
        if (works) {
            long stopTimeMS = System.currentTimeMillis();
            long timeFromRunningMS = stopTimeMS - runTimeMS;  // milliseconds
            long timeFromRunningInTicks = timeFromRunningMS/MILLISECONDS_IN_TICK;
            if ((remainingTimeToEnd - timeFromRunningInTicks) > 0) {
                Bukkit.getScheduler().cancelTask(taskID);
                remainingTimeToEnd -= timeFromRunningInTicks;
                paused = true;
            } else {
                works = false;
            }
        }
    }

    @SuppressWarnings("unused")
    public void continueTask() {
        if (paused) {
            taskID = Bukkit.getScheduler().runTaskLater(plugin, task, remainingTimeToEnd).getTaskId();
            runTimeMS = System.currentTimeMillis();
            paused = false;
        }
    }

    public void stop() {
        if (works && !paused) {
            Bukkit.getScheduler().cancelTask(taskID);
            remainingTimeToEnd = 0;
            works = false;
        }
    }

    public String getRemainingTimeToEnd() {
        long stopTimeMS = System.currentTimeMillis();
        long timeFromRunningMS = stopTimeMS - runTimeMS;  // milliseconds
        long timeFromRunningInTicks = timeFromRunningMS / MILLISECONDS_IN_TICK;
        long timeLeftInTicks = timeToEnd - timeFromRunningInTicks;
        long timeLeftInSeconds = timeLeftInTicks / TICKS_IN_SECOND;
        return ConvertSecondToHHMMSSString(timeLeftInSeconds);
    }

    private String ConvertSecondToHHMMSSString(long nSecondTime) {
        return LocalTime.MIN.plusSeconds(nSecondTime).toString();
    }
}
