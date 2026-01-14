package com.thc.sprbasic2025.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/popup")
@Controller
public class PopupController {
    @RequestMapping("/{page}")
    public String page(@PathVariable String page){
        return "popup/" + page;
    }
    @RequestMapping("/{page}/{id}")
    public String page(@PathVariable String page, @PathVariable String id){
        return "popup/" + page;
    }
}
