package ru.pilot.aliceMeal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Component {

    @Id
    @GeneratedValue
    private Long id;
    private Long mealId;
    private String name;
}
