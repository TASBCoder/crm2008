package com.crmstudy.controller.workbench.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/workbench/main/toIndex")
    public String index(){
        return "workbench/main/index";
    }
}
