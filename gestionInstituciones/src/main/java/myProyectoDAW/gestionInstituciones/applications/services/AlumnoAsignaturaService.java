package myProyectoDAW.gestionInstituciones.applications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RespositoryAlumnosAsignaturas;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

@Service
public class AlumnoAsignaturaService {

    @Autowired
    private RespositoryAlumnosAsignaturas respositoryAlumnosAsignaturas;

    /* Dado un dni de alumno listar todas las asignatruas matriculadas */

    public List<Asignatura> listarAsignaturasDeAlumno(String dniAlumno) {

        return respositoryAlumnosAsignaturas.listarAsignaturasDeAlumno(dniAlumno);
    }

    /* Dado un codigo de asignatura listar todos los alumnos matriculados */
    public List<Alumno> obtenerAlumnosMatriculados(String codigoAsignatura) {
        return respositoryAlumnosAsignaturas.obtenerAlumnosMatriculados(codigoAsignatura);
    }

    /* Verifica si un alumno tiene asignaturas asociadas */
    public boolean alumnoTieneAsignaturas(String dniAlumno) {
        return respositoryAlumnosAsignaturas.alumnoTieneAsignaturas(dniAlumno);
    }

    /* Verifica si una asignatura en cuestion tiene alumnos matriculados */
    public boolean asignaturaTieneAlumnos(String codigoAsignatura) {

        return respositoryAlumnosAsignaturas.asignaturasTieneAlumnos(codigoAsignatura);
    }

    /* Matricularemos a un alumno en una asignatura */
    public ResponseEntity<String> matricularAlumno(String dniAlumno, String codigoAsignatura) {
        return respositoryAlumnosAsignaturas.matricularAlumno(dniAlumno, codigoAsignatura);
    }

    /* Daremos de baja una asignatura de la matricula del alumno */
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumno(String dniAlumno, String codigoAsignatura) {
        return respositoryAlumnosAsignaturas.bajaAsignaturaDeMatriculaDeAlumno(dniAlumno, codigoAsignatura);
    }
}
