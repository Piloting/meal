package ru.pilot.aliceMeal.helper;


import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DbInitaliser {
    
    @Autowired
    private DataSource dataSource;
    
    public void createStructure() throws SQLException, LiquibaseException {
        
        Liquibase liquibase = new Liquibase("changelog.xml", new ClassLoaderResourceAccessor(),  new JdbcConnection(dataSource.getConnection()));
        liquibase.forceReleaseLocks();
        liquibase.dropAll();
        liquibase.update("create");
        liquibase.update("update");
        
    }
    
     /*
        Files.lines(Paths.get("meal.csv"), StandardCharsets.UTF_8).forEach(MealParser::saveMealFileLineToDb);
        Files.lines(Paths.get("compositions.csv"), StandardCharsets.UTF_8).forEach(RecipePageParcer::processPage);
        Files.lines(Paths.get("steps.csv"), StandardCharsets.UTF_8).forEach(RecipePageParcer::processPage);
        Files.lines(Paths.get("tags.csv"), StandardCharsets.UTF_8).forEach(RecipePageParcer::processPage);*/
}
