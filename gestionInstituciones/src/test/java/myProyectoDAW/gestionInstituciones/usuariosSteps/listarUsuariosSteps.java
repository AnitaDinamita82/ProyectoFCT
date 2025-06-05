package myProyectoDAW.gestionInstituciones.usuariosSteps;

import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.And;
import io.restassured.response.Response;
import myProyectoDAW.gestionInstituciones.commonSteps.autenticacionStepsComunes;
import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

public class listarUsuariosSteps {

    @Autowired
    private autenticacionStepsComunes autenticacionStepsComunes;

    private Response response;

    @And("la respuesta debe de ser una lista de usuarios")
    public void la_respuesta_debe_de_ser_una_lista_de_usuarios(){

        response = autenticacionStepsComunes.getResponse(); // Obtiene la respuesta almacenada.

        // Verifica que la respuesta sea una lista y que contenga asignaturas
        List<Usuario> usuarios = response.jsonPath().getList(".", Usuario.class); 
        Assert.assertNotNull(usuarios);
        Assert.assertTrue(!usuarios.isEmpty()); 

         // Imprime la lista de usuarios en la consola
        System.out.println("Lista de usuarios:"); 
        
        for (Usuario usuario : usuarios) {
            System.out.println("  - DNI: " + usuario.getDni());
            System.out.println("  - Login: " + usuario.getLogin());
            System.out.println("  - ROL: " + usuario.getRol());
            System.out.println(" ");
        }
    }
}   

