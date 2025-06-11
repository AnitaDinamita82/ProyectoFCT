package myProyectoDAW.gestionInstituciones.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* Filtro de Autenticación JWT para Spring Security.
 * Esta clase extiende `OncePerRequestFilter` para asegurar que se ejecuta una única vez por cada solicitud HTTP.
 * Su función principal es interceptar las solicitudes, extraer y validar los Tokens Web JSON (JWT)
 * presentes en el encabezado `Authorization`, y configurar el contexto de seguridad de Spring si el token es válido.
 * Esto permite proteger los endpoints de la API que requieren autenticación, sin requerir que las credenciales se envíen en cada solicitud.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    /*
     * Método principal del filtro que se ejecuta una vez por cada solicitud HTTP.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Intenta extraer el encabezado "Authorization" de la solicitud.
        final String authHeader = request.getHeader("Authorization");

        /*
         * Paso 1: Excluir rutas de autenticación.
         * Si la ruta de la solicitud comienza con "/auth", significa que es un endpoint
         * de autenticación (login/signup)
         * y no requiere validación de token. Se pasa directamente al siguiente filtro
         */
        if (request.getServletPath().startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        /*
         * Paso 2: Validar la presencia y el formato del encabezado "Authorization".
         * Si el encabezado es nulo o no empieza con "Bearer ", no hay un JWT válido.
         * Se pasa la solicitud al siguiente filtro (permitiendo el acceso a recursos
         * públicos o denegación por otras reglas).
         */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            /* Paso 3: Extraer el token JWT. */
            final String jwt = authHeader.substring(7);
            final String login = jwtService.extractUsername(jwt);

            /*
             * Paso 4: Verificar si ya hay una autenticación en el contexto de seguridad.
             * Si login no es nulo (es decir, se pudo extraer del token)
             * Y no hay una autenticación existente en el contexto de seguridad
             */
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (login != null && authentication == null) {
                /*
                 * Paso 5: Cargar los detalles del usuario.
                 * Utiliza el UserDetailsService para cargar los detalles completos del usuario
                 * por su login.
                 */
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(login);

                /*
                 * Paso 6: Validar el token JWT con los detalles del usuario.
                 * Comprueba si el token es válido (no expirado, firma correcta, etc.).
                 */

                if (jwtService.isTokenValid(jwt, userDetails)) {

                    /*
                     * Paso 7: Crear y configurar el objeto de autenticación.
                     * Crea un UsernamePasswordAuthenticationToken con los detalles del usuario y
                     * sus autoridades (roles).
                     */
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, // El principal (el usuario autenticado).
                            null, // Las credenciales (la contraseña) no son necesarias en este punto del filtro.
                            userDetails.getAuthorities() // Las autoridades (roles) del usuario.
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    /* Paso 8: Establecer la autenticación en el 'SecurityContextHolder'. */
                    /*
                     * Esto hace que el usuario esté "autenticado" para el resto de la solicitud en
                     * Spring Security.
                     */
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // **Paso 9: Registrar la autenticación exitosa (si aplica).**
            if (login != null) {
                logger.info("\n");
                logger.info("Usuario " + login + " autenticado con exito");
                logger.info("\n");
            }
            // **Paso 10: Continuar la cadena de filtros.**
            filterChain.doFilter(request, response);
        } catch (ServletException | IOException | UsernameNotFoundException exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
