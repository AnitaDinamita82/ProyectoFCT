package myProyectoDAW.gestionInstituciones.domain.models;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario implements UserDetails{

    private Integer id;
    private String dni;
    private String login;
    private String password;
    private String rol; // Ejemplo: "USER,ADMIN"
    private Date createdAt;
    private Date updatedAt;

    /*Constructor */

    public Usuario() {
    }

    public Usuario(Integer id, String dni,String login, String password, String rol,  Date createdAt, Date updatedAt) {
        this.id = id;
        this.dni = dni;
        this.login = login;
        this.password = password;
        this.rol = rol;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    
     /*GETTER AND SETTER */
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return login;
    }

      // Usamos @JsonProperty para mapear username a login
    @JsonProperty("username")
    public void setUsername(String username) {
        this.login = username;
    }
}
