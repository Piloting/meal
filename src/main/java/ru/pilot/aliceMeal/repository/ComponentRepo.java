package ru.pilot.aliceMeal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pilot.aliceMeal.entity.Component;

public interface ComponentRepo extends CrudRepository<Component, Long> {
    
}
