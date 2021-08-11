package anthony.uteq.navigationview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import anthony.uteq.navigationview.utiles.Alerts;
import anthony.uteq.navigationview.utiles.DataStatic;
import anthony.uteq.navigationview.utiles.Methods;
import anthony.uteq.navigationview.utiles.MyLogs;
import anthony.uteq.navigationview.utiles.SessionUser;
import anthony.uteq.navigationview.utiles.TableModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Context this_ctx;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public home_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static home_fragment newInstance(String param1, String param2) {
        home_fragment fragment = new home_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this_ctx = container.getContext();
        loadHomeInfo();
        return inflater.inflate(R.layout.fragment_home_fragment, container, false);
    }
    private void loadHomeInfo(){
        getDataVolley(String.format("{\"user_token\":\"%s\"}", DataStatic.getUser().getUser_token()));
    }

    private void getDataVolley(String data){
        MyLogs.error(data);
        RequestQueue queue = Volley.newRequestQueue(this_ctx);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                DataStatic.gerUrlApi("general/getHombeInfo"),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyLogs.info("ws todo bien");
                        //Procesar las respuesta y armar un Array con estos
                        JsonObject jso = Methods.stringToJSON(response);

                        if(jso.size() > 0){
                            int status = Methods.JsonToInteger(jso, "status", 4);
                            if(status == 2){
                                JsonArray jarr = Methods.JsonToArray(jso,"data");
                                try {
                                    jso = Methods.JsonElementToJSO(jarr.get(0));
                                    if(jso.size() > 0){
                                        TextView cp= (TextView)getView().findViewById(R.id.counts_persons);
                                        TextView cpo= (TextView)getView().findViewById(R.id.counts_puntos);
                                        TextView cr= (TextView)getView().findViewById(R.id.counts_rutas);

                                        cp.setText(Methods.JsonToString(jso, "nusrs", "0") + " Personas");
                                        cpo.setText(Methods.JsonToString(jso, "npoints", "0") + " Points");
                                        cr.setText(Methods.JsonToString(jso, "nrouts", "0") + " Rutas");

                                        ArrayList<String[]> lista = new ArrayList<>();
                                        JsonArray jarr1 = Methods.JsonToArray(jso, "querydata");
                                        for(int ind = 0; ind < jarr1.size();ind++){
                                            JsonObject rut = Methods.JsonElementToJSO(jarr1.get(ind));
                                            lista.add(new String[]{
                                                    Methods.JsonToString(rut, "nombre_ruta", ""),
                                                    Methods.JsonToString(rut, "pais_ruta", ""),
                                                    Methods.JsonToString(rut, "provincia_ruta", ""),
                                                    Methods.JsonToString(rut, "ciudad_ruta", "")
                                            });
                                        }
                                        MyLogs.error("cantidad: " + lista.size());
                                        tableAdapt(lista);
                                    }

                                }catch (Exception e){
                                    MyLogs.error(e.getMessage());
                                }
                            }else{
                                Alerts.MessageToast(this_ctx,
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
                        Alerts.MessageToast(this_ctx,"Error en Volley");
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

    private void tableAdapt(ArrayList<String[]> lista) {
        //obtener la referencia de la tabla en el activity

        TableLayout table = (TableLayout)getView().findViewById(R.id.table);
        //declaramos el objeto que nos creará la tabla dinámica
        TableModel tbModel = new TableModel(this_ctx, table);
        //indicamos los encabezados de la tabla
        tbModel.setHeaders(new String[]{"Nombre", "País", "Provincia", "Ciudad"});
        //enviamos los datos del cuerpo de la tabla
        tbModel.setRows(lista);
        //configuramos la tabla, colores del encabezado y el cuerpo
        // tanto del texto como el fondo
        tbModel.setHeaderBackGroundColor(R.color.back_black);
        tbModel.setRowsBackGroundColor(R.color.back_white);

        tbModel.setHeadersForeGroundColor(R.color.back_white);
        tbModel.setRowsForeGroundColor(R.color.back_black);
        //Modifica la tabla a partir de los datos enviados y los parámetros enviados
        tbModel.makeTable();

        MyLogs.info(" FIN ");
    }
}