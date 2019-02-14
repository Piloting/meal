package ru.pilot.aliceMeal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class TagMeal {

    @Id
    @GeneratedValue
    private Long tagMealId;
    
    private Long mealId;
    private Long tagId;
}
