package myProyectoDAW.gestionInstituciones.alumnosSteps;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import myProyectoDAW.gestionInstituciones.commonSteps.autenticacionStepsComunes;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;

public class altaAlumnoStep {

     private Response response;
    
    @Autowired
    private autenticacionStepsComunes autenticacionStepsComunes;


    @Then("la respuesta debe contener los datos del alumno creado")
    public void la_respuesta_debe_contener_los_datos_del_alumno_creado() {

        response = autenticacionStepsComunes.getResponse(); // Obtiene la respuesta almacenada

      // Obtiene el objeto Alumno de la respuesta JSON
        Alumno alumnoCreado = response.jsonPath().getObject(".", Alumno.class);

        // Verifica que el objeto Alumno no sea nulo
        Assert.assertNotNull(alumnoCreado);

        // Verifica que el DNI del alumno creado no sea nulo
        Assert.assertNotNull(alumnoCreado.getDni());

        // Imprime los datos del alumno creado en la consola
        System.out.println("Alumno creado:");
        System.out.println("  - DNI: " + alumnoCreado.getDni());
        System.out.println("  - Nombre: " + alumnoCreado.getNombre());
        System.out.println("  - Apellido1: " + alumnoCreado.getApellido1());
        System.out.println("  - Apellido2: " + alumnoCreado.getApellido2());
    }
}
