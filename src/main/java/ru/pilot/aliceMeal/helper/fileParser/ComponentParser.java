package ru.pilot.aliceMeal.helper.fileParser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;
import ru.pilot.aliceMeal.entity.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ComponentParser {

    private static final String DELIMITER = ";";

    public String createMealLine(Component component){
        return component.getMealId() + ";" + removeDelimiter(component.getName());
    }
    
    public List<Component> parseComponentFromHtml(Document doc, Long mealId){
        Elements elements = doc.select("li.p-ingredient");
        if (elements == null){
            return null;
        }
        List<Component> list = new ArrayList<>();
        for (Element element : elements) {
            Component comp = new Component();
            comp.setMealId(mealId);
            comp.setName(element.text());
            list.add(comp);
        }

        return list;
    }

    public Component parseComponentFileLine(String line){
        if (StringUtils.isEmpty(line)){
            return null;
        }
        List<String> parts = Arrays.asList(line.split(DELIMITER));
        if (parts.size() != 2){
            throw new RuntimeException("Неверный формат файла");
        }
        Iterator<String> iterator = parts.iterator();

        Component meal = new Component();
        meal.setMealId(Long.decode(iterator.next()));
        meal.setName(iterator.next());
        return meal;
    }
    
    private static String removeDelimiter(String field){
        return field != null ? field.replace(DELIMITER, ",") : null;
    }
}
