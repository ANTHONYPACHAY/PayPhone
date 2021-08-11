package anthony.uteq.navigationview;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import anthony.uteq.navigationview.utiles.Alerts;
import anthony.uteq.navigationview.utiles.DataStatic;
import anthony.uteq.navigationview.utiles.Methods;
import anthony.uteq.navigationview.utiles.MyLogs;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PayPhone#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayPhone extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Random rnd;

    //variables globales para los controles
    EditText txtcantidaddonar;
    EditText txtidentificacion;
    EditText editTextcodepais;
    EditText txtnumtelefonico;
    EditText txtadjunto;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PayPhone() {
        // Required empty public constructor
        rnd = new Random(System.currentTimeMillis());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PayPhone.
     */
    // TODO: Rename and change types and number of parameters
    public static PayPhone newInstance(String param1, String param2) {
        PayPhone fragment = new PayPhone();
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

        return inflater.inflate(R.layout.fragment_pay_phone, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        declararReferenciaControles();
    }

    private void declararReferenciaControles() {
        //obtener referencias de botones
        txtcantidaddonar = (EditText) getView().findViewById(R.id.txtcantidaddonar);
        txtidentificacion = (EditText) getView().findViewById(R.id.txtidentificacion);
        editTextcodepais = (EditText) getView().findViewById(R.id.editTextcodepais);
        txtnumtelefonico = (EditText) getView().findViewById(R.id.txtnumtelefonico);
        txtadjunto = (EditText) getView().findViewById(R.id.txtadjunto);

        Button buton = (Button) getView().findViewById(R.id.btndonar);

        buton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //se le asigna la acción de abrir el menú
                        Alerts.MessageToast(getContext(), "depositar");
                        RealizarDonacion();

                    }
                }
        );
        Alerts.LoadingDialog(getContext());
    }

    private String getCharacter() {
        int aleatorio = rnd.nextInt(90 - 65 + 1) + 65;
        return Character.toString((char) aleatorio);
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    public void RealizarDonacion() {

        //valida el monto
        int monto;
        try {
            Double pago = Double.valueOf(txtcantidaddonar.getText().toString());
            //se multiplica por 100 por problemas de la api, no reconociendo los centavos
            pago = pago * 100;
            //convertimos a string para luego pasarlo a entero
            monto = Integer.parseInt(String.valueOf(pago));
        } catch (Exception e) {
            monto = 110;
            MyLogs.info(e.getMessage());
        }
        MyLogs.info("Deposito: " + monto);
        int tax = (int) (monto * 0.06);
        MyLogs.info("Deposito: " + tax);
        //genera un json con los datos necesarios para realizar el consumo de la api.
        if (txtnumtelefonico.getText().toString().trim().length() > 0 &&
                editTextcodepais.getText().toString().trim().length() > 0 &&
                txtidentificacion.getText().toString().trim().length() > 0 &&
                txtadjunto.getText().toString().trim().length() > 0) {

            JsonObject jsonDonacion = new JsonObject();

            jsonDonacion.addProperty("phoneNumber", txtnumtelefonico.getText().toString());
            jsonDonacion.addProperty("countryCode", editTextcodepais.getText().toString());
            jsonDonacion.addProperty("clientUserId", txtidentificacion.getText().toString());
            jsonDonacion.addProperty("reference", txtadjunto.getText().toString());
            jsonDonacion.addProperty("responseUrl", "http://paystoreCZ.com/confirm.php");
            jsonDonacion.addProperty("amount", monto);
            jsonDonacion.addProperty("amountWithTax", monto - tax);
            jsonDonacion.addProperty("amountWithoutTax", 0);
            jsonDonacion.addProperty("tax", tax);
            jsonDonacion.addProperty("clientTransactionId", String.format("%s%s%s%s", getCharacter(), getCharacter(), getCharacter(), System.currentTimeMillis()));

            MyLogs.info(jsonDonacion.toString());
            realizarDonacion(jsonDonacion.toString());
        } else {
            Alerts.MessageToast(getContext(), "Asegurese de llenar los campos");
        }

    }

    private void realizarDonacion(String data) {
        MyLogs.error(data);
        Alerts.showLoading();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.POST,
                "https://pay.payphonetodoesposible.com/api/Sale",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyLogs.info("ws todo bien");
                        MyLogs.info(response);

                        Alerts.closeLoading();
                        loadFragment(new Donation());
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyLogs.error("we error:" + error.getMessage());
                        Alerts.MessageToast(getContext(), "Error al realizar la transacción");
                        Alerts.closeLoading();
                    }
                }
        ) {
            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("dataType", "json");
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer < ME API KEY>");
                return headers;
            }

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