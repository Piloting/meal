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
@Table(name = "T_TAG")
@Getter
@Setter
public class Tag implements MealComponent {

    @Id
    @SequenceGenerator(name = "mealSeq", sequenceName = "meal_seq", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mealSeq")
    @Column(name = "F_ID")
    //<column name="F_ID" type="NUMBER(19,0)" remarks="Идентификатор">
    private Long id;
    
    //<column name="F_NAME" type="VARCHAR(1024)" remarks="Наименование"/>
    @Column(name = "F_NAME", length = 1024)
    private String name;
}
