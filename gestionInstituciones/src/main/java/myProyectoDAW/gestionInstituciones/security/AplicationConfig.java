package myProyectoDAW.gestionInstituciones.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import myProyectoDAW.gestionInstituciones.applications.services.UsuarioService;

/**
 * Clase de Configuración Principal para Spring Security y Autenticación.
 */
@Configuration
public class AplicationConfig {

    private static final Logger log = LoggerFactory.getLogger(AplicationConfig.class); // Crear logger estático

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Define el bean `UserDetailsService` que carga los detalles del usuario para
     * la autenticación.
     * Este servicio es utilizado por Spring Security para buscar un usuario por su
     * login durante el proceso
     * de autenticación.    
     */
    @Bean
    public UserDetailsService userDetailsService() {

        return login -> {
            try {
                return usuarioService.encontrarSiExisteUsuario(login);
            } catch (Exception e) {
                log.error("Error al buscar usuario con login: {}", login, e); // Añadir log de error
                throw e; // Re-lanzar la excepción para que se propague
            }
        };
    }

    /*
     * Define el bean 'PasswordEncoder' para la codificación y verificación de
     * contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Define el bean `AuthenticationManager` que coordina el proceso de
     * autenticación.
     * Este es un componente principal de Spring Security al que se le pasan las
     * credenciales de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /*
     * Define el bean 'AuthenticationProvider' que implementa la estrategia de
     * autenticación.
     * Se utiliza 'DaoAuthenticationProvider', que recupera los detalles del usuario
     * a través del 'UserDetailsService' y verifica la contraseña utilizando
     * el 'PasswordEncoder'.  
     * Por simplicidad no se estan encriptando las contraseñas 
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // Probar a cambiarlo por passwordEncoder()
        return authProvider;
    }
}
