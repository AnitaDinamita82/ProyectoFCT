package myProyectoDAW.gestionInstituciones.adapters;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import myProyectoDAW.gestionInstituciones.adapters.dtos.AlumnoAsignaturasDTO;
import myProyectoDAW.gestionInstituciones.adapters.dtos.AsignaturaAlumnosDTO;
import myProyectoDAW.gestionInstituciones.adapters.jpas.AlumnoJpaRepository;
import myProyectoDAW.gestionInstituciones.adapters.jpas.AsignaturaJpaRepository;
import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryEstadisticas;

@Component
public class EstadisticasAdapter implements RepositoryEstadisticas {

    @Autowired
    private AlumnoJpaRepository alumnoJpaRepository;

    @Autowired

    private AsignaturaJpaRepository asignaturaJpaRepository;

    @Override
    public ResponseEntity<?> numeroAlumnosPorAsignatura() {

        List<AsignaturaAlumnosDTO> listStats = asignaturaJpaRepository.countAlumnosByAsignatura();

        if (listStats.isEmpty()) {
            return new ResponseEntity<>("Datos incongruentes.", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(listStats, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> alumnosConMasAsignaturas() {
        List<AlumnoAsignaturasDTO> listStats = alumnoJpaRepository.countAsignaturasByAlumnoOrdered();

        if (listStats.isEmpty()) {
            return new ResponseEntity<>("Datos incongruentes.", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(listStats, HttpStatus.OK);
    }

}
