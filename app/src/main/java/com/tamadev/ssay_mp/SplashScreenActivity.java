package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
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
import com.tamadev.ssay_mp.classes.RequestFriend;
import com.tamadev.ssay_mp.classes.SolicitudPartida;
import com.tamadev.ssay_mp.classes.UserFriendProfile;
import com.tamadev.ssay_mp.classes.UsuarioEnFirebase;
import com.tamadev.ssay_mp.utils.AlertDialogIngreso;
import com.tamadev.ssay_mp.utils.AlertDialogNewFriend;
import com.tamadev.ssay_mp.utils.AlertDialogNewRequestFriend;
import com.tamadev.ssay_mp.utils.RVAdapterRequestFriend;
import com.tamadev.ssay_mp.utils.RecyclerViewAdapter;
import com.tamadev.ssay_mp.utils.tamatools;

import java.util.ArrayList;

import database.SQLiteDB;

public class SplashScreenActivity extends AppCompatActivity implements AlertDialogIngreso.AlertDialogIngresoCallback {

    private boolean _bDBExistente;
    private int _iProgressCounter = 1;
    private TextView tvPorcentaje;
    //private ProgressBar pbCarga;
    private boolean _bPBActive = true;
    private boolean _bGetInicial = true;
    private LottieAnimationView animationView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();


        animationView = findViewById(R.id.animation_view);
        //pbCarga = findViewById(R.id.pbCarga);
        tvPorcentaje = findViewById(R.id.tvPorcentaje);

        animationView.loop(true);
        animationView.playAnimation();

        AmigosActivity.DBrefUsuarioFriends = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        AmigosActivity._dataListAllUsers = new ArrayList<>();
        AmigosActivity._dataListAllUsersFilter = new ArrayList<>();


        InicioActivity.DBrefUsuario = FirebaseDatabase.getInstance().getReference();
        InicioActivity._dataListPartidas = new ArrayList<>();
        InicioActivity._dataListPartidasInactivas = new ArrayList<>();
        InicioActivity.adapter = new RecyclerViewAdapter(SplashScreenActivity.this,InicioActivity._dataListPartidas);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        _iProgressCounter = 20;
        //pbCarga.setProgress(_iProgressCounter);
        tvPorcentaje.setText(_iProgressCounter+"%");

