package myProyectoDAW.gestionInstituciones.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import myProyectoDAW.gestionInstituciones.adapters.entitys.UsuarioEntity;
import myProyectoDAW.gestionInstituciones.adapters.jpas.UsuarioJpaRepository;
import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryAuthentication;
import myProyectoDAW.gestionInstituciones.applications.services.UsuarioService;
import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

@Component
public class AuthenticationAdapter implements RepositoryAuthentication {

    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<String> signup(Usuario usuario) {

        // Comprobacion para el DNI
        Boolean existeUsuarioPorDni = usuarioService.existe(usuario.getDni());

        if (existeUsuarioPorDni) { // Estamos intentando registrar un usuario que ya esta dado de alta en la BD.
            return new ResponseEntity<>(
                    "Su usuario ya está dado de alta. Pruebe a iniciar sesion con sus credenciales.",
                    HttpStatus.CONFLICT);
        }

        // Comprobacion para el login
        Boolean existeUsuarioPorLogin = usuarioService.existeLoginDeUsuario(usuario.getLogin());

        if (existeUsuarioPorLogin) {
            return new ResponseEntity<>(
                    "Este nombre de usuario " + usuario.getLogin() + " ya está en uso. Por favor, elija otro distinto",
                    HttpStatus.CONFLICT);
        }

        UsuarioEntity usuarioEntity = convertirUsuarioAEntity(usuario);
        usuarioJpaRepository.save(usuarioEntity);
        return new ResponseEntity<>("¡Enhorabuena!! Cuenta registrada con éxito. Inicie sesión con sus credenciales",
                HttpStatus.OK);
    }

    @Override
    public Usuario authenticate(Usuario usuario) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuario.getLogin(),
                        usuario.getPassword()));

        Usuario usuarioEncontrado = usuarioService.encontrarSiExisteUsuario(usuario.getLogin());
        return usuarioEncontrado;

    }

    /* Metodos de conversion */

    private UsuarioEntity convertirUsuarioAEntity(Usuario usuario) {

        UsuarioEntity usuarioEntity = new UsuarioEntity();

        usuarioEntity.setId(usuario.getId());
        usuarioEntity.setDni(usuario.getDni());
        usuarioEntity.setLogin(usuario.getLogin());
        usuarioEntity.setPassword(usuario.getPassword());
        usuarioEntity.setRol(usuario.getRol());
        return usuarioEntity;
    }
}
