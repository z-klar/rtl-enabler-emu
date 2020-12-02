package cz.zk.springmvcdemo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class globalData {

    public int [] dins = new int[2];
    public int [] douts = new int[16];

    public boolean VideoAbtRunning, VideoFpkRunning, VideoHudRunning;
    public LocalDateTime LastAbtOn, LastFpkOn, LastHudOn;
    public int AbtPort, FpkPort, HudPort;

}
