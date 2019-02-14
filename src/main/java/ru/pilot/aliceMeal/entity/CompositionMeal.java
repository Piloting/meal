package ru.pilot.aliceMeal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class CompositionMeal {

    @Id
    @GeneratedValue
    private Long compositionMealId;
    
    
    private Long mealId;
    private String descr;
    private Long componentId;
}
