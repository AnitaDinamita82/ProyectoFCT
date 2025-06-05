package myProyectoDAW.gestionInstituciones.applications.ports;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryEstadisticas {

    ResponseEntity<?> numeroAlumnosPorAsignatura();

    ResponseEntity<?> alumnosConMasAsignaturas();

}