        if(user != null) {

            Perfil.NAME = user.getDisplayName();
            Perfil.URL_IMAGE_PROFILE = user.getPhotoUrl();
            Perfil.UID = user.getUid();

            _iProgressCounter = 35;
            //pbCarga.setProgress(_iProgressCounter);
            tvPorcentaje.setText(_iProgressCounter+"%");


            InicioActivity.DBrefUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        try{
                            _iProgressCounter = 60;
                            //pbCarga.setProgress(_iProgressCounter);
                            tvPorcentaje.setText(_iProgressCounter+"%");
                            Perfil.USER_ID = dataSnapshot.child("IdentificadoresUnicos").child(Perfil.UID).getValue().toString();
                            Perfil.NAME = dataSnapshot.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("nombre").getValue().toString();


                            getAllUsers();
                            getPartidasActuales();




                        }catch (Exception ex){
                            _iProgressCounter = 70;
                            //pbCarga.setProgress(_iProgressCounter);
                            tvPorcentaje.setText(_iProgressCounter+"%");
                            new AlertDialogIngreso(SplashScreenActivity.this,"Ingresa un apodo", "Aceptar","Tu nick",5,16,SplashScreenActivity.this);

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

        if(!_bPBActive){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(_iProgressCounter<=100){


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvPorcentaje.setText(_iProgressCounter+"%");
                               // pbCarga.setProgress(_iProgressCounter);
                            }
                        });


                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(_iProgressCounter==100){
                            Intent i = new Intent(SplashScreenActivity.this,InicioActivity.class);
                            startActivity(i);
                            finish();
                        }
                        _iProgressCounter++;
                        _bPBActive = true;
                    }
                }
            });
            thread.start();
        }



    }

    private void goLoginScreen() {
        Intent i = new Intent(this,PantallaInicialActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onBackPressed(){

    }


    @Override
    public void ResultCallback(int result) {
        if(result == 1){
            InicioActivity.DBrefUsuario.child("IdentificadoresUnicos").child(Perfil.UID).setValue(Perfil.USER_ID);
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("nombre").setValue(Perfil.NAME);
            _iProgressCounter = 80;
           // pbCarga.setProgress(_iProgressCounter);
            tvPorcentaje.setText(_iProgressCounter+"%");
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("usuario").setValue(Perfil.USER_ID);
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("urlProfileImage").setValue(Perfil.URL_IMAGE_PROFILE.toString());
            _iProgressCounter = 90;
           // pbCarga.setProgress(_iProgressCounter);
            tvPorcentaje.setText(_iProgressCounter+"%");

            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("uid").setValue(Perfil.UID);
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("partidasPrimerPuesto").setValue(Perfil.FIRST_PLACE_GAMES);
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("partidasSegundoPuesto").setValue(Perfil.SECOND_PLACE_GAMES);
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("partidasTercerPuesto").setValue(Perfil.THIRD_PLACE_GAMES);
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("partidasCuartoPuesto").setValue(Perfil.QUARTER_PLACE_GAMES);
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("partidasJugadas").setValue(Perfil.MATCHES_PLAYED);
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("maxScore").setValue(Perfil.MAX_SCORE);

            getAllUsers();
            _iProgressCounter = 100;
           // pbCarga.setProgress(_iProgressCounter);
            tvPorcentaje.setText(_iProgressCounter+"%");
            if(_iProgressCounter==100){
                animationView.pauseAnimation();
                Intent i = new Intent(SplashScreenActivity.this,InicioActivity.class);
                startActivity(i);
                finish();
            }
        }
    }


    private void getRequestsFriend(){
        InicioActivity.DBrefFriendRequest.child(Perfil.USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot NodoPrinicpalUser) {
                InicioActivity._dataListNewRequestFriend.clear();
                for (DataSnapshot nodo: NodoPrinicpalUser.getChildren()){
                    switch (nodo.getKey()){
                        case "SolicitudesRecibidas":
                            for(DataSnapshot solicitudes: nodo.getChildren()){
                                //Por cada solicitud de amistad que tengo, recorro la lista completa de usuarios para usar esos datos y mapear el obj Request Friend(sobretodo la url)
                                for(UserFriendProfile ufp: AmigosActivity._dataListAllUsers){
                                    if(ufp.getUserID().equals(solicitudes.getValue().toString())){
                                        RequestFriend _objRequestFriend = new RequestFriend(solicitudes.getKey(),solicitudes.getValue().toString(),ufp.getUrlImageProfile());
                                        InicioActivity._dataListNewRequestFriend.add(_objRequestFriend);
                                        break;
                                    }
                                }
                            }
                            break;
                    }
                }
                AlertDialogNewRequestFriend.adapterRequestFriend.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPartidasActuales(){

        InicioActivity.DBrefUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot NodosPrincipal) {
                InicioActivity._dataListPartidas.clear();
                InicioActivity._dataListPartidasInactivas.clear();
                _iProgressCounter = 70;
               // pbCarga.setProgress(_iProgressCounter);
                tvPorcentaje.setText(_iProgressCounter+"%");
                for(DataSnapshot nodo: NodosPrincipal.child("Usuarios").child(Perfil.USER_ID).getChildren()){
                    switch (nodo.getKey()){
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
                                if(_bPartidaActiva) {
                                    InicioActivity._dataListPartidas.add(_objCrearPartida);
                                }
                                else {
                                    InicioActivity._dataListPartidasInactivas.add(_objCrearPartida);
                                }

                            }
                            int _iCounter = 1;
                            int _iIndex1 = 0;
                            int _iIndex2 = 1;

                            CrearPartida _objAuxiliar = null;

                            _iProgressCounter = 90;
                            //pbCarga.setProgress(_iProgressCounter);
                            tvPorcentaje.setText(_iProgressCounter+"%");

                            while (_iCounter <= InicioActivity._dataListPartidas.size()){
                                while (_iIndex2 < InicioActivity._dataListPartidas.size()){

                                    int _iIndex1Priority = 0;
                                    int _iIndex2Priority = 0;

                                    // Me busco en la lista de jugadores de la partida que apunta el indice 2
                                    for(int i = 0; i < InicioActivity._dataListPartidas.get(_iIndex2).getJugadores().size(); i++){

                                        if (InicioActivity._dataListPartidas.get(_iIndex2).getJugadores().get(i).getUser().equals(Perfil.USER_ID)){

                                            // una vez encontrado establesco el nivel de prioridad de ese indice con la info del estado y el proximo turno
                                            int _iEstado = InicioActivity._dataListPartidas.get(_iIndex2).getJugadores().get(i).getEstado();
                                            String _sProxJug = InicioActivity._dataListPartidas.get(_iIndex2).getProximoJugador();


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

                                    for(int i = 0; i < InicioActivity._dataListPartidas.get(_iIndex1).getJugadores().size(); i++){

                                        if (InicioActivity._dataListPartidas.get(_iIndex1).getJugadores().get(i).getUser().equals(Perfil.USER_ID)){

                                            int _iEstado = InicioActivity._dataListPartidas.get(_iIndex1).getJugadores().get(i).getEstado();
                                            String _sProxJug = InicioActivity._dataListPartidas.get(_iIndex1).getProximoJugador();


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
                                        _objAuxiliar = InicioActivity._dataListPartidas.get(_iIndex1);
                                        InicioActivity._dataListPartidas.set(_iIndex1,InicioActivity._dataListPartidas.get(_iIndex2));
                                        InicioActivity._dataListPartidas.set(_iIndex2,_objAuxiliar);

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
                _iProgressCounter = 100;
               // pbCarga.setProgress(_iProgressCounter);
                tvPorcentaje.setText(_iProgressCounter+"%");


                InicioActivity.adapter.notifyDataSetChanged();
                if(_bGetInicial){
                    if(_iProgressCounter==100){
                        _bGetInicial = false;
                        animationView.pauseAnimation();
                        Intent i = new Intent(SplashScreenActivity.this,InicioActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getAllUsers(){
        AmigosActivity.DBrefUsuarioFriends.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot NodoUsers) {
                for(DataSnapshot User: NodoUsers.getChildren()){
                    if(User.getKey().equals(Perfil.USER_ID)){
                        continue;
                    }
                    UserFriendProfile _objUser = new UserFriendProfile(User.child("Perfil").child("usuario").getValue().toString(),
                                                                       User.child("Perfil").child("urlProfileImage").getValue().toString(),
                                                                        Boolean.parseBoolean(User.child("Perfil").child("enLinea").getValue().toString()));
                    AmigosActivity._dataListAllUsers.add(_objUser);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
