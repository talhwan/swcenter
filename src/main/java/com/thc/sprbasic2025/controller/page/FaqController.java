package com.thc.sprbasic2025.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/faq")
@Controller
public class FaqController {
    @RequestMapping("/{page}")
    public String page(@PathVariable String page){
        return "faq/" + page;
    }
    @RequestMapping("/{page}/{id}")
    public String page(@PathVariable String page, @PathVariable String id){
        return "faq/" + page;
    }
}
