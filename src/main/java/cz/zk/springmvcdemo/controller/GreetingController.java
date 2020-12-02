package cz.zk.springmvcdemo.controller;

import cz.zk.springmvcdemo.globalData;
import cz.zk.springmvcdemo.service.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class GreetingController {

    private ProcessBuilder pbAbt;
    private Process procAbt;

    private final Logger log = LoggerFactory.getLogger(GreetingController.class);

    @Autowired
    private globalData gd;
    @Autowired
    private GreetingService greetingService;

    @GetMapping("/enabler")
    public String greeting(Model model) {

        String class0 = "btn btn-danger btn-block";
        String class1 = "btn btn-success btn-block";
        for(int i=0; i<16; i++ ) {
            String relay = String.format("bs_relay%02d", i+1);
            model.addAttribute(relay, gd.douts[i]==0 ? class0 : class1);
        }
        return "greeting";
    }

    @GetMapping("/enabler/video")
    public ResponseEntity<Object> switchVideo(@RequestParam Map<String, String> pars, Model model) {
        String sStream = pars.get("stream");
        String sState = pars.get("state");
        String sPort = pars.get("port");
        int stream = Integer.parseInt(sStream);
        int state = Integer.parseInt(sState);
        int port = sPort.length()==0 ? 0 : Integer.parseInt(sPort);
        log.info("enabler/video: stream=" + stream + ",  state=" + state + ", port="
                + port + "   IN-DOCKER:" + greetingService.isRunningInsideDocker());
        if((state == 1) && (port == 0)) {
            return new ResponseEntity<>("Invalid Port !", HttpStatus.BAD_REQUEST);
        }
        String AbtCmd, FpkCmd, HudCmd, AbtSrc, FpkSrc, HudSrc;
        if(greetingService.isRunningInsideDocker()) {
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
                if(state == 1) {   // START
                    try {
                        log.info("  StartAbt: bash | " + AbtCmd + " | " + sPort + " | " + AbtSrc + " | ");
                        pbAbt = new ProcessBuilder("/bin/bash", AbtCmd, sPort, AbtSrc);
                        procAbt = pbAbt.start();
                        return new ResponseEntity<>("OK: ABT Video streaming started", HttpStatus.OK);
                    }
                    catch(Exception ex) {
                        return new ResponseEntity<>("Unable to start process VIDEO_ABT !", HttpStatus.BAD_REQUEST);
                    }
                }
                else {   //  STOP
                    greetingService.KillProcess("abt");
                    return new ResponseEntity<>("OK ABT Video streaming stopped", HttpStatus.OK);
                }
            case 2:    // FPK
                if(state == 1) {   // START
                    try {
                        pbAbt = new ProcessBuilder("/bin/bash", FpkCmd, sPort, FpkSrc);
                        procAbt = pbAbt.start();
                        return new ResponseEntity<>("OK: FPK Video streaming started", HttpStatus.OK);
                    }
                    catch(Exception ex) {
                        return new ResponseEntity<>("Unable to start process VIDEO_FPK !", HttpStatus.BAD_REQUEST);
                    }
                }
                else {   //  STOP
                    greetingService.KillProcess("fpk");
                    return new ResponseEntity<>("OK: FPK Video streaming stopped", HttpStatus.OK);
                }
            case 3:    // HUD
                if(state == 1) {   // START
                    try {
                        pbAbt = new ProcessBuilder("/bin/bash", HudCmd, sPort, HudSrc);
                        procAbt = pbAbt.start();
                        return new ResponseEntity<>("OK: HuD Video streaming started", HttpStatus.OK);
                    }
                    catch(Exception ex) {
                        return new ResponseEntity<>("Unable to start process VIDEO_HUD !", HttpStatus.BAD_REQUEST);
                    }
                }
                else {   //  STOP
                    greetingService.KillProcess("hud");
                    return new ResponseEntity<>("OK: HuD Video streaming stopped", HttpStatus.OK);
                }
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/test")
    public String displayTest(Model model) {
        return "test";
    }

}
