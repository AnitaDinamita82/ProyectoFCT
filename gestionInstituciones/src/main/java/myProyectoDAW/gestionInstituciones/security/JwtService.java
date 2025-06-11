package myProyectoDAW.gestionInstituciones.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Servicio para la gestión de JSON Web Tokens (JWTs).
 * Esta clase se encarga de todas las operaciones relacionadas con los JWTs en
 * la aplicación:
 * - Generar nuevos tokens JWT para usuarios autenticados.
 * - Extraer información (claims) de un token JWT existente, como el nombre de
 * usuario.
 * - Validar la integridad y la expiración de un token JWT.
 * Utiliza la librería JJWT (Java JWT) para interactuar con los tokens.
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /* Extrae el nombre de usuario (sujeto) de un token JWT. */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /* Método genérico para extraer cualquier claim (afirmación) de un token JWT. */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    /*
     * Extrae todos los claims (cuerpo) de un token JWT.
     * Realiza el parseo del token y verifica su firma utilizando la clave secreta.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /* Obtiene la clave de firma (Key) a partir de la clave secreta Base64. */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /* Genera un token JWT para un usuario con claims por defecto. */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /* Genera un token JWT para un usuario con claims adicionales. */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /* Obtiene el tiempo de expiración configurado para los JWTs */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /*
     * Construye el token JWT con los claims, sujeto, fecha de emisión y expiración,
     * y lo firma
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /*
     * Valida un token JWT.
     * Comprueba si el nombre de usuario del token coincide con el de los
     * UserDetails y si el token no ha expirado.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {

        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /* Verifica si un token JWT ha expirado */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /* Extrae la fecha de expiración (claim 'exp') de un token JWT. */
    private Date extractExpiration(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration;
    }
}
