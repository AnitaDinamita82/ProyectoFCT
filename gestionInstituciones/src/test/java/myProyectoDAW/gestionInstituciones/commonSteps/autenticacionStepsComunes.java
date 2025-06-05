package myProyectoDAW.gestionInstituciones.commonSteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import myProyectoDAW.gestionInstituciones.applications.services.UsuarioService;
import myProyectoDAW.gestionInstituciones.domain.models.Usuario;
import myProyectoDAW.gestionInstituciones.security.JwtService;

public class autenticacionStepsComunes {

    private Response response;
    private String login;
    private String password;
    private String token; // Añade esta variable para almacenar el token
    private Usuario usuario;

    @Autowired
    private UsuarioService usuarioService;

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }
    public Usuario getUsuario(){
        return usuario;
    }

    @Value("${api.auth.login.url:/auth/login}") // Valor por defecto en caso de que no este configurado.
    private String authLoginUrl;
    
    public void  credencialesUsuario(String login, String password) {
        setLogin(login);
        setPassword(password);
    }

    public Boolean existeUsuario(String login){

        Usuario newUser = usuarioService.encontrarSiExisteUsuario(login);

        if(newUser != null){
            setUsuario(newUser);
            return true;
        }
        return false;
    }

    public void enviarPeticionDeAutenticacion(String endpoint) {

        
       Response currentResponse = RestAssured.given()
            .contentType("application/json")
            .body("{\"login\":\"" + getLogin() + "\",\"password\":\"" + getPassword() + "\"}")
            .post(authLoginUrl);
            
            setResponse(currentResponse);

            // Guardar el token si la autenticación es exitosa
            if (getResponse().getStatusCode() == 200) {

                setToken(getResponse().jsonPath().getString("token"));
                log.info("Token de autenticación obtenido satisfactoriamente");
            } else {
                log.error("La autenticación falló con código: {}", getResponse().getStatusCode());
        }           
    }

    @Given("un usuario con login {string} y contraseña {string}")
    public void un_usuario_con_login_y_contraseña(String login, String password) {
       
          credencialesUsuario(login, password);
    }

    @Given("un usuario administrador autenticado con login {string} y contraseña {string}")
    public void un_usuario_administrador_autenticado_con_login_y_contraseña(String login, String password) {

         
            if (existeUsuario(login)){ // Si el usuario existe

                // Si el usuario es administrador y las contraseñas coinciden
                if("ADMIN".equals(getUsuario().getRol()) && password.equals(getUsuario().getPassword())){ 
                    credencialesUsuario(login, password); // Guardo sus creenciales
                    enviarPeticionDeAutenticacion(authLoginUrl); // envio la peticion de autenticacion.

                    if (response.getStatusCode() != 200) {
                        throw new AutenticacionFallidaException("Fallo de auenticacion para el usuario: " + getUsuario().getLogin());
                    }
                }else{
                    throw new CredencialesInvalidasException("Credenciales invalidas o el usuario no es Administrador");
                }

            } else{

                throw new UsuarioNoEncontradoException("No existe ningun usuario con login: " + login);
            }
    }

    
    @When("el usuario envia una solicitud POST a {string} con dichas credenciales")
    public void el_usuario_envia_una_solicitud_post_a_con_dichas_credenciales(String endpoint) {
       
        enviarPeticionDeAutenticacion(endpoint);
    }

// Excepciones personalizadas 


        public static class UsuarioNoEncontradoException extends RuntimeException {
            public UsuarioNoEncontradoException(String message) {
                super(message);
            }
        }

        public static class CredencialesInvalidasException extends RuntimeException {
            public CredencialesInvalidasException(String message) {
                super(message);
            }
        }

        public static class AutenticacionFallidaException extends RuntimeException {
            public AutenticacionFallidaException(String message) {
                super(message);
            }
        }
}