package ru.pilot.meal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pilot.meal.entity.Tag;

public interface TagRepo extends CrudRepository<Tag, Long> {
    
}
