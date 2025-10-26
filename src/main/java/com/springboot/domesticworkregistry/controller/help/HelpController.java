package com.springboot.domesticworkregistry.controller.help;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/help")
public class HelpController {

    @GetMapping("/tutorial")
    public String showTutorial() {
        return "help/tutorial";
    }

}
