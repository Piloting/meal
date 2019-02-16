package ru.pilot.meal.entity;

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
    //<column name="F_ID" type="NUMBER(19,0)" remarks="Идентификатор">
    private Long stepId;

    //<column name="F_MEALID" type="NUMBER(19,0)" remarks="ID Блюда"/>
    private Long mealId;

    //<column name="F_NAME" type="VARCHAR(500)" remarks="Наименование"/>
    private String pictUrl;

    //<column name="F_PICTURL" type="VARCHAR(1024)" remarks="Url картинки"/>
    private String name;
}
