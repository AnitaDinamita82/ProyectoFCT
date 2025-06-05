package myProyectoDAW.gestionInstituciones.applications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryAsignatura;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

@Service
public class AsignaturaService {

    @Autowired
    private RepositoryAsignatura repositoryAsignatura;

    /* Listar todas las asignaturas */
    public List<Asignatura> listarAsignaturas() {
        return repositoryAsignatura.listarAsignaturas();
    }

    /* Añadir una nueva asignatura */
    public Asignatura darDeAltaUnaAsignatura(Asignatura asignatura) {
        return repositoryAsignatura.altaAsignatura(asignatura);
    }

    /* Elimiar una asignatura dado un codigo */
    public Boolean darDeBajaUnaAsignatura(String codigoAsignatura) {
        return repositoryAsignatura.eliminarAsignaturaDadoCodigo(codigoAsignatura);
    }

    /* Actualizar una asignatura */
    public ResponseEntity<String> actualizarAsignatura(Asignatura asignatura) {
        return repositoryAsignatura.actualizarAsignatura(asignatura);
    }

    /* Encontrar una asignatura dado un codigo */
    public Asignatura encontrarSiExisteAsignatura(String codigoAsignatura) {
        return repositoryAsignatura.encontrarSiExisteAsignatura(codigoAsignatura);
    }

    /* Comprobar si la asignatura ya existe */
    public boolean existe(String codigoAsignatura) {
        return repositoryAsignatura.existe(codigoAsignatura);
    }

}
