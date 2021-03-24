package cz.zk.springmvcdemo.service;


import cz.zk.springmvcdemo.controller.GreetingController;
import cz.zk.springmvcdemo.globalData;
import cz.zk.springmvcdemo.model.AbtButtonsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
public class GreetingService {

    @Autowired
    private globalData gd;

    private final Logger log = LoggerFactory.getLogger(GreetingService.class);

    /**
     *
     * @param pattern
     */
    public void KillProcess(String pattern) {
        ProcessBuilder processBuilder = new ProcessBuilder("ps", "-axg" );
        log.debug("Kill process:  getting process list ...");
        File output = new File("proc.log");
        processBuilder.redirectOutput(output);
        try {
            String InputLine;
            int pid;
            Process process = processBuilder.start();
            process.waitFor();
            BufferedReader vstup = new BufferedReader(new FileReader("proc.log"));
            while((InputLine = vstup.readLine()) != null) {
                if(InputLine.contains(pattern)) {
                    log.debug(InputLine);
                    pid = getPid(InputLine);
                    ProcessBuilder pb = new ProcessBuilder("kill", String.format("%d", pid));
                    Process proc = pb.start();
                    proc.waitFor();
                    log.debug("   - Process[PID=" + pid + "] deleted .....");
                }
            }
            vstup.close();
        }
        catch(Exception ex) {
            log.error("Exception: " + ex.getMessage());
        }
    }

    /**
     *
     * @param inp
     * @return
     */
    private int getPid(String inp) {
        String spom = "";
        int i;
        for(i=0; i<inp.length(); i++) {
            if(inp.charAt(i) != ' ') break;
        }
        int start = i;
        for(i=start; i<inp.length(); i++) {
            if(inp.charAt(i) == ' ') break;
        }
        spom = inp.substring(start, i);
        int ires = Integer.parseInt(spom);
        //log.info("Start=" + start + "    End=" + i + "   SPOM=[" + spom + "]   ->  IRES=" + ires);
        return(ires);
    }

    /**
     *
     * @return
     */
    public Boolean isRunningInsideDocker() {
        try (Stream< String > stream =
                     Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     *
     * @param stream: 1=ABT, 2=FPK, 3=HUD
     * @param state: 1=Switch ON,  0=Switc OFF
     * @param port
     * @return
     */
    public ResponseEntity<Object> ProcessVideoRequest(int stream, int state, int port, String JanusIp) {
        String sPort = String.format("%d", port);
        String AbtCmd, FpkCmd, HudCmd, AbtSrc, FpkSrc, HudSrc;
        ProcessBuilder pb;


        if(isRunningInsideDocker()) {
            AbtCmd = "/etc/video_abt.sh";
            AbtSrc = "/etc/film_abt.mp4";
            FpkCmd = "/etc/video_fpk.sh";
            FpkSrc = "/etc/film_fpk.mp4";
            HudCmd = "/etc/video_hud.sh";
            HudSrc = "/etc/film_hud.mp4";
        }
        else {
            AbtCmd = "./etc/video_abt.sh";
            AbtSrc = "./etc/film_abt.mp4";
            FpkCmd = "./etc/video_fpk.sh";
            FpkSrc = "./etc/film_fpk.mp4";
            HudCmd = "./etc/video_hud.sh";
            HudSrc = "./etc/film_hud.mp4";
        }
        switch(stream) {
            case 1:    // ABT
                gd.AbtPort = port;
                gd.JanusIp = JanusIp;
                if(state == 1) {   // START
                    try {
                        log.info("  StartAbt: bash | " + AbtCmd + " | " + sPort + " | " + AbtSrc + " | " + JanusIp);
                        log.debug("Start VideoABT ....");
                        pb = new ProcessBuilder("/bin/bash", AbtCmd, sPort, AbtSrc, JanusIp);
                        pb.start();
                        gd.VideoAbtRunning = true;
                        gd.LastAbtOn = LocalDateTime.now();
                        return new ResponseEntity<>("OK: ABT Video streaming started", HttpStatus.OK);
                    }
                    catch(Exception ex) {
                        log.error("Exception: " + ex.getMessage());
                        return new ResponseEntity<>("Unable to start process VIDEO_ABT !" + ex.getMessage(), HttpStatus.BAD_REQUEST);
                    }
                }
                else {   //  STOP
                    gd.VideoAbtRunning = false;
                    KillProcess("abt");
                    return new ResponseEntity<>("OK ABT Video streaming stopped", HttpStatus.OK);
                }
            case 2:    // FPK
                gd.FpkPort = port;
                gd.JanusIp = JanusIp;
                if(state == 1) {   // START
                    try {
                        log.debug("Start VideoFPK ....");
                        pb = new ProcessBuilder("/bin/bash", FpkCmd, sPort, FpkSrc, JanusIp);
                        pb.start();
                        gd.VideoFpkRunning = true;
                        gd.LastFpkOn = LocalDateTime.now();
                        return new ResponseEntity<>("OK: FPK Video streaming started", HttpStatus.OK);
                    }
                    catch(Exception ex) {
                        log.error("Exception: " + ex.getMessage());
                        return new ResponseEntity<>("Unable to start process VIDEO_FPK !" + ex.getMessage(), HttpStatus.BAD_REQUEST);
                    }
                }
                else {   //  STOP
                    gd.VideoFpkRunning = false;
                    KillProcess("fpk");
                    return new ResponseEntity<>("OK: FPK Video streaming stopped", HttpStatus.OK);
                }
            case 3:    // HUD
                gd.JanusIp = JanusIp;
                gd.HudPort = port;
                if(state == 1) {   // START
                    try {
                        log.debug("Start VideoHUD ....");
                        pb = new ProcessBuilder("/bin/bash", HudCmd, sPort, HudSrc, JanusIp);
                        pb.start();
                        gd.VideoHudRunning = true;
                        gd.LastHudOn = LocalDateTime.now();
                        return new ResponseEntity<>("OK: HuD Video streaming started", HttpStatus.OK);
                    }
                    catch(Exception ex) {
                        log.error("Exception: " + ex.getMessage());
                        return new ResponseEntity<>("Unable to start process VIDEO_HUD !" + ex.getMessage(), HttpStatus.BAD_REQUEST);
                    }
                }
                else {   //  STOP
                    gd.VideoHudRunning = false;
                    KillProcess("hud");
                    return new ResponseEntity<>("OK: HuD Video streaming stopped", HttpStatus.OK);
                }
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);

    }

    public ResponseEntity<AbtButtonsDTO> getAbtButtonsState() {

        AbtButtonsDTO buttons = new AbtButtonsDTO();
        buttons.setAbtButtonHome(gd.BtnAbtHome);
        buttons.setAbtButtonMenu(gd.BtnAbtMenu);
        buttons.setAbtButtonPower(gd.BnAbtPower);
        buttons.setAbtButtonsMessage1(gd.BtnAbtTouch);
        buttons.setMflButtonsMessage1(gd.BtnMflTouch);
        buttons.setAbtMessage(gd.getAbtMessage());
        return new ResponseEntity<AbtButtonsDTO>(buttons, HttpStatus.OK);
    }
}
