package myProyectoDAW.gestionInstituciones.adapters.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myProyectoDAW.gestionInstituciones.adapters.dtos.UsuarioDTO;
import myProyectoDAW.gestionInstituciones.applications.services.UsuarioService;
import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

@RestController
@RequestMapping("{version}/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /* -- ENDPOINT GET -- */

    // LISTAR USUARIOS //
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarUsuarios() {

        List<Usuario> usuarios = usuarioService.listarUsuarios();

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // BUSCAR POR DNI DE USUARIO //
    @GetMapping("/buscar/{dniUsuario}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable String dniUsuario) {

        Boolean existeUsuario = usuarioService.existe(dniUsuario);

        if (existeUsuario) {

            Usuario usuario = usuarioService.obtenerUsuarioDadoDni(dniUsuario);
            return ResponseEntity.ok(usuario);

        } else {
            return ResponseEntity.notFound().build(); // Devuelve código 404 Not Found si no se encuentra
        }
    }

    /* -- ENDPOINT POST -- */

    // ALTA DE UN USUARIO //
    @PostMapping("/alta")
    public ResponseEntity<?> altaUsuario(@RequestBody UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioDTO.convertirDTOAEntity();

        // Comprobamos si el DNI ya esxiste
        if (usuarioService.existe(usuario.getDni())) {
            return new ResponseEntity<>("El usuario con DNI: " + usuario.getDni() + " ya existe en el sistema.",
                    HttpStatus.CONFLICT);
        }
        // Comprobamos si el Login de usuario ya existe.
        if (usuarioService.existeLoginDeUsuario(usuario.getLogin())) {
            return new ResponseEntity<>(
                    "El login " + usuario.getLogin() + " ya está siendo utilizado por otra persona.",
                    HttpStatus.CONFLICT);
        }
        // Actualizamos usuario
        usuarioService.darDeAltaUnUsuario(usuario);
        return new ResponseEntity<>("Se ha dado al usuario " + usuario.getLogin() + " de alta con éxito",
                HttpStatus.OK);
    }

    /* -- ENDPOINT DELETE -- */

    // ELIMINAR USUARIO DADO SU DNI //
    @DeleteMapping("/baja/{dniUsuario}")
    public ResponseEntity<String> bajaUsuario(@PathVariable String dniUsuario) {

        Boolean existeUsuario = usuarioService.existe(dniUsuario);

        if (existeUsuario) {

            Usuario usuario = usuarioService.obtenerUsuarioDadoDni(dniUsuario);

            Boolean bajaExitosa = usuarioService.darDeBajaUnUsuario(usuario.getLogin());

            if (bajaExitosa) {
                return ResponseEntity.ok("Se ha dado de baja al usuario de manera satisfactoria.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No se puede eliminar al único usuario existente en el sistema.");
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se puede dar de baja un usuario que no existe.");
        }
    }

    /* -- ENDPOINT PUT -- */
    // ACTUALIZAR DATOS DE UN USUARIO //
    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarUsuario(@RequestBody UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioDTO.convertirDTOAEntity();
        return usuarioService.actualizarUsuario(usuario);
    }
}
