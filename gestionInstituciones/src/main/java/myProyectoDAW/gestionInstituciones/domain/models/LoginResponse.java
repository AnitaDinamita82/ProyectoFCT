package myProyectoDAW.gestionInstituciones.domain.models;

public class LoginResponse {

    private String token;
    private long expiresIn;

    public LoginResponse() {
    }
 
    public LoginResponse(String token, long expiresIn) {
        this.expiresIn = expiresIn;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LoginResponse{");
        sb.append("token=").append(token);
        sb.append(", expiresIn=").append(expiresIn);
        sb.append('}');
        return sb.toString();
    }
}
