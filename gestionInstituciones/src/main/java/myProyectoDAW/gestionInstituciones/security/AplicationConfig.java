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


@Configuration
public class AplicationConfig {

    private static final Logger log = LoggerFactory.getLogger(AplicationConfig.class); // Crear logger estático

    @Autowired
    private UsuarioService usuarioService;
    
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

    //crea una instancia de BCryptPasswordEncoder() que se utiliza para codificar la contraseña del usuario
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //establece la nueva estrategia para realizar la autenticación.
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return authProvider;
    }

}
