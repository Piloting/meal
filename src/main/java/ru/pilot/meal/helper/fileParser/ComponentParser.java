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

    private static List<String> final_units =Arrays.asList("г", "кг", "ст л", "ч л", "мл", "л", "шт", "ст", "уп");
    private static List<String> units = Arrays.asList("зубчик", "горош", "зерн", "бутон", "веточ", "баноч", "кусо", "голов", "консерв", "дольк", "пакет",
            "шарик", "пачк", "струч", "лист", "палоч", "пуч", "стеб", "полос", "упаков", "вилок");
    private static List<String> deleted_units = Arrays.asList("маленьк", "мелк", "мини", "молод", "обычн", "средн", "небольш", "больш", "крупн", "желанию",
            "вес", "вкус", "свеж", "соус", "цедр", "тверд", "полутверд", "мягк", "люб", "заморо", "готов", "общ", "различ", "разн", "варен",
            "это", "белы", "вид", "светл", "темн", "спел", "холод");
    private static List<String> final_deleted_units = Arrays.asList("или", "в", "на", "без", "или", "c", "не", "около", "для", "и", "по", "от", "из",
            "т.д.", "при", "пол", "полу", "одна", "один", "два", "две", "желательно", "хорошо");

    private static final String DELIMITER = ";";

    public String createMealLine(ItemMeal itemMeal){
        return itemMeal.getMealId() + ";" + removeDelimiter(itemMeal.getName());
    }
    
    public List<ItemMeal> parseComponentFromHtml(Document doc, Long mealId){
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
        item.setId(ItemMeal.getNextId());
        item.setMealId(Long.decode(iterator.next()));
        item.setName(iterator.next());
        return item;
    }

    public ItemMeal newParse(String line, Set<String> tagList){
        ItemMeal itemMeal = parseComponentFileLine(line);
        if (itemMeal == null){
            return null;
        }

        extractTags(itemMeal.getName(), tagList, luceneMorph);

        return itemMeal;
    }

    private void extractTags(String siteLine, Set<String> tagList, LuceneMorphology luceneMorph) {
        siteLine = siteLine.toLowerCase();
        // убираем то, что в скобках
        siteLine = siteLine.replaceAll("\\(.*\\)", "");

        // убираем единицы измерения и весь левак
        siteLine = siteLine.replaceAll("[.,|+/–-—]", " ");
        siteLine = deleteUnits(siteLine, final_units, units);
        siteLine = deleteUnits(siteLine, final_deleted_units, deleted_units);

        // убираем остальное лишнее
        siteLine = siteLine.replaceAll("[^А-Яа-я ]", "");
        siteLine = siteLine.replaceAll("\\s+", " ");
        siteLine = siteLine.trim();

        if (StringUtils.isEmpty(siteLine)){
            return;
        }
        String[] words = siteLine.split(" ");
        if (words.length <= 1){
            if (siteLine.length() <= 2){
                return;
            }
            tagList.add(getNormForm(luceneMorph, siteLine));
        } else {
            for (String word : words) {
                if (word.length() > 2){
                    tagList.add(getNormForm(luceneMorph, word));
                }
            }
        }
    }

    private String getNormForm(LuceneMorphology luceneMorph, String word) {
        try {
            return luceneMorph.getNormalForms(word).iterator().next();
        } catch (Exception e) {
            System.out.println("Error get normal form by: '" + word + "'");
        }
        return word;
    }

    private String deleteUnits(String lineWithoutCount, List<String> final_units, List<String> units) {
        String[] split = lineWithoutCount.split(" ");
        if (split.length <= 1){
            return lineWithoutCount;
        }
        if (final_units != null){
            for (String final_unit : final_units) {
                lineWithoutCount = lineWithoutCount.replace(" " + final_unit + " ", " ");
                lineWithoutCount = lineWithoutCount.replaceAll("^"+final_unit + " "," ");
                lineWithoutCount = lineWithoutCount.replaceAll( " " + final_unit+"$"," ");
            }
        }

        if (units != null){
            for (String unit : units) {
                if (lineWithoutCount.contains(unit)){
                    Integer endIndex = lineWithoutCount.indexOf(" ", lineWithoutCount.indexOf(unit));
                    endIndex = endIndex == -1 ? lineWithoutCount.length() : endIndex;
                    lineWithoutCount = lineWithoutCount.substring(0, lineWithoutCount.indexOf(unit)) + " " + lineWithoutCount.substring(endIndex, lineWithoutCount.length());
                }
            }
        }

        return lineWithoutCount;
    }

    private static String removeDelimiter(String field){
        return field != null ? field.replace(DELIMITER, ",") : null;
    }
}


