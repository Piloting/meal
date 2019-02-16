package ru.pilot.meal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pilot.meal.entity.StepMeal;

public interface StepRepo extends CrudRepository<StepMeal, Long> {
    
}
