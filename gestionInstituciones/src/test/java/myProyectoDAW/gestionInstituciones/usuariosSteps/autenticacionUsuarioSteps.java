package myProyectoDAW.gestionInstituciones.usuariosSteps;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.cucumber.java.en.Then;
import myProyectoDAW.gestionInstituciones.commonSteps.autenticacionStepsComunes;
import myProyectoDAW.gestionInstituciones.security.JwtService;

public class autenticacionUsuarioSteps {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private autenticacionStepsComunes autenticacionStepsComunes;

    @Then("la respuesta debe contener un token JWT valido")
    public void respuesta_contiene_token_jwt_valido() {

        String token = autenticacionStepsComunes.getResponse().jsonPath().getString("token");
        Assert.assertNotNull(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(autenticacionStepsComunes.getLogin());
        Assert.assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}
