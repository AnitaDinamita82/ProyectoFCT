package myProyectoDAW.gestionInstituciones.applications.ports;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

@Repository
public interface RepositoryAsignatura {

    Asignatura altaAsignatura(Asignatura asignatura);

    List<Asignatura> listarAsignaturas();

    Boolean eliminarAsignaturaDadoCodigo(String codigoAsignatura);

    ResponseEntity<String> actualizarAsignatura(Asignatura asignatura);

    Asignatura encontrarSiExisteAsignatura(String codigo);

    boolean existe(String codigoAsignatura);

}
