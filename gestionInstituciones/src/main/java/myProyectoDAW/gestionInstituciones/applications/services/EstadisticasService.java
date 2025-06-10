package myProyectoDAW.gestionInstituciones.applications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryEstadisticas;

@Service
public class EstadisticasService {

    @Autowired
    private RepositoryEstadisticas repositoryEstadisticas;

    /* Obtener la cuenta del numero de alumnos matriculados en una asignatura */
    public ResponseEntity<?> numeroAlumnosPorAsignatura() {
        return repositoryEstadisticas.numeroAlumnosPorAsignatura();
    }

    /* Obtener la cuenta de los alumnos con mas asignaturas matriculadas */
    public ResponseEntity<?> alumnosConMasAsignaturas() {
        return repositoryEstadisticas.alumnosConMasAsignaturas();
    }

}
