package ru.pilot.meal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@Setter
public class ItemMeal {
    private static volatile AtomicLong ID = new AtomicLong(1L);
    public static long getNextId(){
        return ID.getAndIncrement();
    }

    @Id
    // <column name="F_ID" type="NUMBER(19,0)" remarks="Идентификатор">
    private Long id;
    //<column name="F_MEALID" type="NUMBER(19,0)" remarks="ID Блюда"/>
    private Long mealId;
    //<column name="F_NAME" type="VARCHAR(1024)" remarks="Ингридиент"/>
    private String name;
}
