package myProyectoDAW.gestionInstituciones.adapters.controllers;

/**
 * Clase de utilidad para representar una respuesta de error estándar en las
 * APIs REST.
 */
public class ErrorResponse {

    private String mensaje;

    public ErrorResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
