package myProyectoDAW.gestionInstituciones.alumnosSteps;


import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.And;
import io.restassured.response.Response;
import myProyectoDAW.gestionInstituciones.commonSteps.autenticacionStepsComunes;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;

public class listarAlumnosSteps {

    @Autowired
    private autenticacionStepsComunes autenticacionStepsComunes;

    private Response response;

    @And("la respuesta debe de ser una lista de alumnos")
    public void la_respuesta_debe_de_ser_una_lista_de_alumnos(){

        response = autenticacionStepsComunes.getResponse(); // Obtiene la respuesta almacenada

        // Verifica que la respuesta sea una lista y que contenga alumnos.
        List<Alumno> alumnos = response.jsonPath().getList(".", Alumno.class); 
        Assert.assertNotNull(alumnos);
        Assert.assertTrue(!alumnos.isEmpty()); 

         // Imprime la lista de alumnos en la consola
        System.out.println("Lista de alumnos:"); 
        
        for (Alumno alumno : alumnos) {
            System.out.println("  - DNI: " + alumno.getDni());
            System.out.println("  - Nombre: " + alumno.getNombre());
            System.out.println("  - Apellido1: " + alumno.getApellido1());
            System.out.println("  - Apellido2: " + alumno.getApellido2());
            System.out.println(" ");
        }

    } 
}    
