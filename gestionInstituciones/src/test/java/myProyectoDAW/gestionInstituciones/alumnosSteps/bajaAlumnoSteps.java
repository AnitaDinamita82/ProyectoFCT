package myProyectoDAW.gestionInstituciones.alumnosSteps;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import myProyectoDAW.gestionInstituciones.applications.services.MatriculaService;
import myProyectoDAW.gestionInstituciones.applications.services.AlumnoService;
import myProyectoDAW.gestionInstituciones.commonSteps.autenticacionStepsComunes;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;

public class bajaAlumnoSteps {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private MatriculaService matriculaService;

    private Response response;

    @Autowired
    private autenticacionStepsComunes autenticacionStepsComunes;

    @Given("un alumno con DNI {string} no existente en el sistema")
    public void alumno_no_existente(String dni) {
        // Verifica que el alumno no existe en la base de datos
        Alumno alumno = alumnoService.encontrarSiExisteAlumno(dni);
        assertNull(alumno);
    }

    @Transactional
    @Given("el alumno con DNI {string} tiene asignaturas asociadas")
    public void alumno_tiene_asignaturas_asociadas(String dniAlumno) {
        assertTrue(matriculaService.alumnoTieneAsignaturas(dniAlumno));
    }

    @Transactional
    @Given("el alumno con DNI {string} no tiene asignaturas asociadas")
    public void alumno_no_tiene_asignaturas_asociadas(String dniAlumno) {
        // Asegúrate de que el alumno no tenga asignaturas asociadas en la base de datos
        assertFalse(matriculaService.alumnoTieneAsignaturas(dniAlumno));
    }

    @And("se comprobara que el alumno no existe mostrando la lista de alumnos")
    public void se_comprobara_que_el_alumno_no_existe_mostrando_la_lista_de_alumnos() {

        response = autenticacionStepsComunes.getResponse(); // Obtiene la respuesta de autenticacionStepsComunes

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
