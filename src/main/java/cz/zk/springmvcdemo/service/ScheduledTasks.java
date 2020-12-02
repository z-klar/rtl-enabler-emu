package cz.zk.springmvcdemo.service;

import cz.zk.springmvcdemo.globalData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
public class ScheduledTasks {

    private final long restartPeriod = 120;

    @Autowired
    private globalData gd;
    @Autowired
    private GreetingService greetingService;

    @Scheduled(fixedRate = 10000)
    /**
     *
     */
    public void TenSecondTimer() {
        LocalDateTime ted = LocalDateTime.now();
        long diff;

        log.info("### Timer 10 Seconds .....");
        if(gd.VideoAbtRunning) {
            diff = gd.LastAbtOn.until(ted, ChronoUnit.SECONDS);
            log.info("   ABT running " + diff + " seconds ....");
            if(diff > restartPeriod) {
                log.info("      restarting .....");
                greetingService.KillProcess("abt");
                delay(1000);
                greetingService.ProcessVideoRequest(1, 1, gd.AbtPort);
            }
        }
        if(gd.VideoFpkRunning) {
            diff = gd.LastFpkOn.until(ted, ChronoUnit.SECONDS);
            log.info("   FPK running " + diff + " seconds ....");
            if(diff > restartPeriod) {
                log.info("      restarting .....");
                greetingService.KillProcess("fpk");
                delay(1000);
                greetingService.ProcessVideoRequest(2, 1, gd.FpkPort);
            }
        }
        if(gd.VideoHudRunning) {
            diff = gd.LastHudOn.until(ted, ChronoUnit.SECONDS);
            log.info("   HUD running " + diff + " seconds ....");
            if(diff > restartPeriod) {
                log.info("      restarting .....");
                greetingService.KillProcess("hud");
                delay(1000);
                greetingService.ProcessVideoRequest(3, 1, gd.HudPort);
            }
        }
    }
    /**
     *
     * @param period
     */
    private void delay(long period) {
        try {
            Thread.sleep(period);
        }
        catch(Exception ex) {
        }
    }
}
