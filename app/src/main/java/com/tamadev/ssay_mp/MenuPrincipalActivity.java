package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import database.SQLiteDB;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.Jugador;
import com.tamadev.ssay_mp.classes.LV_Usuario;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.SolicitudPartida;
import com.tamadev.ssay_mp.classes.UserFriendProfile;
import com.tamadev.ssay_mp.utils.AlertDialogIngreso;
import com.tamadev.ssay_mp.utils.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalActivity extends AppCompatActivity implements AlertDialogIngreso.AlertDialogIngresoCallback {
    private SQLiteDB helper;
    public static DatabaseReference DBrefUsuario;
    private ImageView ivProfilePhoto, ivLvlIcon;
    private ImageButton ibMore;
    private TextView tvNickName, tvLvlTxt;
    private ArrayList<CrearPartida> _dataListPartidas = new ArrayList<>();
    private ArrayList<SolicitudPartida> _dataListSolicitudes= new ArrayList<>();
    private ArrayList<CrearPartida> _listaSolicitudPartidas = new ArrayList<>();
    private RecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_menu_principal);

        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        ivLvlIcon = findViewById(R.id.ivLvl);
        ibMore = findViewById(R.id.ibMore);
        tvNickName = findViewById(R.id.tvProfileNick);
        tvLvlTxt = findViewById(R.id.tvLvl);



        DBrefUsuario = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        LinearLayoutManager layoutManager = new LinearLayoutManager(MenuPrincipalActivity.this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(MenuPrincipalActivity.this,_dataListPartidas);

        recyclerView.setAdapter(adapter);



        if(user != null){

            Perfil.NAME = user.getDisplayName();
            String _sEmail = user.getEmail();
            Perfil.URL_IMAGE_PROFILE = user.getPhotoUrl();
            Perfil.UID = user.getUid();


            Picasso.with(this).load(Perfil.URL_IMAGE_PROFILE).error(R.mipmap.ic_launcher).fit().centerInside().into(ivProfilePhoto);



            DBrefUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        try{
                            Perfil.USER_ID = dataSnapshot.child("IdentificadoresUnicos").child(Perfil.UID).getValue().toString();
                            Perfil.NAME = dataSnapshot.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("nombre").getValue().toString();
                            getPartidasActuales();
                            tvNickName.setText(Perfil.USER_ID);

                        }catch (Exception ex){
                            new AlertDialogIngreso(MenuPrincipalActivity.this,"Ingresa un apodo", "Aceptar","Tu nick",5,16,MenuPrincipalActivity.this);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            goLoginScreen();
        }


    }

    private void goLoginScreen() {
        Intent i = new Intent(this,PantallaInicialActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void CrearPartida(View view){

        Intent i = new Intent(this,CreacionPartida.class);
        startActivity(i);
        finish();
    }

    public void DesplegarDatos(View view){
        helper = new SQLiteDB(this,"db",null,1);
        String _resultPush = helper.PushDB();

        Toast.makeText(this, _resultPush,Toast.LENGTH_LONG).show();
    }
    /*
    public void DescargarDatos(View view){
        helper = new SQLiteDB(this,"db",null,1);
        try {
            helper.GetDB(_sUsuario, false);
            Toast.makeText(this, "Datos descargados con éxito", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(this, "Fallo la descarga", Toast.LENGTH_SHORT).show();
        }
        String pathDB = getDatabasePath("db").toString();
        String to = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + getPackageName() + "/" + "dbBK";
        helper.copiaBD(pathDB,to);
        helper.close();
    }

     */
    private void goAmigos(View view){

        Intent i = new Intent(this, AmigosActivity.class);
        startActivity(i);
        finish();
    }

    public void Jugar(View view){
        Intent i = new Intent(MenuPrincipalActivity.this,CreacionPartida.class);
        startActivity(i);
        finish();
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
                                    jugadores.add(new UserFriendProfile(jugador.child("user").getValue().toString(),jugador.child("urlImageUser").getValue().toString(),false));
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

    private void initRecyclerView(){

    }

    public void BorrarDatos(View vie){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
        /*
        if(MenuPrincipalActivity.this.deleteDatabase("db")){
            Intent i = new Intent(MenuPrincipalActivity.this,PantallaInicialActivity.class);
            startActivity(i);
            finish();
        } else{
            Toast.makeText(this,"Error al cerrar sesión",Toast.LENGTH_SHORT).show();
        }*/
    }
    public void Salir(View view)
    {
        System.exit(0);
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
