package myProyectoDAW.gestionInstituciones.applications.ports;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

@Repository
public interface RepositoryAuthentication {

    ResponseEntity<String> signup(Usuario usuario);

    Usuario authenticate(Usuario usuario);

}
