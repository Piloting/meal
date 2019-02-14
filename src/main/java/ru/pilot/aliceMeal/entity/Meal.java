package ru.pilot.aliceMeal.entity;

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
    private Long id;
    private String url;
    private String name;
    
    @Column(length = 1024)
    private String descr;
}
