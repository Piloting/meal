package ru.pilot.meal.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pilot.meal.entity.Meal;
import ru.pilot.meal.helper.fileParser.MealParser;
import ru.pilot.meal.repository.MealRepo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/meal")
public class MealController {
    
    private final MealRepo repo;
    public MealController(MealRepo repo){
        this.repo = repo;
    }
    
    @GetMapping("/initFromFile")
    public String initFromFile() throws IOException, URISyntaxException {
        MealParser mealParser = new MealParser();
        URL resource = Thread.currentThread().getContextClassLoader().getResource("mealFromSite/meal.csv");
        Files.lines(Paths.get(resource.toURI()), StandardCharsets.UTF_8).forEach(s -> saveMealToDb(mealParser, s));
        return "Init from file finish";
    }

    @GetMapping("/getAll")
    public Iterable<Meal> getAll() {
        return repo.findAll();
    }

    private void saveMealToDb(MealParser mealParser, String s) {
        Meal meal = mealParser.parseMealFileLine(s);
        if (meal != null){
            repo.save(meal);
        }
    }


}