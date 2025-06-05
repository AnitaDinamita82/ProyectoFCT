package myProyectoDAW.gestionInstituciones.applications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryAuthentication;
import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

@Service
public class AuthenticationService {

    @Autowired
    private RepositoryAuthentication repositoryAuthentication;

    public ResponseEntity<String> signup(Usuario usuario) {
        return repositoryAuthentication.signup(usuario);
    }

    public Usuario authenticate(Usuario usuario) {
        return repositoryAuthentication.authenticate(usuario);
    }

}
