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
@Table(name = "T_MEALTAG")
@Getter
@Setter
public class TagMeal implements MealComponent, MealChild {

    @Id
    @SequenceGenerator(name = "mealSeq", sequenceName = "meal_seq", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mealSeq")
    @Column(name = "F_ID")
    //<column name="F_ID" type="NUMBER(19,0)" remarks="Идентификатор">
    private Long tagMealId;

    //<column name="F_MEALID" type="NUMBER(19,0)" remarks="ID Блюда"/>
    @Column(name = "F_MEALID")
    private Long mealId;

    //<column name="F_TAGID" type="NUMBER(19,0)" remarks="ID тега"/>
    @Column(name = "F_TAGID")
    private Long tagId;
}
