package com.eshop.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {
    @GetMapping("/")
    public String root() {
        return "forward:/index.html";
    }

    @GetMapping(value = {
            "/{path:^(?!api$|swagger-ui$|api-docs$|csrf$|error$|media$|assets$)[^\\.]*}",
            "/{path:^(?!api$|swagger-ui$|api-docs$|csrf$|error$|media$|assets$)[^\\.]*}/**"
    })
    public String forward() {
        return "forward:/index.html";
    }
}