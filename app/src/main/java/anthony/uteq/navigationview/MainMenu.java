package anthony.uteq.navigationview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import anthony.uteq.navigationview.utiles.Alerts;
import anthony.uteq.navigationview.utiles.DataStatic;
import anthony.uteq.navigationview.utiles.MyLogs;

public class MainMenu extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //oculta tool bar  en este activity
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_menu);
        //obtiene el botón en la interfaz principal
        drawerLayout = findViewById(R.id.drawer_layout);
        //este botón será quien va a abrir el menú de la aplicación
        findViewById(R.id.imageOpenMenu).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //se le asigna la acción de abrir el menú
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }
        );
        // si por alguna razón no hay datos de usuario, regresar a la interfaz de login
        if (DataStatic.getUser() == null) {
            //onBackPressed();
            Intent intent = new Intent(MainMenu.this, MainActivity.class);
            startActivity(intent);
        }
        //obtiene el navigation view en donde estará ubicado elementos como
        NavigationView navigationView = findViewById(R.id.id_opctions_menu);
        navigationView.setItemIconTintList(null);

        //establece el frame utilizado para la presentación de las ventanas
        NavController navController = Navigation.findNavController(MainMenu.this, R.id.content_frame);
        //asigna la vista de navegación al controlador
        NavigationUI.setupWithNavController(navigationView, navController);

        //obtenemos el encabezado del menú
        View headMenu = navigationView.getHeaderView(0);
        //modifica el encabezado
        changeHeaderView(headMenu);
        Menu bodyMenu = navigationView.getMenu();

        MyLogs.info("items: " + bodyMenu.size());


        configureMenu(bodyMenu);

        dinamicMenu(bodyMenu);

        loadFragment(new home_fragment());
    }

    private void changeHeaderView(View headMenu) {
        //se modifica el label, estableciendo el nombre del usuario logeado.

        String userName = (DataStatic.getUser().getNombre_persona().trim()
                + " " + DataStatic.getUser().getApellido_persona().trim()).toUpperCase();
        userName = userName.length() > 15 ? userName.substring(0, 13) + "..." : userName;

        ((TextView) headMenu.findViewById(R.id.profile_username)).setText(userName);
        //de igual manera, se modifica la imagen por la foto del usuario
        ImageView userImage = headMenu.findViewById(R.id.profile_image);
        Glide.with(headMenu)
                .load(DataStatic.getUser().getImagen_persona())
                .error(R.drawable.user)
                .into(userImage);
    }

    private void configureMenu(Menu bodyMenu) {
//        String rol = DataStatic.getUser().getRol_persona();
//        if(rol.equals("R")) {
//            bodyMenu.findItem(R.id.navigation_root).setVisible(true);
//        }else{
//            bodyMenu.findItem(R.id.navigation_root).setVisible(false);
//        }
//        if(rol.matches("[RA]")){
//            bodyMenu.findItem(R.id.navigation_admin).setVisible(true);
//        }else{
//            bodyMenu.findItem(R.id.navigation_admin).setVisible(false);
//        }
//        if(rol.matches("[RAU]")){
//            bodyMenu.findItem(R.id.navigation_user).setVisible(true);
//        }else{
//            bodyMenu.findItem(R.id.navigation_user).setVisible(false);
//        }
    }

    private void dinamicMenu(Menu menu) {
        //obtiene el permiso del usuario, para asi determinar que opciones se van a generar y cuales no
        String rol = DataStatic.getUser().getRol_persona();

        List<MenuItem> menus = new ArrayList<MenuItem>();
        //genera cada uno de los items para el menú
        MenuItem portal = menu.add("Portal");
        portal.setIcon(R.drawable.ic_baseline_home_24);
        portal.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Alerts.MessageToast(MainMenu.this, "clic en portal");
                loadFragment(new home_fragment());

                return true;
            }
        });

        MenuItem profile = menu.add("Perfil de usuario");
        profile.setIcon(R.drawable.ic_baseline_person_24);
        profile.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Alerts.MessageToast(MainMenu.this, "Mi perfil");
                loadFragment(new profile_fragment());
                return true;
            }
        });

        MenuItem tmp;
        if (rol.equals("R")) {
            SubMenu navigation_root = menu.addSubMenu("Administración");
            tmp = navigation_root.add("Gestionar Usuarios").setIcon(R.drawable.ic_baseline_supervised_user_circle_24);
            menus.add(tmp);
        }
        if (rol.matches("[RA]")) {
            SubMenu navigation_admin = menu.addSubMenu("Gestión");
            tmp = navigation_admin.add("Gestionar Rutas").setIcon(R.drawable.icon_route);
            menus.add(tmp);
            tmp = navigation_admin.add("Gestionar Puntos").setIcon(R.drawable.icon_location);
            menus.add(tmp);
        }
        if (rol.matches("[RAU]")) {
            SubMenu navigation_user = menu.addSubMenu("Opciones Generales");
            tmp = navigation_user.add("Visualizar Rutas").setIcon(R.drawable.icon_route);
            menus.add(tmp);
            tmp = navigation_user.add("Ver Puntos").setIcon(R.drawable.icon_location);
            menus.add(tmp);
        }

        SubMenu navigation_others = menu.addSubMenu("Otros");
        tmp = navigation_others.add("Configuración");
        tmp.setIcon(R.drawable.ic_baseline_settings_24);
        menus.add(tmp);

        tmp = navigation_others.add("Donación");
        tmp.setIcon(R.drawable.ic_baseline_credit_card_24);
        tmp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                loadFragment(new Donation());
                return false;
            }
        });

        tmp = navigation_others.add("Acerca de");
        tmp.setIcon(R.drawable.ic_baseline_info_24);
        menus.add(tmp);
        tmp = navigation_others.add("Cerrar Sesión");
        tmp.setIcon(R.drawable.icon_exit);
        tmp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Alerts.MessageToast(MainMenu.this, "cerrar sesión");
                DataStatic.setUser(null);
                onBackPressed();
                return false;
            }
        });

        for (MenuItem temp : menus) {
            temp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    loadFragment(new nodisponible_fragment());
                    return true;
                }
            });
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }


}