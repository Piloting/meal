package ru.pilot.meal.helper;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.pilot.meal.entity.ItemMeal;
import ru.pilot.meal.entity.Meal;
import ru.pilot.meal.entity.Tag;
import ru.pilot.meal.entity.TagMeal;
import ru.pilot.meal.repository.ItemMealRepo;
import ru.pilot.meal.repository.MealRepo;
import ru.pilot.meal.repository.TagMealRepo;
import ru.pilot.meal.repository.TagRepo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TagProcessor {
    
    @Autowired
    private MealRepo mealRepo;
    @Autowired
    private TagMealRepo tagMealRepo;
    @Autowired
    private TagRepo tagRepo;
    @Autowired
    private ItemMealRepo itemMealRepo;

    private static List<String> final_units = Arrays.asList("г", "кг", "ст л", "ч л", "мл", "л", "шт", "ст", "уп");
    private static List<String> units = Arrays.asList("зубчик", "горош", "зерн", "бутон", "веточ", "баноч", "кусо", "голов", "консерв", "дольк", "пакет",
            "шарик", "пачк", "струч", "лист", "палоч", "пуч", "стеб", "полос", "упаков", "вилок");
    private static List<String> deleted_units = Arrays.asList("маленьк", "мелк", "мини", "молод", "обычн", "средн", "небольш", "больш", "крупн", "желанию",
            "вес", "вкус", "свеж", "соус", "цедр", "тверд", "полутверд", "мягк", "люб", "заморо", "готов", "общ", "различ", "разн", "варен",
            "это", "белы", "вид", "светл", "темн", "спел", "холод");
    private static List<String> final_deleted_units = Arrays.asList("или", "в", "на", "без", "или", "c", "не", "около", "для", "и", "по", "от", "из",
            "т.д.", "при", "пол", "полу", "одна", "один", "два", "две", "желательно", "хорошо", "еще", "под");
    
    private boolean isMultiThread = false;


    public void prepareTagToAlMeal() throws Exception {
        clearTags();
        
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();


        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (Meal meal : mealRepo.findAll()) {
            String descr = meal.getName();
            Long mealId = meal.getId();
            if (isMultiThread){
                executor.execute(() -> processTags(luceneMorph, descr, mealId));
            } else {
                processTags(luceneMorph, descr, mealId);
            }
        }

        for (ItemMeal itemMeal : itemMealRepo.findAll()) {
            String descr = itemMeal.getName();
            Long mealId = itemMeal.getMealId();
            if (isMultiThread){
                executor.execute(() -> processTags(luceneMorph, descr, mealId));
            } else {
                processTags(luceneMorph, descr, mealId);
            }
        }
    }

    private void clearTags() {
        tagMealRepo.deleteAll();
        tagRepo.deleteAll();
    }

    private void processTags(LuceneMorphology luceneMorph, String descr, Long mealId) {
        Set<String> tagList = extractTags(descr, luceneMorph);
        for (String tagName : tagList) {
            Long tagId = getTagId(tagName);

            if (!tagMealRepo.existsByMealIdAndAndTagId(mealId, tagId)){
                TagMeal newTagMeal = new TagMeal();
                newTagMeal.setMealId(mealId);
                newTagMeal.setTagId(tagId);
                try {
                    tagMealRepo.save(newTagMeal);
                } catch (Exception e){
                    // добавили в соседнем потоке
                }
            }
        }
    }

    /**
     * Получаем id тэга, если нет - создаем
     */
    private Long getTagId(String tagName) {
        Tag existsTag = tagRepo.findTagByName(tagName);
        Long tagId;
        if (existsTag == null){
            Tag newTag = new Tag();
            newTag.setName(tagName);
            try {
                tagRepo.save(newTag);
            } catch (Exception e){
                // добавление в другом потоке
                newTag = tagRepo.findTagByName(tagName);
            }
            tagId = newTag.getId();
            System.out.println("add tag - " + tagName);
        } else {
            tagId = existsTag.getId();
        }
        return tagId;
    }


    private Set<String> extractTags(String siteLine, LuceneMorphology luceneMorph) {
        Set<String> tagList = new HashSet<>();
        siteLine = siteLine.toLowerCase();
        // убираем то, что в скобках
        siteLine = siteLine.replaceAll("\\(.*\\)", "");

        // убираем единицы измерения и весь левак
        siteLine = siteLine.replaceAll("[.,|+/–-—-]", " ");
        siteLine = deleteUnits(siteLine, final_units, units);
        siteLine = deleteUnits(siteLine, final_deleted_units, deleted_units);

        // убираем остальное лишнее
        siteLine = siteLine.replaceAll("[^А-Яа-я ]", "");
        siteLine = siteLine.replaceAll("\\s+", " ");
        siteLine = siteLine.trim();

        if (StringUtils.isEmpty(siteLine)){
            return tagList;
        }
        String[] words = siteLine.split(" ");
        if (words.length <= 1){
            if (siteLine.length() <= 2){
                return tagList;
            }
            tagList.add(getNormForm(luceneMorph, siteLine));
        } else {
            for (String word : words) {
                if (word.length() > 2){
                    tagList.add(getNormForm(luceneMorph, word));
                }
            }
        }
        return tagList;
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

    public void setMultiThread(boolean multiThread) {
        isMultiThread = multiThread;
    }
}
