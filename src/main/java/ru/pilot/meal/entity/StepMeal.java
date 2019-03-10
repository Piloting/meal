package ru.pilot.meal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "T_STEP")
@Getter
@Setter
public class StepMeal implements MealComponent, MealChild {

    @Id
    @SequenceGenerator(name = "mealSeq", sequenceName = "meal_seq", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mealSeq")
    @Column(name = "F_ID")
    //<column name="F_ID" type="NUMBER(19,0)" remarks="Идентификатор">
    private Long id;

    //<column name="F_MEALID" type="NUMBER(19,0)" remarks="ID Блюда"/>
    @Column(name = "F_MEALID")
    private Long mealId;

    //<column name="F_NAME" type="VARCHAR(500)" remarks="Наименование"/>
    @Column(name = "F_NAME", length = 500)
    private String pictUrl;

    //<column name="F_PICTURL" type="VARCHAR(1024)" remarks="Url картинки"/>
    @Column(name = "F_PICTURL", length = 1024)
    private String name;
}
