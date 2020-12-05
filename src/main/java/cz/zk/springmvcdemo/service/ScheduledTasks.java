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

        log.debug("### Timer 10 Seconds .....");
        if(gd.VideoAbtRunning) {
            diff = gd.LastAbtOn.until(ted, ChronoUnit.SECONDS);
            log.debug("   ABT running " + diff + " seconds ....");
            if(diff > restartPeriod) {
                log.debug("      restarting .....");
                greetingService.KillProcess("abt");
                delay(1000);
                greetingService.ProcessVideoRequest(1, 1, gd.AbtPort, gd.JanusIp);
            }
        }
        if(gd.VideoFpkRunning) {
            diff = gd.LastFpkOn.until(ted, ChronoUnit.SECONDS);
            log.debug("   FPK running " + diff + " seconds ....");
            if(diff > restartPeriod) {
                log.debug("      restarting .....");
                greetingService.KillProcess("fpk");
                delay(1000);
                greetingService.ProcessVideoRequest(2, 1, gd.FpkPort, gd.JanusIp);
            }
        }
        if(gd.VideoHudRunning) {
            diff = gd.LastHudOn.until(ted, ChronoUnit.SECONDS);
            log.debug("   HUD running " + diff + " seconds ....");
            if(diff > restartPeriod) {
                log.debug("      restarting .....");
                greetingService.KillProcess("hud");
                delay(1000);
                greetingService.ProcessVideoRequest(3, 1, gd.HudPort, gd.JanusIp);
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
