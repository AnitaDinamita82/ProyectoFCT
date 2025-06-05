package myProyectoDAW.gestionInstituciones.applications.ports;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

@Repository
public interface RespositoryAlumnosAsignaturas {

    List<Asignatura> listarAsignaturasDeAlumno(String dniAlumno);

    List<Alumno> obtenerAlumnosMatriculados(String codigoAsignatura);

    boolean alumnoTieneAsignaturas(String dniAlumno);

    boolean asignaturasTieneAlumnos(String codigoAsignatura);

    ResponseEntity<String> matricularAlumno(String dniAlumno, String codigoAsignatura);

    ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumno(String dniAlumno, String codigoAsignatura);
}
