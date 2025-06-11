package myProyectoDAW.gestionInstituciones.adapters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myProyectoDAW.gestionInstituciones.adapters.dtos.UsuarioDTO;
import myProyectoDAW.gestionInstituciones.applications.services.AuthenticationService;
import myProyectoDAW.gestionInstituciones.domain.models.LoginResponse;
import myProyectoDAW.gestionInstituciones.domain.models.Usuario;
import myProyectoDAW.gestionInstituciones.security.JwtService;

/**
 * Clase controladora REST para la autenticación de usuarios.
 * Maneja las solicitudes relacionadas con el registro y el inicio de sesión de
 * usuarios.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService; // Para generar el token JWT.

    /* -- ENPOINTS POST -- */

    // ENDPOINT PARA EL PROCESO DE REGISTRO DE UN USUARIO //
    @PostMapping("/signup")
    public ResponseEntity<String> registrar(@RequestBody UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioDTO.convertirDTOAEntity();
        return authenticationService.signup(usuario);
    }

    // ENDPINT PARA EL PROCESO DE AUTENTICACION DE UN USUARIO //
    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody UsuarioDTO usuarioDTO) {

        try {
            Usuario usuario = usuarioDTO.convertirDTOAEntity();
            Usuario usuarioAutenticado = authenticationService.authenticate(usuario);

            String jwtToken = jwtService.generateToken(usuarioAutenticado);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());

            return ResponseEntity.ok(loginResponse);

        } catch (AuthenticationException e) {
            // Credenciales incorrectas
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("AutenticaciÓn fallida."));
        }
    }

}
