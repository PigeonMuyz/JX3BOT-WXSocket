package io.github.pigeonmuyz.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ManualTimer {
    private static final Logger log = LogManager.getLogger(ManualTimer.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Runnable task;
    String path;
    private boolean isRunning = false;

    public ManualTimer(Runnable task, String path) {
        this.task = task;
        this.path = path;
    }

    public void start(long initialDelay, long period) {
        if (!isRunning) {
            log.info("配置文件定时器启动成功");
            scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
            isRunning = true;
        }
    }

    public void stop() {
        scheduler.shutdownNow();
        isRunning = false;
    }
//     调用示例
//    public static void main(String[] args) {
//        Runnable task = () -> System.out.println("執行任務: " + System.currentTimeMillis());
//        ManualTimer timer = new ManualTimer(task);
//
//        // 啟動定時器，5秒後開始執行，之後每10秒執行一次
//        timer.start(5, 10);
//
//        // 停止定時器
//        // timer.stop();
//    }
}

