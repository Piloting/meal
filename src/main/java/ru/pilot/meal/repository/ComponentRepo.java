package ru.pilot.meal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pilot.meal.entity.ItemMeal;

public interface ComponentRepo extends CrudRepository<ItemMeal, Long> {
    
}
