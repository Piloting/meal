package ru.pilot.meal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@Setter
public class Meal {
    private static volatile AtomicLong ID = new AtomicLong(1L);
    public static long getNextId(){
        return ID.getAndIncrement();
    }
    
    @Id
    //<column name="F_ID" type="NUMBER(19,0)" remarks="Идентификатор блюда">
    private Long id;

    //<column name="F_URL" type="VARCHAR(1024)" remarks="Url блюда"/>
    private String url;

    //<column name="F_NAME" type="VARCHAR(500)" remarks="Наименование"/>
    private String name;

    //<column name="F_DESCR" type="VARCHAR(1024)" remarks="Описание"/>
    @Column(length = 1024)
    private String descr;

    //<column name="F_URL_PICT" type="VARCHAR(1024)" remarks="Url картинки"/>
    private String pictUrl;
}
