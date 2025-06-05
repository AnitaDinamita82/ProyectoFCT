package myProyectoDAW.gestionInstituciones.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        @Autowired
        private AuthenticationProvider authenticationProvider;

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(HttpMethod.OPTIONS, "/1.0/**").permitAll() // Permitir
                                                                                                            // OPTIONS
                                                                                                            // para
                                                                                                            // /1.0/**
                                                .requestMatchers("/h2-console/**").permitAll()
                                                .requestMatchers("/auth/**").permitAll() // Permitir acceso a /auth/**
                                                .anyRequest().authenticated() // Requiere autenticación para todas las
                                                                              // demás peticiones
                                )
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sesiones sin
                                                                                                        // estado
                                )
                                .authenticationProvider(authenticationProvider) // Configurar el proveedor de
                                                                                // autenticación
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Añadir
                                                                                                                      // filtro
                                                                                                                      // JWT
                                .headers(headers -> headers
                                                .frameOptions(frameOptions -> frameOptions.sameOrigin()));
                return http.build();
        }

}
