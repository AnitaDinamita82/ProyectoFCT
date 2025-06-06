package myProyectoDAW.gestionInstituciones.applications.ports;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

@Repository
public interface RepositoryUsuario {

     public List<Usuario> listarUsuarios();

     public Usuario darDeAltaUnUsuario(Usuario usuario);

     public Usuario encontrarSiExisteUsuario(String login);

     public Boolean eliminarUsuarioDadoLogin(String login);

     public ResponseEntity<String> actualizarUsuario(Usuario usuario);

     public long obtenerNumeroTotalDeUsuarios();

     public boolean existe(String dniUsuario);

     public Usuario obtenerUsuarioDadoDni(String dniUsuario);

     public Boolean existeLoginDeUsuario(String login);

}
