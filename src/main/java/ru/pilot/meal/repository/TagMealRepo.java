package ru.pilot.meal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pilot.meal.entity.TagMeal;

public interface TagMealRepo extends CrudRepository<TagMeal, Long> {
    boolean existsByMealIdAndAndTagId(Long mealId, Long tagId);
}
