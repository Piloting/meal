package ru.pilot.meal.helper.fileParser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.util.StringUtils;
import ru.pilot.meal.entity.Meal;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MealParser {

    private static final String DELIMITER = ";";

    public String createMealLine(Meal meal){
        return meal.getId() + ";" + removeDelimiter(meal.getUrl()) + ";" + removeDelimiter(meal.getName()) + ";" + removeDelimiter(meal.getDescr());
    }
    
    public Meal parseMealFromHtml(Document doc){
        Meal meal = new Meal();
        meal.setName(removeDelimiter(doc.title()));
        meal.setUrl(removeDelimiter(doc.baseUri()));
        Element element = doc.select("div.p-summary").first();
        if (element != null){
            meal.setDescr(removeDelimiter(element.text()));
        }
        return meal;
    }

    public Meal parseMealFileLine(String line){
        if (StringUtils.isEmpty(line)){
            return null;
        }
        List<String> parts = Arrays.asList(line.split(DELIMITER));
        if (parts.size() != 4){
            throw new RuntimeException("Неверный формат файла");
        }
        Iterator<String> iterator = parts.iterator();
        
        Meal meal = new Meal();
        meal.setId(Long.decode(iterator.next()));
        meal.setUrl(iterator.next());
        meal.setName(iterator.next());
        meal.setDescr(iterator.next());
        return meal;
    }
    
    private static String removeDelimiter(String field){
        return field != null ? field.replace(DELIMITER, ",") : null;
    }
}
