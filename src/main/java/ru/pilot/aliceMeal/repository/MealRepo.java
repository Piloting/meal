package ru.pilot.aliceMeal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pilot.aliceMeal.entity.Meal;

public interface MealRepo extends CrudRepository<Meal, Long> {
    
}
