package com.crmstudy.controller.workbench;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkController {
    @RequestMapping("/workbench/goIndex")
    public String test(){
        return "workbench/index";
    }
}
