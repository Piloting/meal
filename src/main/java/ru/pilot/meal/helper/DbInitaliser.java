package ru.pilot.meal.helper;


import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pilot.meal.entity.ItemMeal;
import ru.pilot.meal.helper.fileParser.ComponentParser;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class DbInitaliser {
    
    @Autowired
    private DataSource dataSource;
    
    public void createStructure() throws SQLException, LiquibaseException {
        
        Liquibase liquibase = new Liquibase("changelog.xml", new ClassLoaderResourceAccessor(),  new JdbcConnection(dataSource.getConnection()));
        liquibase.forceReleaseLocks();
        liquibase.dropAll();
        liquibase.update("create");
        liquibase.update("update");
        
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        ComponentParser componentParser = new ComponentParser(new RussianLuceneMorphology());
        List<ItemMeal> itemMealList = new ArrayList<>();
        Set<String> tagList = new TreeSet<>();

        URL resource = Thread.currentThread().getContextClassLoader().getResource("mealFromSite/compositions.csv");
        Files.lines(Paths.get(resource.toURI()), StandardCharsets.UTF_8).forEach(s ->  processComponent(s, componentParser, itemMealList, tagList));

        System.out.println();
        System.out.println("ITEM - " + tagList.size());
        for (String s : tagList) {
            System.out.println(s);
        }
    }

    private static void processComponent(String line, ComponentParser componentParser, List<ItemMeal> itemMealList, Set<String> itemList){
        //itemMealList.add(componentParser.newParse(line, itemList));
    }

     /*
        Files.lines(Paths.get("meal.csv"), StandardCharsets.UTF_8).forEach(MealParser::saveMealFileLineToDb);
        Files.lines(Paths.get("compositions.csv"), StandardCharsets.UTF_8).forEach(RecipePageParcer::processPage);
        Files.lines(Paths.get("steps.csv"), StandardCharsets.UTF_8).forEach(RecipePageParcer::processPage);
        Files.lines(Paths.get("tags.csv"), StandardCharsets.UTF_8).forEach(RecipePageParcer::processPage);*/
}
