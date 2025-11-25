package com.bebida.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    // Redirigir a la página principal
    @GetMapping("/")
    public String home() {
        return "redirect:/productos";
    }
}
