package myProyectoDAW.gestionInstituciones.usuariosSteps;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Given;
import myProyectoDAW.gestionInstituciones.applications.services.UsuarioService;
import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

public class bajaUsuarioSteps {

    @Autowired
    private UsuarioService usuarioService;

    @Given("un usuario existente con login {string}")
    public void un_usuario_existente_con_login(String login) {

        Usuario usuario = usuarioService.encontrarSiExisteUsuario(login);
        Assert.assertNotNull(usuario);

    }

    @Given("un usuario con login {string} no existente en el sistema")
    public void un_usuario_con_login_no_existente_en_el_sistema(String login) {

        Usuario usuario = usuarioService.encontrarSiExisteUsuario(login);
        Assert.assertNull(usuario);
    }
}
