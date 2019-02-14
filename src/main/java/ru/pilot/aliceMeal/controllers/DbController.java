package ru.pilot.aliceMeal.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pilot.aliceMeal.helper.DbInitaliser;

@RestController
@RequestMapping("/db")
public class DbController {
    
    public DbController(){
        
    }
    
    @GetMapping("/init")
    public String initFromFile() throws Exception {
        DbInitaliser initaliser = new DbInitaliser();
        initaliser.createStructure();
        return "Init from file finish";
    }
    
}