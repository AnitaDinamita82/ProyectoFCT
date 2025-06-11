package myProyectoDAW.gestionInstituciones.applications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryMatricula;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

/**
 * Capa de Servicio para la gestión de las Matriculas. Es quien implementa la
 * logica de
 * negocio para las operaciones relacionadas con las mismas
 * Utiliza la inyección de dependencia a través de << RepositoryMatricula >>
 * para delegar las interacciones con la capa de persistencia,
 * manteniendo una clara separación de responsabilidades en la arquitectura de
 * la aplicación.
 */
@Service
public class MatriculaService {

    @Autowired
    private RepositoryMatricula repositoryMatricula;

    /*
     * Definicion del metodo que dado un dni de un alumno lista todas su asignaturas
     * matriculadas
     */
    public List<Asignatura> listarAsignaturasDeAlumno(String dniAlumno) {

        return repositoryMatricula.listarAsignaturasDeAlumno(dniAlumno);
    }

    /*
     * Definicion del metodo que dado un codigo de asignatura lista todos los
     * alumnos matriculados
     */
    public List<Alumno> obtenerAlumnosMatriculados(String codigoAsignatura) {
        return repositoryMatricula.obtenerAlumnosMatriculados(codigoAsignatura);
    }

    /*
     * Definicion del metodo que verifica si un alumno tiene asignaturas asociadas
     */
    public boolean alumnoTieneAsignaturas(String dniAlumno) {
        return repositoryMatricula.alumnoTieneAsignaturas(dniAlumno);
    }

    /*
     * Definicion del metodo que verifica si una asignatura en cuestion tiene
     * alumnos matriculados
     */
    public boolean asignaturaTieneAlumnos(String codigoAsignatura) {

        return repositoryMatricula.asignaturasTieneAlumnos(codigoAsignatura);
    }

    /*
     * Definicion del metodo por el cual se matriculará a un alumno en una
     * asignatura
     */
    public ResponseEntity<String> matricularAlumno(String dniAlumno, String codigoAsignatura) {
        return repositoryMatricula.matricularAlumno(dniAlumno, codigoAsignatura);
    }

    /*
     * Deficicion de metodo por el que daremos de baja una asignatura de la
     * matricula del alumno
     */
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumno(String dniAlumno, String codigoAsignatura) {
        return repositoryMatricula.bajaAsignaturaDeMatriculaDeAlumno(dniAlumno, codigoAsignatura);
    }

    /*
     * Definicion del metodo por el que daremos de baja todas las asignaturas de la
     * matricula de un alumno de forma general
     */
    public ResponseEntity<String> desmatricularTodasLasAsignaturasDeUnAlumno(String dniAlumno) {
        return repositoryMatricula.desmatricularTodasLasAsignaturasDeUnAlumno(dniAlumno);
    }

    /*
     * Definicion del metodo por el que daremos de baja una asignatura en la
     * matricula de un alumno asociada a un modulo en concreto
     */
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumnoConGestionDeModulo(String dniAlumno,
            String codigoAsignatura, String codigoModulo) {

        return repositoryMatricula.bajaAsignaturaDeMatriculaDeAlumnoConGestionDeModulo(dniAlumno, codigoAsignatura,
                codigoModulo);
    }

    /*
     * Definicion del metodo para la obtencion de los alumnos de una asignatura para
     * un modulo en concreto
     */
    public ResponseEntity<?> alumnosMatriculadosEnAsignaturaPorModulos(String codigoAsignatura,
            String codigoModulo) {
        return repositoryMatricula.alumnosMatriculadosEnAsignaturaPorModulos(codigoAsignatura, codigoModulo);
    }
}
