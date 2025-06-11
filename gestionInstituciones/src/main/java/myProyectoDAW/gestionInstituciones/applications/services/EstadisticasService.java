package myProyectoDAW.gestionInstituciones.applications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryEstadisticas;

/**
 * Capa de Servicio para la gestión de las estadisticas.
 */
@Service
public class EstadisticasService {

    @Autowired
    private RepositoryEstadisticas repositoryEstadisticas;

    /*
     * Defiicion del metodo para obtener la cuenta del numero de alumnos
     * matriculados en una asignatura
     */
    public ResponseEntity<?> numeroAlumnosPorAsignatura() {
        return repositoryEstadisticas.numeroAlumnosPorAsignatura();
    }

    /*
     * Definicion del metodo para obtener una lista de alumnos con la cantidad de
     * asignaturas en las que están matriculados.
     */
    public ResponseEntity<?> alumnosConMasAsignaturas() {
        return repositoryEstadisticas.alumnosConMasAsignaturas();
    }

}
