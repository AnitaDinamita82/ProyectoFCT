package myProyectoDAW.gestionInstituciones.applications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryUsuario;
import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

/**
 * Capa de Servicio para la gestión de Usuarios. Es quien implementa la logica
 * de
 * negocio para las operaciones relacionadas con los usuarios
 * Utiliza la inyección de dependencia a través de << RepositoryUsuario >> para
 * delegar las interacciones con la capa de persistencia,
 * manteniendo una clara separación de responsabilidades en la arquitectura de
 * la aplicación.
 */
@Service
public class UsuarioService {

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    /* Definición del metodo para listar todos los usuarios de la base de datos */
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = repositoryUsuario.listarUsuarios();
        return usuarios;
    }

    /* Definicion del metodo para añadir un nuevo usuario */
    public Usuario darDeAltaUnUsuario(Usuario usuario) {

        return repositoryUsuario.darDeAltaUnUsuario(usuario);
    }

    /* Definicion del metodo para obtener al usuario en si dado su login */
    public Usuario encontrarSiExisteUsuario(String login) {

        return repositoryUsuario.encontrarSiExisteUsuario(login);
    }

    /* Definicion del metodo para dar de baja un usuario dado su login */
    public Boolean darDeBajaUnUsuario(String login) {

        return (repositoryUsuario.eliminarUsuarioDadoLogin(login));
    }

    /* Definicion del metodo para actualizar un usuario */
    public ResponseEntity<String> actualizarUsuario(Usuario usuario) {
        return repositoryUsuario.actualizarUsuario(usuario);
    }

    /* Definicion del metodo para obtener el numero de usuarios en BD */
    public long obtenerNumeroTotalDeUsuarios() {
        return repositoryUsuario.obtenerNumeroTotalDeUsuarios();
    }

    /* Definicion del metodo que nos devuelve si el usuario ya existe dado su DNI */
    public boolean existe(String dniUsuario) {
        return repositoryUsuario.existe(dniUsuario);
    }

    /* Definicion del metodo para obtener al usuario en si dado su DNI */
    public Usuario obtenerUsuarioDadoDni(String dniUsuario) {
        return repositoryUsuario.obtenerUsuarioDadoDni(dniUsuario);
    }

    /*
     * Definicion del metodo qque nos devuelve si el usuario existe o no dado su
     * login
     */
    public Boolean existeLoginDeUsuario(String login) {
        return repositoryUsuario.existeLoginDeUsuario(login);
    }

}
