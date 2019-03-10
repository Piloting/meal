package ru.pilot.meal.helper;

import ru.pilot.meal.entity.MealComponent;

import java.util.List;

public interface ComponentSaver {
    Long save(MealComponent mealComponent);
    void save(List<? extends MealComponent> mealComponentList);
}
