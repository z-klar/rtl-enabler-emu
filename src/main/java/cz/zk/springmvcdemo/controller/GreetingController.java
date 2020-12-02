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

    /**
     *
     * @param model
     * @return
     */
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
    /**
     *
     * @param pars
     * @param model
     * @return
     */
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
        return(greetingService.ProcessVideoRequest(stream, state, port));
    }

    @GetMapping("/test")
    public String displayTest(Model model) {
        return "test";
    }

}
