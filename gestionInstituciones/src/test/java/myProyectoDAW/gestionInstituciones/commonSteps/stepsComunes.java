package myProyectoDAW.gestionInstituciones.commonSteps;

import org.junit.Assert;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import myProyectoDAW.gestionInstituciones.applications.services.AlumnoService;
import myProyectoDAW.gestionInstituciones.applications.services.AsignaturaService;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;


public class stepsComunes {

    private Response response;
    private String token;

    @Autowired
    private autenticacionStepsComunes autenticacionStepsComunes;

    @Autowired
    private AsignaturaService asignaturaService;

    @Autowired
    private AlumnoService alumnoService;

       
    public Response enviarPeticionConToken(String endpoint, String jsonBody, String httpMethod) {

        token = autenticacionStepsComunes.getToken(); // Obtiene el token almacenado.
        RequestSpecification request = RestAssured.given().header("Authorization", "Bearer " + token);

        // El cuerpo JSON se añade a la petición solo si jsonBody no es nulo ni vacío
        if (jsonBody != null && !jsonBody.isEmpty()) {
            request.contentType("application/json").body(jsonBody);
        }

        switch (httpMethod.toUpperCase()) {
            case "GET" -> response = request.get(endpoint);
            case "POST" -> response = request.post(endpoint);
            case "PUT" -> response = request.put(endpoint);
            case "DELETE" -> response = request.delete(endpoint);
            default -> throw new IllegalArgumentException("Metodo HTTP no soportado: " + httpMethod);
        }
        autenticacionStepsComunes.setResponse(response); // Guardamos la respuesta
        return response;
    }

    
    @Given("un alumno con DNI {string} existente en el sistema")
    public void un_alumno_con_dni_existente_en_el_sistema(String dni) {
        
        Alumno alumno = alumnoService.encontrarSiExisteAlumno(dni);
        Assert.assertNotNull(alumno);
    }
    @
    Given("una asignatura con codigo {string} existente en el sistema")
    public void una_asignatura_con_codigo_existente_en_el_sistema(String codigo) {
        
        Asignatura  asignatura = asignaturaService.encontrarSiExisteAsignatura(codigo);
        Assert.assertNotNull(asignatura);
    }
    
    @When("se realiza una peticion POST a {string} con los siguientes datos:")
    public void se_realiza_una_peticion_post_a_con_los_siguientes_datos(String endpoint, String jsonBody ) {

        enviarPeticionConToken(endpoint, jsonBody,"POST");
    } 

    @When("se realiza una peticion GET a {string}")
    public void se_realiza_una_peticion_get_a_listar_asignaturas(String endpoint) {

        enviarPeticionConToken(endpoint, null, "GET"); // Especifica GET y cuerpo null
    }
    
    @When("se realiza una peticion PUT a {string} con los siguientes datos:")
    public void se_realiza_una_peticion_put_a_con_los_siguientes_datos(String endpoint, String jsonBody) {
        
        enviarPeticionConToken(endpoint, jsonBody,"PUT");
    }

    @When("se realiza una peticion DELETE a {string}")
    public void se_realiza_una_peticion_delete_a(String endpoint) {
        
        enviarPeticionConToken(endpoint,null,"DELETE");
    }

    @Then("el codigo de respuesta debe ser {int}")
    public void respuesta_codigo_estado(int codigoEstado) {

        Assert.assertEquals(codigoEstado,autenticacionStepsComunes.getResponse().getStatusCode());
    } 

    @Then("se imprimira un mensaje indicando {string}")
    public void mensaje_respuesta(String mensajeEsperado) {
        // Verifica que el mensaje de respuesta contiene el mensaje esperado
        String mensajeRespuesta = autenticacionStepsComunes.getResponse().getBody().asString();
        assertTrue(mensajeRespuesta.contains(mensajeEsperado));
    }

    @Then("se imprimirá un mensaje indicando {string}")
    public void imprimir_mensaje_indicando(String mensajeEsperado) {
        String mensajeRespuesta = autenticacionStepsComunes.getResponse().getBody().asString();
        assertTrue(mensajeRespuesta.contains(mensajeEsperado));
    }
}
