package ru.pilot.meal.helper.fileParser;

import org.apache.lucene.morphology.LuceneMorphology;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;
import ru.pilot.meal.entity.ItemMeal;

import java.util.*;

public class ComponentParser {
    private LuceneMorphology luceneMorph;
    public ComponentParser(LuceneMorphology luceneMorph){
        this.luceneMorph = luceneMorph;
    }

    
    private static final String DELIMITER = ";";

    public String createMealLine(ItemMeal itemMeal){
        return itemMeal.getMealId() + ";" + removeDelimiter(itemMeal.getName());
    }

    public ItemMeal parseComponentFileLine(String line){
        if (StringUtils.isEmpty(line)){
            return null;
        }
        List<String> parts = Arrays.asList(line.split(DELIMITER));
        if (parts.size() != 2){
            throw new RuntimeException("Неверный формат файла");
        }
        Iterator<String> iterator = parts.iterator();

        ItemMeal item = new ItemMeal();
        item.setMealId(Long.decode(iterator.next()));
        item.setName(iterator.next());
        return item;
    }


    private static String removeDelimiter(String field){
        return field != null ? field.replace(DELIMITER, ",") : null;
    }
}


