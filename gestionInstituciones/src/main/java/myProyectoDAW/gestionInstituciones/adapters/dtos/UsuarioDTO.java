package myProyectoDAW.gestionInstituciones.adapters.dtos;

import java.sql.Date;

import myProyectoDAW.gestionInstituciones.domain.models.Usuario;

public class UsuarioDTO implements java.io.Serializable {

    private Integer id;
    private String dni;
    private String login;
    private String password;
    private String rol; // Ejemplo: "USER,ADMIN"
    private Date createdAt;
    private Date updatedAt;


    /*Constructores */
    public UsuarioDTO() {
    }

    public UsuarioDTO(Integer id, String dni, String login, String password, String rol, Date createdAt,
            Date updatedAt) {
        this.id = id;
        this.dni = dni;
        this.login = login;
        this.password = password;
        this.rol = rol;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /*Getter and Setter */

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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UsuariosDTO{");
        sb.append("id=").append(id);
        sb.append(", dni=").append(dni);
        sb.append(", login=").append(login);
        sb.append(", password=").append(password);
        sb.append(", rol=").append(rol);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append('}');
        return sb.toString();
    }

    /* METODOS DE CONVERSION */

    //DTO to Entity
    public Usuario convertirDTOAEntity(){

        Usuario usuario = new Usuario();

        usuario.setId(this.getId());
        usuario.setDni(this.getDni());
        usuario.setLogin(this.getLogin());
        usuario.setPassword(this.getPassword());
        usuario.setRol(this.getRol());
        usuario.setCreatedAt(this.getCreatedAt());
        usuario.setUpdatedAt(this.getUpdatedAt());
        return usuario;
    }   

   //DTO to Entity
    public static UsuarioDTO convertirEntityADTO(Usuario usuario){
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getDni(),
            usuario.getLogin(),
            usuario.getPassword(),
            usuario.getRol(),
            usuario.getCreatedAt(),
            usuario.getUpdatedAt()
        );
    }
}
