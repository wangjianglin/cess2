package io.cess.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doc")
public class SwaggerController {

    @RequestMapping("/{service}")
    public String doc(@PathVariable("service") String service, Model model){

        model.addAttribute("cm", "------");
        return "docs";
    }
}
