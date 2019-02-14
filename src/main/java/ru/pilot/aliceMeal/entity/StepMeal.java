package ru.pilot.aliceMeal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class StepMeal {

    @Id
    @GeneratedValue
    private Long stepId;
    private Long mealId;
    private String item;
    private String pictUrl;

}