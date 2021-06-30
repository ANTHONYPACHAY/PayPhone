package anthony.uteq.navigationview.utiles;

public class SessionUser {
    //user session class
    String nombre_persona = "";
    String apellido_persona = "";
    String email_persona = "";
    String rol_persona = "";
    String imagen_persona = "";
    String user_token = "";

    public String getNombre_persona() {
        return nombre_persona;
    }

    public void setNombre_persona(String nombre_persona) {
        this.nombre_persona = nombre_persona;
    }

    public String getApellido_persona() {
        return apellido_persona;
    }

    public void setApellido_persona(String apellido_persona) {
        this.apellido_persona = apellido_persona;
    }

    public String getEmail_persona() {
        return email_persona;
    }

    public void setEmail_persona(String email_persona) {
        this.email_persona = email_persona;
    }

    public String getRol_persona() {
        return rol_persona;
    }

    public void setRol_persona(String rol_persona) {
        this.rol_persona = rol_persona;
    }

    public String getImagen_persona() {
        return imagen_persona;
    }

    public void setImagen_persona(String imagen_persona) {
        this.imagen_persona = imagen_persona;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }
}
