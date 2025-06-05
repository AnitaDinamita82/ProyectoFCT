package myProyectoDAW.gestionInstituciones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
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
