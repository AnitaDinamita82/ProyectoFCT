package myProyectoDAW.gestionInstituciones.applications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryMatricula;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

@Service
public class MatriculaService {

    @Autowired
    private RepositoryMatricula repositoryMatricula;

    /* Dado un dni de alumno listar todas las asignatruas matriculadas */
    public List<Asignatura> listarAsignaturasDeAlumno(String dniAlumno) {

        return repositoryMatricula.listarAsignaturasDeAlumno(dniAlumno);
    }

    /* Dado un codigo de asignatura listar todos los alumnos matriculados */
    public List<Alumno> obtenerAlumnosMatriculados(String codigoAsignatura) {
        return repositoryMatricula.obtenerAlumnosMatriculados(codigoAsignatura);
    }

    /* Verifica si un alumno tiene asignaturas asociadas */
    public boolean alumnoTieneAsignaturas(String dniAlumno) {
        return repositoryMatricula.alumnoTieneAsignaturas(dniAlumno);
    }

    /* Verifica si una asignatura en cuestion tiene alumnos matriculados */
    public boolean asignaturaTieneAlumnos(String codigoAsignatura) {

        return repositoryMatricula.asignaturasTieneAlumnos(codigoAsignatura);
    }

    /* Matricularemos a un alumno en una asignatura */
    public ResponseEntity<String> matricularAlumno(String dniAlumno, String codigoAsignatura) {
        return repositoryMatricula.matricularAlumno(dniAlumno, codigoAsignatura);
    }

    /* Daremos de baja una asignatura de la matricula del alumno */
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumno(String dniAlumno, String codigoAsignatura) {
        return repositoryMatricula.bajaAsignaturaDeMatriculaDeAlumno(dniAlumno, codigoAsignatura);
    }

    /*
     * Daremos de baja todas las asignaturas de la matricula del alumno de forma
     * general
     */
    public ResponseEntity<String> desmatricularTodasLasAsignaturasDeUnAlumno(String dniAlumno) {
        return repositoryMatricula.desmatricularTodasLasAsignaturasDeUnAlumno(dniAlumno);
    }

    /* Baja de la asignatura de un alumno asociada a un modulo en concreto */
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumnoConGestionDeModulo(String dniAlumno,
            String codigoAsignatura, String codigoModulo) {

        return repositoryMatricula.bajaAsignaturaDeMatriculaDeAlumnoConGestionDeModulo(dniAlumno, codigoAsignatura,
                codigoModulo);
    }

    /* Obtencion de los alumnos de una asignatura para un modulo en concreto */
    public ResponseEntity<?> alumnosMatriculadosEnAsignaturaPorModulos(String codigoAsignatura,
            String codigoModulo) {
        return repositoryMatricula.alumnosMatriculadosEnAsignaturaPorModulos(codigoAsignatura, codigoModulo);
    }
}
