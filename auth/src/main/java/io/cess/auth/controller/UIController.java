package io.cess.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class UIController {

    @RequestMapping()
    public String index(){
        return "/ui/index";
    }
}
