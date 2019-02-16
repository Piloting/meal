package ru.pilot.meal.controllers;

import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pilot.meal.entity.ItemMeal;
import ru.pilot.meal.helper.fileParser.ComponentParser;
import ru.pilot.meal.repository.ComponentRepo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/component")
public class ComponentController {
    
    private final ComponentRepo repo;
    public ComponentController(ComponentRepo repo){
        this.repo = repo;
    }
    
    @GetMapping("/initFromFile")
    public String initFromFile() throws IOException, URISyntaxException {
        ComponentParser componentParser = new ComponentParser(new RussianLuceneMorphology());
        URL resource = Thread.currentThread().getContextClassLoader().getResource("mealFromSite/compositions.csv");
        Files.lines(Paths.get(resource.toURI()), StandardCharsets.UTF_8).forEach(s -> saveMealToDb(componentParser, s));
        return "Init from file finish";
    }

    @GetMapping("/getAll")
    public Iterable<ItemMeal> getAll() {
        return repo.findAll();
    }

    private void saveMealToDb(ComponentParser componentParser, String s) {
        ItemMeal itemMeal = componentParser.parseComponentFileLine(s);
        if (itemMeal != null){
            repo.save(itemMeal);
        }
    }


}