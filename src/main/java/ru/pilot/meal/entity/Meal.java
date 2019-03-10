package ru.pilot.meal.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pilot.meal.repository.MealRepo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Table(name = "T_MEAL")
@Getter
@Setter
public class Meal implements MealComponent {
    
    @Id
    @SequenceGenerator(name = "mealSeq", sequenceName = "meal_seq", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mealSeq")
    @Column(name = "F_ID")
    //<column name="F_ID" type="NUMBER(19,0)" remarks="Идентификатор блюда">
    private Long id;

    //<column name="F_URL" type="VARCHAR(1024)" remarks="Url блюда"/>
    @Column(name = "F_URL", length = 1024)
    private String url;

    //<column name="F_NAME" type="VARCHAR(500)" remarks="Наименование"/>
    @Column(name = "F_NAME", length = 500)
    private String name;

    //<column name="F_DESCR" type="VARCHAR(1024)" remarks="Описание"/>
    @Column(name="F_DESCR", length = 1024)
    private String descr;

    //<column name="F_URL_PICT" type="VARCHAR(1024)" remarks="Url картинки"/>
    @Column(name = "F_URL_PICT", length = 1024)
    private String pictUrl;
}
