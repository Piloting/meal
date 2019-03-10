package ru.pilot.meal.helper.htmlParser.say7;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.pilot.meal.entity.ComplexMeal;
import ru.pilot.meal.entity.ItemMeal;
import ru.pilot.meal.entity.Meal;
import ru.pilot.meal.entity.StepMeal;

import java.util.ArrayList;
import java.util.List;

public class HtmlComponentParser {
    
    public ComplexMeal extractAllComponent(Document document){
        // получение основных элементов
        Meal meal = MealExtractor.get(document);
        if (meal == null){
            return null;
        }
        List<ItemMeal> itemMealList = ItemMealExtractor.get(document, meal.getId());
        List<StepMeal> stepList = StepMealExtractor.get(document, meal.getId());

        ComplexMeal complexMeal = new ComplexMeal();
        complexMeal.setMeal(meal);
        complexMeal.setItemMealList(itemMealList);
        complexMeal.setStepMealList(stepList);
        
        return complexMeal;
    }


    private static class MealExtractor {
        public static Meal get(Document doc){
            Meal meal = new Meal();
            meal.setName(doc.title());
            meal.setUrl(doc.baseUri());
            Element element = doc.select("div.p-summary").first();
            if (element != null){
                meal.setDescr(element.text().replace(";", ","));
            }
            element = doc.select("img.u-photo").first();
            if (element != null){
                meal.setPictUrl(Say7Parser.getHref(element, "src"));
            }
            return meal;
        }
    }

    private static class ItemMealExtractor {
        public static List<ItemMeal> get(Document doc, Long mealId){
            Elements elements = doc.select("li.p-ingredient");
            if (elements == null){
                return null;
            }
            List<ItemMeal> list = new ArrayList<>();
            for (Element element : elements) {
                ItemMeal comp = new ItemMeal();
                comp.setMealId(mealId);
                comp.setName(element.text());
                list.add(comp);
            }

            return list;
        }
    }

    private static class StepMealExtractor {
        public static List<StepMeal> get(Document doc, Long mealId){
            Element div = doc.select("div.stepbystep").first();
            if (div == null){
                return null;
            }
            List<StepMeal> list = new ArrayList<>();
            Elements pList = div.select("p[id]");
            if (pList == null){
                return null;
            }
            for (Element element : pList) {
                String text = element.text();
                if (text.isEmpty()){
                    continue;
                }
                StepMeal step = new StepMeal();
                step.setMealId(mealId);
                step.setName(text);
                Element img = element.select("img[src]").first();
                if (img != null){
                    step.setPictUrl(Say7Parser.getHref(img, "src"));
                }
                list.add(step);
            }

            return list;
        }
    }
    
}
