package myProyectoDAW.gestionInstituciones.applications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryUsuario;
import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

@Service
public class UsuarioService {

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    /* Listar todos los usuarios de la base de datos */
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = repositoryUsuario.listarUsuarios();
        return usuarios;
    }

    /* Añadir un nuevo usuario */
    public Usuario darDeAltaUnUsuario(Usuario usuario) {

        return repositoryUsuario.darDeAltaUnUsuario(usuario);
    }

    /* Dado un login de usuario saber si existe en base de datos */
    public Usuario encontrarSiExisteUsuario(String login) {

        return repositoryUsuario.encontrarSiExisteUsuario(login);
    }

    /* Dar de baja un usuario dado su login */
    public Boolean darDeBajaUnUsuario(String login) {

        return (repositoryUsuario.eliminarUsuarioDadoLogin(login));
    }

    /* Actualizar un usuario */
    public ResponseEntity<String> actualizarUsuario(Usuario usuario) {
        return repositoryUsuario.actualizarUsuario(usuario);
    }

    /* Obtener el numero de usuarios en BD */

    public long obtenerNumeroTotalDeUsuarios() {
        return repositoryUsuario.obtenerNumeroTotalDeUsuarios();
    }

    /* Nos devuelve si el usuario ya existe */
    public boolean existe(String dniUsuario) {
        return repositoryUsuario.existe(dniUsuario);
    }

    public Usuario obtenerUsuarioDadoDni(String dniUsuario) {
        return repositoryUsuario.obtenerUsuarioDadoDni(dniUsuario);
    }

}
