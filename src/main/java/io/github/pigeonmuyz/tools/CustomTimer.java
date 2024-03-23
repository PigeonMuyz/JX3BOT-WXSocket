package io.github.pigeonmuyz.tools;

import java.util.Timer;
import java.util.TimerTask;

public class CustomTimer {
    private Timer timer;
    public CustomTimer() {
        this.timer = new Timer();
    }

    public void start(Runnable task) {
        TimerTask timerTask = new TimerTask() {
            int count = 0;
            @Override
            public void run() {
                task.run();
                count++;
                if (count == 10) {
                    timer.cancel();
                }
            }
        };
        // 定时器每分钟（30秒）执行一次任务
        timer.schedule(timerTask, 0, 30000);
    }
}
