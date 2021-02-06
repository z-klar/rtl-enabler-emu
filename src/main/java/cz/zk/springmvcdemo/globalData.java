package cz.zk.springmvcdemo;

import cz.zk.springmvcdemo.model.SysInfo;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class globalData {

    public int [] dins = new int[2];
    public int [] douts = new int[16];

    public boolean BtnAbtHome, BtnAbtMenu, BnAbtPower;
    public String BtnAbtTouch;

    public boolean VideoAbtRunning, VideoFpkRunning, VideoHudRunning;
    public LocalDateTime LastAbtOn, LastFpkOn, LastHudOn;
    public int AbtPort, FpkPort, HudPort;
    public String JanusIp;
    public SysInfo sysInfo = new SysInfo("1.1.0.0", "2021-02-06");

}
