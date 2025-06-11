package myProyectoDAW.gestionInstituciones.adapters;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import myProyectoDAW.gestionInstituciones.adapters.entitys.UsuarioEntity;
import myProyectoDAW.gestionInstituciones.adapters.jpas.UsuarioJpaRepository;
import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryUsuario;
import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

/* Adaptador para la entidad Usuario */
@Component
public class UsuarioAdapter implements RepositoryUsuario {

    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;

    private Optional<UsuarioEntity> usuarioEntityOptional;

    /*
     * Operación que bbtiene una lista de todos los usuarios existentes en la base
     * de datos.
     */
    @Override
    public List<Usuario> listarUsuarios() {

        List<UsuarioEntity> usuarioEntities = usuarioJpaRepository.findAll();

        return usuarioEntities.stream()
                .map(this::convertirEntityAUsuario)
                .collect(Collectors.toList());
    }

    /* Opeeracion que da de alta a un nuevo usuario */
    @Override
    public Usuario darDeAltaUnUsuario(Usuario usuario) {

        UsuarioEntity usuarioEntity = convertirUsuarioAEntity(usuario);
        return convertirEntityAUsuario(usuarioJpaRepository.save(usuarioEntity));

    }

    /* Operacion que busca y obtiene un usuario dado su login */
    @Override
    public Usuario encontrarSiExisteUsuario(String login) {

        usuarioEntityOptional = usuarioJpaRepository.findByLogin(login);

        if (usuarioEntityOptional.isPresent()) {

            UsuarioEntity usuarioEntity = usuarioEntityOptional.get();
            return convertirEntityAUsuario(usuarioEntity);

        } else {
            System.out.println("No se encontró ningun usuario con el login: " + login);

        }
        return null;
    }

    /* Operación que da de baja (elimina) a un usuario del sistema. */
    @Override
    public Boolean eliminarUsuarioDadoLogin(String login) {

        usuarioEntityOptional = usuarioJpaRepository.findByLogin(login);

        if (usuarioEntityOptional.isPresent()) {

            UsuarioEntity usuarioEntity = usuarioEntityOptional.get();

            long totalUsuarios = usuarioJpaRepository.count();

            if (totalUsuarios == 1) {
                System.out.println("No se puede eliminar al único usuario existente en la base de datos."); // PUNTO DE
                                                                                                            // CONTROL
                return false;
            }
            usuarioJpaRepository.delete(usuarioEntity);
        }

        return true;
    }

    /* Operacion para la modificacion de datos de los usuarios */
    @Override
    public ResponseEntity<String> actualizarUsuario(Usuario usuario) {

        usuarioEntityOptional = usuarioJpaRepository.findByDni(usuario.getDni());

        if (usuarioEntityOptional.isPresent()) {

            UsuarioEntity usuarioEntity = usuarioEntityOptional.get();

            usuarioEntity.setDni(usuario.getDni());
            usuarioEntity.setLogin(usuario.getLogin());
            usuarioEntity.setPassword(usuario.getPassword());
            usuarioEntity.setRol(usuario.getRol());
            usuarioEntity.setCreatedAt(usuario.getCreatedAt());
            usuarioEntity.setUpdatedAt(usuario.getUpdatedAt());

            usuarioJpaRepository.save(usuarioEntity);
            convertirEntityAUsuario(usuarioEntity);
            return new ResponseEntity<>("Modificación realizada con éxito.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Parece que ha habido un error con la actualización del usuario.", HttpStatus.OK);
    }

    /* Operación para obtener el numero total de usuarios conectados */
    @Override
    public long obtenerNumeroTotalDeUsuarios() {
        return usuarioJpaRepository.count();
    }

    /* Operación para saber si un determinado usuario existe dado su dni */
    @Override
    public boolean existe(String dniUsuario) {
        return usuarioJpaRepository.findByDni(dniUsuario).isPresent();
    }

    /* Operacion para obtener a un usuario por su DNI */
    @Override
    public Usuario obtenerUsuarioDadoDni(String dniUsuario) {

        usuarioEntityOptional = usuarioJpaRepository.findByDni(dniUsuario);
        UsuarioEntity usuarioEntity = usuarioEntityOptional.get();
        return convertirEntityAUsuario(usuarioEntity);
    }

    /* Operacion para saber si ya existe un determinado login */
    @Override
    public Boolean existeLoginDeUsuario(String login) {
        return usuarioJpaRepository.findByLogin(login).isPresent();
    }

    /* -- METODOS DE CONVERSION -- */

    private UsuarioEntity convertirUsuarioAEntity(Usuario usuario) {

        UsuarioEntity usuarioEntity = new UsuarioEntity();

        usuarioEntity.setId(usuario.getId());
        usuarioEntity.setDni(usuario.getDni());
        usuarioEntity.setLogin(usuario.getLogin());
        usuarioEntity.setPassword(usuario.getPassword());
        usuarioEntity.setRol(usuario.getRol());
        return usuarioEntity;
    }

    public Usuario convertirEntityAUsuario(UsuarioEntity usuarioEntity) {

        Usuario usuario = new Usuario();

        usuario.setId(usuarioEntity.getId());
        usuario.setDni(usuarioEntity.getDni());
        usuario.setLogin(usuarioEntity.getLogin());
        usuario.setPassword(usuarioEntity.getPassword());
        usuario.setRol(usuarioEntity.getRol());
        return usuario;
    }
}
