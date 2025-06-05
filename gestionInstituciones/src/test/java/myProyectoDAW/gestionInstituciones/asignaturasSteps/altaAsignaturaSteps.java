package myProyectoDAW.gestionInstituciones.asignaturasSteps;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import myProyectoDAW.gestionInstituciones.commonSteps.autenticacionStepsComunes;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

public class altaAsignaturaSteps {

     private Response response;
    
    @Autowired
    private autenticacionStepsComunes autenticacionStepsComunes;

    @Then("la respuesta debe contener los datos de la asignatura creada")
    public void la_respuesta_debe_contener_los_datos_de_la_asignatura_creada() {

        response = autenticacionStepsComunes.getResponse(); // Obtiene la respuesta almacenada

        // Obtengo el objeto Asignatura de la respuesta JSON
        Asignatura asignaturaCreada = response.jsonPath().getObject(".", Asignatura.class);

        // Verifica que el objeto Asignatura no sea nulo
        Assert.assertNotNull(asignaturaCreada);

        // Verifica que el Codigo de la asignatura no sea nulo.
        Assert.assertNotNull(asignaturaCreada.getCodigo());

        // Imprime los datos de la asignatura en la consola
        System.out.println("Asignatura creada:");
        System.out.println("  - Codigo: " + asignaturaCreada.getCodigo());
        System.out.println("  - Nombre: " + asignaturaCreada.getNombre());
        System.out.println("  - Descripcion: " + asignaturaCreada.getDescripcion());
    }

}
