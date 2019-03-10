package ru.pilot.meal.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pilot.meal.entity.ComplexMeal;
import ru.pilot.meal.entity.ItemMeal;
import ru.pilot.meal.entity.Meal;
import ru.pilot.meal.entity.MealChild;
import ru.pilot.meal.entity.MealComponent;
import ru.pilot.meal.entity.StepMeal;
import ru.pilot.meal.entity.Tag;
import ru.pilot.meal.entity.TagMeal;
import ru.pilot.meal.helper.ComponentSaver;

import java.util.List;

@Component
public class DbComponentSaver implements ComponentSaver {
    @Autowired
    private MealRepo mealRepo;
    @Autowired
    private ItemMealRepo itemMealRepo;
    @Autowired
    private StepMealRepo stepMealRepo;
    @Autowired
    private TagRepo tagRepo;
    @Autowired
    private TagMealRepo tagMealRepo;

    @Override
    public Long save(MealComponent mealComponent) {
        if (mealComponent == null){
            return null;
        }
        if (mealComponent instanceof Meal){
            mealRepo.save((Meal)mealComponent);
            return ((Meal)mealComponent).getId();
        } else if (mealComponent instanceof ItemMeal){
            itemMealRepo.save((ItemMeal)mealComponent);
            return ((ItemMeal)mealComponent).getId();
        } else if (mealComponent instanceof StepMeal){
            stepMealRepo.save((StepMeal)mealComponent);
            return ((StepMeal)mealComponent).getId();
        } else if (mealComponent instanceof Tag){
            tagRepo.save((Tag)mealComponent);
            return ((Tag)mealComponent).getId();
        } else if (mealComponent instanceof TagMeal){
            tagMealRepo.save((TagMeal)mealComponent);
            return null;
        } else if  (mealComponent instanceof ComplexMeal){
            ComplexMeal complexMeal = (ComplexMeal) mealComponent;
            Meal meal = complexMeal.getMeal();
            save(meal);
            // проставим id осносной сущности
            setMealId(complexMeal.getItemMealList(), meal.getId());
            setMealId(complexMeal.getStepMealList(), meal.getId());
            save(complexMeal.getItemMealList());
            save(complexMeal.getStepMealList());
            return (meal.getId());
        } else {
            throw new RuntimeException("Component not supported!");
        }
    }

    @Override
    public void save(List<? extends MealComponent> mealComponentList) {
        if (mealComponentList == null){
            return;
        }
        for (MealComponent mealComponent : mealComponentList) {
            save(mealComponent);
        }
    }
    
    private void setMealId(List<? extends MealChild> mealChildList, Long mealId){
        if (mealChildList == null){
            return;
        }
        for (MealChild mealChild : mealChildList) {
            mealChild.setMealId(mealId);
        }
    }
}
