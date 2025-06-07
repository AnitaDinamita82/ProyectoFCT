package myProyectoDAW.gestionInstituciones.asignaturasSteps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import myProyectoDAW.gestionInstituciones.applications.services.MatriculaService;
import myProyectoDAW.gestionInstituciones.applications.services.AsignaturaService;
import myProyectoDAW.gestionInstituciones.commonSteps.autenticacionStepsComunes;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

public class bajaAsignaturaSteps {

    @Autowired
    private AsignaturaService asignaturaService;

    @Autowired
    private MatriculaService matriculaService;

    private Response response;

    @Autowired
    private autenticacionStepsComunes autenticacionStepsComunes;

    @Given("una asignatura con codigo {string} no existente en el sistema")
    public void asignatura_no_existente(String codigo) {
        // Verifica que la asignatura no existe en la base de datos
        Asignatura asignatura = asignaturaService.encontrarSiExisteAsignatura(codigo);
        Assert.assertNull(asignatura);
    }

    @Transactional
    @Given("la signatura con codigo {string} tiene alumnos asociados")
    public void asignatura_tiene_alumnos_asociados(String codigoAsignatura) {
        assertTrue(matriculaService.asignaturaTieneAlumnos(codigoAsignatura));
    }

    @Transactional
    @Given("la asignatura con codigo {string} no tiene alumnos asociados")
    public void asignatura_no_tiene_alumnos_asociados(String codigoAsignatura) {
        // Asegúrate de que la asignatura no tenga alumnos asociados en la base de datos
        assertFalse(matriculaService.asignaturaTieneAlumnos(codigoAsignatura));
    }

    @And("se comprobara que la asignatura no existe mostrando la lista de asignaturas")
    public void se_comprobara_que_la_asignatura_no_existe_mostrando_la_lista_de_asignaturas() {

        response = autenticacionStepsComunes.getResponse(); // Obtiene la respuesta de autenticacionStepsComunes

        // Verifica que la respuesta sea una lista y que contenga asignaturas.
        List<Asignatura> asignaturas = response.jsonPath().getList(".", Asignatura.class);
        Assert.assertNotNull(asignaturas);
        Assert.assertTrue(!asignaturas.isEmpty());

        // Imprime la lista de asignaturas en la consola
        System.out.println("Lista de asignaturas:");

        for (Asignatura asignatura : asignaturas) {
            System.out.println("  - Codigo: " + asignatura.getCodigo());
            System.out.println("  - Nombre: " + asignatura.getNombre());
            System.out.println("  - Descripcion: " + asignatura.getDescripcion());
            System.out.println();
        }
    }
}
