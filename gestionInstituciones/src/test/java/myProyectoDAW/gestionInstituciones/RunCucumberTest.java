package myProyectoDAW.gestionInstituciones;



import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


    @RunWith(Cucumber.class)
    @CucumberOptions(features ="src/test/resources/features",
                     glue =  "myProyectoDAW.gestionInstituciones",
                     plugin = {"pretty", "html:target/cucumber-reports"}   
                     )
    public class RunCucumberTest {}
