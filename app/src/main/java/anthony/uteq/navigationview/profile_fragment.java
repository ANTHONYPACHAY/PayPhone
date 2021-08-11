package anthony.uteq.navigationview;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import anthony.uteq.navigationview.utiles.DataStatic;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_fragment newInstance(String param1, String param2) {
        profile_fragment fragment = new profile_fragment();
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

        //loadUserData();

        return inflater.inflate(R.layout.fragment_profile_fragment, container, false);
    }

    private void loadUserData(){
        TextView profile_name = (TextView) getView().findViewById(R.id.profile_name);
        TextView profile_lastname = (TextView) getView().findViewById(R.id.profile_lastname);
        TextView profile_email = (TextView) getView().findViewById(R.id.profile_email);
        TextView profile_rol = (TextView) getView().findViewById(R.id.profile_rol);

        ImageView profile_image = (ImageView) getView().findViewById(R.id.profile_image);

        Glide.with(getContext())
                .load(DataStatic.getUser().getImagen_persona())
                .error(R.drawable.user)
                .into(profile_image);

        profile_name.setText(DataStatic.getUser().getNombre_persona().trim().toUpperCase());
        profile_lastname.setText(DataStatic.getUser().getApellido_persona().trim().toUpperCase());
        profile_email.setText(DataStatic.getUser().getEmail_persona().trim());
        String rol = DataStatic.getUser().getRol_persona();
        if(rol.equals("U")){
            profile_rol.setText("Usuario");
            profile_rol.setBackground(ContextCompat.getDrawable(getContext(), R.color.btn_info));
        } else if(rol.equals("A")){
            profile_rol.setText("Moderados");
            profile_rol.setBackground(ContextCompat.getDrawable(getContext(), R.color.btn_warning));
        } else if(rol.equals("R")){
            profile_rol.setText("Administrador");
            profile_rol.setBackground(ContextCompat.getDrawable(getContext(), R.color.btn_danger));
        } else{
            profile_rol.setText("Unknow");
            profile_rol.setBackground(ContextCompat.getDrawable(getContext(), R.color.btn_secondary));
        }

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUserData();
    }
}