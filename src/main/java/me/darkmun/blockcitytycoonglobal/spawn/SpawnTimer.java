package me.darkmun.blockcitytycoonglobal.spawn;

import me.darkmun.blockcitytycoonglobal.BlockCityTycoonGlobal;
import me.darkmun.blockcitytycoonglobal.Task;
import me.darkmun.blockcitytycoonglobal.storages.Configs;

public class SpawnTimer {

    private static final long TICKS_PER_SECOND = 20;

    private static final long THIRTY_MINUTES_IN_TICKS = 20 * 60 * 30;
    private boolean spawnEnable;
    private Task spawnTaskTimer;

    public SpawnTimer() {
        this.spawnEnable = false;

        this.spawnTaskTimer = new Task(BlockCityTycoonGlobal.getPlugin(), () -> {
            spawnEnable = true;
            spawnTaskTimer.stop();
        }, TICKS_PER_SECOND * Configs.mainConfig.getLong("spawn.time-to-enable"));
    }

    public void startWork() {
        this.spawnEnable = false;
        spawnTaskTimer.run();
    }

    public String getRemainingTime() {
        return spawnTaskTimer.getRemainingTimeToEnd();
    }

    public boolean isSpawnEnabled() {
        return spawnEnable;
    }
}