package com.thc.sprbasic2025.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/board")
@Controller
public class BoardController {
    @RequestMapping("/{page}")
    public String page(@PathVariable String page){
        return "board/" + page;
    }
    @RequestMapping("/{page}/{id}")
    public String page(@PathVariable String page, @PathVariable String id){
        return "board/" + page;
    }
}
