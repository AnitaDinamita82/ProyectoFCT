package myProyectoDAW.gestionInstituciones;

import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = GestionInstitucionesApplication.class)
public class CucumberSpringConfiguration {

}
