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

    /*
     * Dado un login de usuario saber si existe en base de datos y nos devuelve el
     * usuario
     */
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

    /* Nos devuelve si el usuario ya existe dado su DNI */
    public boolean existe(String dniUsuario) {
        return repositoryUsuario.existe(dniUsuario);
    }

    /* Obtener al usuario en si dado su DNI */
    public Usuario obtenerUsuarioDadoDni(String dniUsuario) {
        return repositoryUsuario.obtenerUsuarioDadoDni(dniUsuario);
    }

    /* Nos devuelve si el usuario existe o no dado su login */
    public Boolean existeLoginDeUsuario(String login) {
        return repositoryUsuario.existeLoginDeUsuario(login);
    }

}
