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

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService; // Para generar el token JWT.

    /* -- ENPOINT POST -- */

    // Registro de un usuario //
    @PostMapping("/signup")
    public ResponseEntity<String> registrar(@RequestBody UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioDTO.convertirDTOAEntity();
        return authenticationService.signup(usuario);
    }

    // Proceso de autenticación de un usuario //
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
