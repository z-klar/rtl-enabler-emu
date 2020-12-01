package cz.zk.springmvcdemo.service;


import cz.zk.springmvcdemo.controller.GreetingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Component
public class GreetingService {

    private final Logger log = LoggerFactory.getLogger(GreetingService.class);

    public void KillProcess(String pattern) {
        ProcessBuilder processBuilder = new ProcessBuilder("ps", "-axg" );
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
                    log.info(InputLine);
                    pid = getPid(InputLine);
                    ProcessBuilder pb = new ProcessBuilder("kill", String.format("%d", pid));
                    Process proc = pb.start();
                    proc.waitFor();
                    log.info("   - Process[PID=" + pid + "] deleted .....");
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
}
