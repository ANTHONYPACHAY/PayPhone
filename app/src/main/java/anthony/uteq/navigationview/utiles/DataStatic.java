package anthony.uteq.navigationview.utiles;

public class DataStatic {

    private static String urlDomain = "https://ciclerfff.herokuapp.com";
    //"https://aplicaciones.uteq.edu.ec/" ciclero_server;
    private static String webservices = "/webresources/";

    public static String gerUrlApi(String servicePath){
        //persona/logIn
        return urlDomain + webservices + servicePath;
    }
    //user and session information
    private static SessionUser user;

    public static SessionUser getUser() {
        return user;
    }

    public static void setUser(SessionUser user) {
        DataStatic.user = user;
    }
}
