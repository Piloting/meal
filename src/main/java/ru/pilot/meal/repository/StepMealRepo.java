package ru.pilot.meal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pilot.meal.entity.StepMeal;

public interface StepMealRepo extends CrudRepository<StepMeal, Long> {
    
}
