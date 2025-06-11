package myProyectoDAW.gestionInstituciones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de Configuración CORS (Cross-Origin Resource Sharing).
 * Esta clase se encarga de definir las políticas CORS para la aplicación Spring
 * Boot, permitiendo que las solicitudes HTTP desde orígenes diferentes puedan
 * acceder a los recursos de
 * esta API.
 * Sin esta configuración, el navegador bloquea las solicitudes debido a la
 * política de mismo origen.
 */
@Configuration
public class CorsConfig {

    /*
     * Define y configura el bean `WebMvcConfigurer` para gestionar las reglas CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
                registry.addMapping("/auth/**")
                        .allowedOrigins("http://localhost:8081")
                        .allowedMethods("POST", "GET", "OPTIONS", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);

                // Configuración CORS para todas las rutas bajo /1.0/ (con soporte para
                // Authorization header)
                registry.addMapping("/1.0/**")
                        .allowedOrigins("http://localhost:8081")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Incluye OPTIONS
                        .allowedHeaders("Content-Type", "Authorization") // Asegúrate de incluir Authorization
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }

}
