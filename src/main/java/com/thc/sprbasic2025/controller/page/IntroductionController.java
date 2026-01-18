package com.thc.sprbasic2025.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/introduction")
@Controller
public class IntroductionController {
    @RequestMapping("/{page}")
    public String page(@PathVariable String page){
        return "introduction/" + page;
    }
}
