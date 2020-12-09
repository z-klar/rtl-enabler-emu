package cz.zk.springmvcdemo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zk.springmvcdemo.globalData;
import cz.zk.springmvcdemo.model.SysInfo;
import cz.zk.springmvcdemo.xmlProcessing;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Api(value="Emulating QUIDO I/O Module")
public class QuidoController {

    private final String VERSION = "1.0.0.0";
    private final Logger log = LoggerFactory.getLogger(QuidoController.class);

    @Autowired
    private globalData gd;

    /***************************************************************************************
     * Relay update
     * @param pars
     * @return
     ***************************************************************************************/
    @RequestMapping(value = "/set.xml", method = RequestMethod.GET)
    @ApiOperation(value = "Set OUTPUT state")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OUTPUT successfully updated"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<Object> updateRelay(
            @ApiParam("URL type parameters:?type=s|r&id=ID")
            @RequestParam Map <String, String> pars) {

        String stype = pars.get("type");
        String sid = pars.get("id");
        int nid = Integer.parseInt(sid);
        log.info(String.format("UpdateRelay - PARAMS: TYPE=[%s]   ID=[%s]", stype, sid));

        if((nid<1) || (nid>16)) {
            log.info("Wrong OUTPUT ID !");
            return new ResponseEntity<>("Wrong OUTPUT ID !", HttpStatus.BAD_REQUEST);
        }

        if(stype.startsWith("r")) gd.douts[nid-1] = 0;
        else if(stype.startsWith(("s"))) gd.douts[nid-1] = 1;
        else {
            log.info("Unknown type !");
            return new ResponseEntity<>("Unknown type !", HttpStatus.BAD_REQUEST);
        }
        // return new ResponseEntity<>(String.format("PARAMS: TYPE=[%s]   ID=[%s]", stype, sid), HttpStatus.OK);

        String sXml = "";
        sXml = xmlProcessing.getCurrentOutputState(nid-1);
        if(sXml.startsWith("ERR")) {
            log.info(sXml);
            return new ResponseEntity<>(sXml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            log.info(sXml);
            return new ResponseEntity<>(sXml, HttpStatus.OK);
        }

    }

    /***************************************************************************************
     * Get the file with current status
     * @return
     ***************************************************************************************/
    @RequestMapping(value = "/fresh.xml", method = RequestMethod.GET , produces = "application/xml")
    @ApiOperation(value = "Provide the current state of all elements")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Object> getStatus() {
        String sXml = "";

        sXml = xmlProcessing.getCurrentState(gd.dins, gd.douts);
        if(sXml.startsWith("ERR")) {
            log.info(sXml);
            return new ResponseEntity<>(sXml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            log.debug(sXml);
            return new ResponseEntity<>(sXml, HttpStatus.OK);
        }
    }


    /***************************************************************************************
     * Relay update
     * @param pars
     * @return
     * GET  /inputs/?type=s&id=2    sets the appropriate input
     ***************************************************************************************/
    @RequestMapping(value = "/inputs", method = RequestMethod.GET)
    @ApiOperation(value = "Set INPUT state")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "INPUT successfully updated"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<Object> updateInput(
            @ApiParam("URL type parameters:?type=s|r&id=ID")
            @RequestParam Map <String, String> pars) {

        String stype = pars.get("type");
        String sid = pars.get("id");
        int nid = Integer.parseInt(sid);
        log.info(String.format("UpdateInput - PARAMS: TYPE=[%s]   ID=[%s]", stype, sid));

        if((nid<1) || (nid>2)) {
            log.info("Wrong INPUT ID !");
            return new ResponseEntity<>("Wrong INPUT ID !", HttpStatus.BAD_REQUEST);
        }

        if(stype.startsWith("r")) gd.dins[nid-1] = 0;
        else if(stype.startsWith(("s"))) gd.dins[nid-1] = 1;
        else {
            log.info("Unknown type !");
            return new ResponseEntity<>("Unknown type !", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.format("PARAMS: TYPE=[%s]   ID=[%s]", stype, sid), HttpStatus.OK);
    }

    @RequestMapping(value = "/sysinfo", method = RequestMethod.GET , produces = "application/json")
    @ApiOperation(value = "Provide the system information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved info"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Object> getSystemInfo() {
        SysInfo si = new SysInfo("1.0.0.3", "2020-12-09 09:45:00");
        String jsonString = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writeValueAsString(si);
            return new ResponseEntity<>(jsonString, HttpStatus.OK);
        }
        catch(Exception ex) {
            return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
