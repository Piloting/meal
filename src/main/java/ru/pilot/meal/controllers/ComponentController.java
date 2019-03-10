package ru.pilot.meal.controllers;

import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pilot.meal.entity.ItemMeal;
import ru.pilot.meal.helper.TagProcessor;
import ru.pilot.meal.helper.fileParser.ComponentParser;
import ru.pilot.meal.helper.htmlParser.say7.Say7Parser;
import ru.pilot.meal.repository.DbComponentSaver;
import ru.pilot.meal.repository.ItemMealRepo;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/component")
public class ComponentController {
    @Autowired
    private ApplicationContext applicationContext;
    
    private final ItemMealRepo repo;
    public ComponentController(ItemMealRepo repo){
        this.repo = repo;
    }
    
    @GetMapping("/initFromFile")
    public String initFromFile() throws IOException, URISyntaxException {
        ComponentParser componentParser = new ComponentParser(new RussianLuceneMorphology());
        URL resource = Thread.currentThread().getContextClassLoader().getResource("mealFromSite/compositions.csv");
        Files.lines(Paths.get(resource.toURI()), StandardCharsets.UTF_8).forEach(s -> saveMealToDb(componentParser, s));
        return "Init from file finish";
    }
    
    @GetMapping("/initFromSite")
    public String initFromSite() throws Exception {
        // парсинг сайта и сохранение в БД блюд, состава, шагов
        Say7Parser siteParser = new Say7Parser(applicationContext.getBean(DbComponentSaver.class), true);
        siteParser.processParseAndSave();
        
        return "Init from site finish";
    }
    
    @GetMapping("/initTags")
    public String initTags() throws Exception {
        // извлечение и сохранение тегов
        TagProcessor tagProcessor = applicationContext.getBean(TagProcessor.class);
        tagProcessor.setMultiThread(true);
        tagProcessor.prepareTagToAlMeal();
        return "Init tags finish";
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