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

@Component
public class UsuarioAdapter implements RepositoryUsuario {

    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;

    private Optional<UsuarioEntity> usuarioEntityOptional;

    @Override // Operación de Listar todos los usuarios de la BD
    public List<Usuario> listarUsuarios() {

        List<UsuarioEntity> usuarioEntities = usuarioJpaRepository.findAll();

        return usuarioEntities.stream()
                .map(this::convertirEntityAUsuario)
                .collect(Collectors.toList());
    }

    @Override // Operación para dar de alta un usuario.
    public Usuario darDeAltaUnUsuario(Usuario usuario) {

        UsuarioEntity usuarioEntity = convertirUsuarioAEntity(usuario);
        return convertirEntityAUsuario(usuarioJpaRepository.save(usuarioEntity));

    }

    @Override // Operación de busqueda y obtención de un usuario dado su login.
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

    @Override // Operación para dar de baja un usuario dado su login
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

    @Override // Operación para la modificación de datos de un usuario.
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

    @Override // Operación para obtener el numero total de usuarios conectados
    public long obtenerNumeroTotalDeUsuarios() {
        return usuarioJpaRepository.count();
    }

    @Override // Operación para saber si un determinado usuario existe dado su dni
    public boolean existe(String dniUsuario) {
        return usuarioJpaRepository.findByDni(dniUsuario).isPresent();
    }

    @Override // Operación para obtener al usuario dado su DNI
    public Usuario obtenerUsuarioDadoDni(String dniUsuario) {

        usuarioEntityOptional = usuarioJpaRepository.findByDni(dniUsuario);
        UsuarioEntity usuarioEntity = usuarioEntityOptional.get();
        return convertirEntityAUsuario(usuarioEntity);
    }

    @Override // Operación para saber si existe un usuario dado su Login.
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
