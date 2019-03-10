package ru.pilot.meal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pilot.meal.entity.ItemMeal;

public interface ItemMealRepo extends CrudRepository<ItemMeal, Long> {
    
}
