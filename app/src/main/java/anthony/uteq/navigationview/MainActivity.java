package anthony.uteq.navigationview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;


import java.io.UnsupportedEncodingException;

import anthony.uteq.navigationview.utiles.Alerts;
import anthony.uteq.navigationview.utiles.DataStatic;
import anthony.uteq.navigationview.utiles.Methods;
import anthony.uteq.navigationview.utiles.MyLogs;
import anthony.uteq.navigationview.utiles.SessionUser;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //establecer etiqueta de los Logs
        MyLogs.setLabel("MyLogs");
    }

    public void login(View view){
        MyLogs.info("Estoy aqui");
        //obtiene los daots (usuario y clave) para logearse
        String data = String.format("{\"email\":\"%s\", \"contrasenia\":\"%s\"}",
                ((EditText) findViewById(R.id.txtusuario)).getText().toString(),
                ((EditText) findViewById(R.id.txtclave)).getText().toString());
        MyLogs.info("login para:" + data);
        getDataVolley(data);
    }

    private void getDataVolley(String data){
        Alerts.LoadingDialog(MainActivity.this);
        Alerts.showLoading();
        //Obtención de datos del web service utilzando Volley
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                DataStatic.gerUrlApi("persona/logIn"),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyLogs.info("ws todo bien");
                        //Procesar las respuesta y armar un Array con estos
                        MyLogs.detailedLog(response);
                        JsonObject jso = Methods.stringToJSON(response);

                        if(jso.size() > 0){
                            int status = Methods.JsonToInteger(jso, "status", 4);
                            if(status == 2){
                                JsonArray jarr = Methods.JsonToArray(jso,"data");
                                try {
                                    jso = Methods.JsonElementToJSO(jarr.get(0));
                                    Gson gson = new Gson();
                                    SessionUser ses =gson.fromJson(jso.toString(), SessionUser.class);
                                    DataStatic.setUser(ses);

                                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                                    startActivity(intent);
                                }catch (Exception e){
                                    MyLogs.error(e.getMessage());
                                }
                            }else{
                                Alerts.MessageToast(MainActivity.this,
                                        Methods.JsonToString(jso, "information", "Unknown error"));
                            }
                        }

                        Alerts.closeLoading();

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyLogs.error("we error");
                        Alerts.MessageToast(MainActivity.this,"Error en Volley");
                        Alerts.closeLoading();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                //Establece la codificación para los datos del web service
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                //los datos que se envian deben ir bajo este formato
                try {
                    return data == null ? "{}".getBytes("utf-8") : data.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    MyLogs.error("Error al momento de codificar la solicitud");
                    Alerts.closeLoading();
                    return null;
                }
            }
        };
        queue.add(request);
    }
}