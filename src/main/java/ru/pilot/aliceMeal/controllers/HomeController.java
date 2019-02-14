package ru.pilot.aliceMeal.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {
    
    @GetMapping("/visits")
    public String home(){
        return "Hello World!";
    }
}