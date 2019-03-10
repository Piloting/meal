package ru.pilot.meal.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ComplexMeal implements MealComponent {
    private Meal meal;
    private List<ItemMeal> itemMealList;
    private List<StepMeal> stepMealList;
}
