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

/**
 * **Adaptador para la autenticación de usuarios.**
 *
 * Esta clase implementa la interfaz 'RepositoryAuthentication', actuando como
 * un adaptador de dominio los mecanismos de autenticación.
 * Se encarga de manejar el registro y la autenticación de usuarios, utilizando
 * el 'UsuarioJpaRepository', el 'UsuarioService' y el 'AuthenticationManager'
 * de Spring Security para el
 * proceso de login.
 */
@Component
public class AuthenticationAdapter implements RepositoryAuthentication {

    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /* Procesa el registro de un nuevo usuario en el sistema */
    @Override
    public ResponseEntity<String> signup(Usuario usuario) {

        // Comprobacion para el DNI
        Boolean existeUsuarioPorDni = usuarioService.existe(usuario.getDni());

        if (existeUsuarioPorDni) { // Estamos intentando registrar un usuario que ya esta dado de alta en la BD.
            return new ResponseEntity<>(
                    "Este DNI ya está registrado en el sistema. Pruebe a iniciar sesión con sus credenciales.",
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
        return new ResponseEntity<>("¡Enhorabuena!! Cuenta registrada con éxito. Ya puede iniciar sesión.",
                HttpStatus.OK);
    }

    /* Proceso de autenticacion de un usuario */
    @Override
    public Usuario authenticate(Usuario usuario) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( // Crea un token de autenticación con el login y la contraseña.
                        usuario.getLogin(),
                        usuario.getPassword()));

        Usuario usuarioEncontrado = usuarioService.encontrarSiExisteUsuario(usuario.getLogin());
        return usuarioEncontrado;

    }

    // METODOS DE CONVERSION //
    /*
     * Se definen estos metodos de coversion dado que JPA Repository trabaja con el
     * modelo de datos entity para asegurar la persistencia de los datos
     */

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
