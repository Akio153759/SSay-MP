package com.tamadev.ssay_mp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.SolicitudPartida;
import com.tamadev.ssay_mp.classes.UserFriendProfile;
import com.tamadev.ssay_mp.ui.gallery.GalleryFragment;
import com.tamadev.ssay_mp.ui.home.HomeFragment;
import com.tamadev.ssay_mp.ui.share.ShareFragment;
import com.tamadev.ssay_mp.ui.slideshow.SlideshowFragment;
import com.tamadev.ssay_mp.ui.tools.ToolsFragment;
import com.tamadev.ssay_mp.utils.AlertDialogIngreso;
import com.tamadev.ssay_mp.utils.RecyclerViewAdapter;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuPrincipalActivityNavDrawer extends AppCompatActivity implements AlertDialogIngreso.AlertDialogIngresoCallback, NavigationView.OnNavigationItemSelectedListener {

    public static DatabaseReference DBrefUsuario;
    public static ArrayList<CrearPartida> _dataListPartidas;
    public static  RecyclerViewAdapter adapter;

    private NavigationView navigationView;
    private ImageView ivProfilePhoto, ivLvlIcon, ivProfilePhotoBar;
    private ImageButton ibMore;
    private TextView tvNickName,tvNickNameBar , tvLvlTxt, tvProfileName;
    private ArrayList<SolicitudPartida> _dataListSolicitudes= new ArrayList<>();
    private ArrayList<CrearPartida> _listaSolicitudPartidas = new ArrayList<>();

    private DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu_principal_nav_drawer);
        ivProfilePhotoBar = findViewById(R.id.ivProfilePhotoBar);
        tvNickNameBar = findViewById(R.id.tvProfileNickBar);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        LinearLayoutManager layoutManager = new LinearLayoutManager(MenuPrincipalActivityNavDrawer.this,LinearLayoutManager.HORIZONTAL,false);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                break;
            case R.id.nav_gallery:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new GalleryFragment()).commit();
                break;
            case R.id.nav_slideshow:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new SlideshowFragment()).commit();
                break;
            case R.id.nav_tools:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ToolsFragment()).commit();
                break;
            case R.id.nav_share:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ShareFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goLoginScreen() {
        Intent i = new Intent(this,PantallaInicialActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal_activity_nav_drawer, menu);



        ivProfilePhoto = mAppBarConfiguration.getDrawerLayout().findViewById(R.id.ivProfilePhoto);
        tvNickName = mAppBarConfiguration.getDrawerLayout().findViewById(R.id.tvProfileNick);
        tvProfileName = mAppBarConfiguration.getDrawerLayout().findViewById(R.id.tvProfileName);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                        break;
                    case R.id.nav_gallery:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new GalleryFragment()).commit();
                        break;
                    case R.id.nav_slideshow:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new SlideshowFragment()).commit();
                        break;
                    case R.id.nav_tools:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ToolsFragment()).commit();
                        break;
                    case R.id.nav_share:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ShareFragment()).commit();
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(MenuPrincipalActivityNavDrawer.this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(adapter);
        tvProfileName.setText(Perfil.NAME);
        tvNickName.setText(Perfil.USER_ID);
        tvNickNameBar.setText(Perfil.USER_ID);
        Picasso.with(this).load(Perfil.URL_IMAGE_PROFILE).error(R.mipmap.ic_launcher).fit().centerInside().into(ivProfilePhoto);
        Picasso.with(this).load(Perfil.URL_IMAGE_PROFILE).error(R.mipmap.ic_launcher).fit().centerInside().into(ivProfilePhotoBar);











        return true;
    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    private void getPartidasActuales(){

        DBrefUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot NodosPrincipal) {
                _dataListPartidas.clear();
                _dataListSolicitudes.clear();
                for(DataSnapshot nodo: NodosPrincipal.child("Usuarios").child(Perfil.USER_ID).getChildren()){
                    switch (nodo.getKey()){
                        case "SolicitudesPartidas":
                            for(DataSnapshot partida: nodo.getChildren()){
                                ArrayList<UserFriendProfile> jugadores = new ArrayList<>();
                                for(DataSnapshot jugador: nodo.child(partida.getKey()).child("jugadores").getChildren()){
                                    jugadores.add(new UserFriendProfile(jugador.child("user").getValue().toString(),jugador.child("urlImageUser").getValue().toString()));
                                }
                                SolicitudPartida sp = new SolicitudPartida(partida.getKey(),nodo.child(partida.getKey()).child("idPartida").getValue().toString(),jugadores,nodo.child(partida.getKey()).child("anfitrion").getValue().toString());
                                _dataListSolicitudes.add(sp);
                                _listaSolicitudPartidas.add(NodosPrincipal.child("Partidas").child(sp.getIdPartida()).getValue(CrearPartida.class));
                            }

                            break;
                        case "Partidas":

                            for(DataSnapshot partida: nodo.getChildren()){

                                CrearPartida _objCrearPartida = (NodosPrincipal.child("Partidas").child(partida.getValue().toString()).getValue(CrearPartida.class));

                                boolean _bPartidaActiva = false;
                                // Mapeo en el objeto CrearPartida, las url profile image de los juadores de la partida consultando la url que cada jugador tiene en su perfil
                                for(int _iMyIndexPlay = 0; _iMyIndexPlay < _objCrearPartida.getJugadores().size(); _iMyIndexPlay++){

                                    _objCrearPartida.getJugadores().get(_iMyIndexPlay).setUrlImageUser(NodosPrincipal.child("Usuarios").
                                            child(_objCrearPartida.getJugadores().get(_iMyIndexPlay).getUser()).
                                            child("Perfil").
                                            child("urlProfileImage").getValue().toString());
                                    // si yo estoy activo en la partida, activo el flag
                                    if(_objCrearPartida.getJugadores().get(_iMyIndexPlay).getUser().equals(Perfil.USER_ID) && _objCrearPartida.getJugadores().get(_iMyIndexPlay).getEstado() == 1 || _objCrearPartida.getJugadores().get(_iMyIndexPlay).getUser().equals(Perfil.USER_ID) && _objCrearPartida.getJugadores().get(_iMyIndexPlay).getEstado() == 0){
                                        _bPartidaActiva = true;
                                    }
                                }
                                if(_bPartidaActiva)
                                    _dataListPartidas.add(_objCrearPartida);

                            }
                            int _iCounter = 1;
                            int _iIndex1 = 0;
                            int _iIndex2 = 1;

                            CrearPartida _objAuxiliar = null;

                            while (_iCounter <= _dataListPartidas.size()){
                                while (_iIndex2 < _dataListPartidas.size()){

                                    int _iIndex1Priority = 0;
                                    int _iIndex2Priority = 0;

                                    // Me busco en la lista de jugadores de la partida que apunta el indice 2
                                    for(int i = 0; i < _dataListPartidas.get(_iIndex2).getJugadores().size(); i++){

                                        if (_dataListPartidas.get(_iIndex2).getJugadores().get(i).getUser().equals(Perfil.USER_ID)){

                                            // una vez encontrado establesco el nivel de prioridad de ese indice con la info del estado y el proximo turno
                                            int _iEstado = _dataListPartidas.get(_iIndex2).getJugadores().get(i).getEstado();
                                            String _sProxJug = _dataListPartidas.get(_iIndex2).getProximoJugador();


                                            if(_iEstado == 0 && _sProxJug.equals(Perfil.USER_ID)){
                                                _iIndex2Priority = 1;
                                            }
                                            else if(_iEstado == 0 && !_sProxJug.equals(Perfil.USER_ID)){
                                                _iIndex2Priority = 3;
                                            }
                                            else if(_iEstado == 1 && _sProxJug.equals(Perfil.USER_ID)){
                                                _iIndex2Priority = 2;
                                            }
                                            else {
                                                _iIndex2Priority = 4;
                                            }

                                            break;
                                        }
                                    }

                                    for(int i = 0; i < _dataListPartidas.get(_iIndex1).getJugadores().size(); i++){

                                        if (_dataListPartidas.get(_iIndex1).getJugadores().get(i).getUser().equals(Perfil.USER_ID)){

                                            int _iEstado = _dataListPartidas.get(_iIndex1).getJugadores().get(i).getEstado();
                                            String _sProxJug = _dataListPartidas.get(_iIndex1).getProximoJugador();


                                            if(_iEstado == 0 && _sProxJug.equals(Perfil.USER_ID)){
                                                _iIndex1Priority = 1;
                                            }
                                            else if(_iEstado == 0 && !_sProxJug.equals(Perfil.USER_ID)){
                                                _iIndex1Priority = 3;
                                            }
                                            else if(_iEstado == 1 && _sProxJug.equals(Perfil.USER_ID)){
                                                _iIndex1Priority = 2;
                                            }
                                            else {
                                                _iIndex1Priority = 4;
                                            }

                                            break;
                                        }
                                    }

                                    if(_iIndex2Priority < _iIndex1Priority){
                                        _objAuxiliar = _dataListPartidas.get(_iIndex1);
                                        _dataListPartidas.set(_iIndex1,_dataListPartidas.get(_iIndex2));
                                        _dataListPartidas.set(_iIndex2,_objAuxiliar);

                                    }

                                    _iIndex1++;
                                    _iIndex2++;
                                }

                                _iCounter++;
                                _iIndex1 = 0;
                                _iIndex2 = 1;
                            }

                            break;
                    }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Jugar(View view){
        Intent i = new Intent(MenuPrincipalActivityNavDrawer.this,CreacionPartida.class);
        startActivity(i);
        finish();
    }

    @Override
    public void ResultCallback(int result) {
        if(result == 1){
            DBrefUsuario.child("IdentificadoresUnicos").child(Perfil.UID).setValue(Perfil.USER_ID);
            DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("nombre").setValue(Perfil.NAME);
            DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("usuario").setValue(Perfil.USER_ID);
            DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("urlProfileImage").setValue(Perfil.URL_IMAGE_PROFILE.toString());
            DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("uid").setValue(Perfil.UID);

        }
    }


}
