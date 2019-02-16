package ru.pilot.meal.entity;

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
    //<column name="F_ID" type="NUMBER(19,0)" remarks="Идентификатор">
    private Long tagMealId;

    //<column name="F_MEALID" type="NUMBER(19,0)" remarks="ID Блюда"/>
    private Long mealId;

    //<column name="F_TAGID" type="NUMBER(19,0)" remarks="ID тега"/>
    private Long tagId;
}
