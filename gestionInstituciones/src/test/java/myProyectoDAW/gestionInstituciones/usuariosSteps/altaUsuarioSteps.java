package myProyectoDAW.gestionInstituciones.usuariosSteps;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import myProyectoDAW.gestionInstituciones.commonSteps.autenticacionStepsComunes;
import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

public class altaUsuarioSteps {


    private Response response;
    
    @Autowired
    private autenticacionStepsComunes autenticacionStepsComunes;

    @Then("la respuesta debe contener los datos del nuevo usuario creado")
    public void la_respuesta_debe_contener_los_datos_del_nuevo_usuario_creado() {

        response = autenticacionStepsComunes.getResponse(); // Obtiene la respuesta almacenada

        // Obtengo el objeto Usuario
        Usuario usuario = response.jsonPath().getObject(".", Usuario.class);

        Assert.assertNotNull(usuario); // Verificamos que el objeto Usuario no sea nulo.
        Assert.assertNotNull(usuario.getDni()); // Verificamos que el DNI del nuevo usuario no sea nulo.

          // Imprime los campos del usuario en la consola
          System.out.println("Usuario creado:");
          System.out.println("  - DNI: " + usuario.getDni());
          System.out.println("  - Login: " + usuario.getLogin());
          System.out.println("  - Password: " + usuario.getPassword());
    }
}
