package cz.zk.springmvcdemo.controller;

import cz.zk.springmvcdemo.globalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GreetingController {

    @Autowired
    private globalData gd;

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

    @GetMapping("/test")
    public String displayTest(Model model) {
        return "test";
    }

}
