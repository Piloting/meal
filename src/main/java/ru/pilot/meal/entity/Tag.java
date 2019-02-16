package ru.pilot.meal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue
    //<column name="F_ID" type="NUMBER(19,0)" remarks="Идентификатор">
    private Long id;
    //<column name="F_NAME" type="VARCHAR(1024)" remarks="Наименование"/>
    private String name;
}
