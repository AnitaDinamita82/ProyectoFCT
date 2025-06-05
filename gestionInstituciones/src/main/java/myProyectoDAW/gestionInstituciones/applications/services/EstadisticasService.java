package myProyectoDAW.gestionInstituciones.applications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryEstadisticas;

@Service
public class EstadisticasService {

    @Autowired
    private RepositoryEstadisticas repositoryEstadisticas;

    public ResponseEntity<?> numeroAlumnosPorAsignatura() {
        return repositoryEstadisticas.numeroAlumnosPorAsignatura();
    }

    public ResponseEntity<?> alumnosConMasAsignaturas() {
        return repositoryEstadisticas.alumnosConMasAsignaturas();
    }

}
