package myProyectoDAW.gestionInstituciones.asignaturasSteps;

import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.And;
import io.restassured.response.Response;
import myProyectoDAW.gestionInstituciones.commonSteps.autenticacionStepsComunes;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

public class listarAsignaturasSteps {

    @Autowired
    private autenticacionStepsComunes autenticacionStepsComunes;

    private Response response;

    @And("la respuesta debe de ser una lista de asignaturas")
    public void la_respuesta_debe_de_ser_una_lista_de_asignaturas(){

        response = autenticacionStepsComunes.getResponse(); // Obtiene la respuesta almacenada.

        // Verifica que la respuesta sea una lista y que contenga asignaturas
        List<Asignatura> asignaturas = response.jsonPath().getList(".", Asignatura.class); 
        Assert.assertNotNull(asignaturas);
        Assert.assertTrue(!asignaturas.isEmpty()); 

         // Imprime la lista de alumnos en la consola
        System.out.println("Lista de asignaturas:"); 
        
        for (Asignatura asignatura : asignaturas) {
            System.out.println("  - Codigo: " + asignatura.getCodigo());
            System.out.println("  - Nombre: " + asignatura.getNombre());
            System.out.println("  - Descripcion: " + asignatura.getDescripcion());
            System.out.println(" ");
        }

    } 

}
