package ru.pilot.meal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pilot.meal.entity.Meal;

public interface MealRepo extends CrudRepository<Meal, Long> {
    
}
