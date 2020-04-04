package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
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
import com.tamadev.ssay_mp.utils.RVAdapterFriends;

import java.util.ArrayList;

import database.SQLiteDB;

public class CreacionPartida extends AppCompatActivity {

    private CheckBox cbAmigo1, cbAmigo2, cbAmigo3, cbAmigo4;
    private SQLiteDB helper = new SQLiteDB(this,"db",null,1);
    private DatabaseReference DBrefPartidas;

    private ArrayList<UserFriendProfile> _dataListAmigosAdd = new ArrayList<>();
    private ArrayList<UserFriendProfile> _dataListAmigosAddFilter = new ArrayList<>();
    private RVAdapterFriends _adapterAmigosAdd;
    private ListView _lvParticipantes;
    private RecyclerView _lvAmigos;
    private DatabaseReference DBrefUsuario;
    private TextView lblAnfitrion;
    private EditText inputSearchFriend;

    public static ArrayList<UserFriendProfile> LISTA_JUGADORES_SALA = new ArrayList<>();
    public static ImageView IV_ANFITRION_SALA;
    public static ImageView IV_P2_SALA;
    public static ImageView IV_P3_SALA;
    public static ImageView IV_P4_SALA;
    public static TextView LBL_ANFITRION_SALA;
    public static TextView LBL_P2_SALA;
    public static TextView LBL_P3_SALA;
    public static TextView LBL_P4_SALA;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_creacion_partida);



        IV_ANFITRION_SALA = findViewById(R.id.ivAnfitrion);
        IV_P2_SALA = findViewById(R.id.ivPlayer2);
        IV_P3_SALA = findViewById(R.id.ivPlayer3);
        IV_P4_SALA = findViewById(R.id.ivPlayer4);
        LBL_ANFITRION_SALA = findViewById(R.id.tvAnfitrion);
        LBL_P2_SALA = findViewById(R.id.tvPlayer2);
        LBL_P3_SALA = findViewById(R.id.tvPlayer3);
        LBL_P4_SALA = findViewById(R.id.tvPlayer4);
        lblAnfitrion = findViewById(R.id.lblAnfitrion);
        inputSearchFriend = findViewById(R.id.etSearchFriend);

        LinearLayoutManager layoutManager = new LinearLayoutManager(CreacionPartida.this,LinearLayoutManager.HORIZONTAL,false);
        _lvAmigos = (RecyclerView)findViewById(R.id.rv_friends_add);
        _lvAmigos.setLayoutManager(layoutManager);
        _adapterAmigosAdd = new RVAdapterFriends(_dataListAmigosAddFilter,CreacionPartida.this);

        _lvAmigos.setAdapter(_adapterAmigosAdd);

        Picasso.with(CreacionPartida.this).load(Perfil.URL_IMAGE_PROFILE).error(R.mipmap.ic_launcher).fit().centerInside().into(IV_ANFITRION_SALA);
        LBL_ANFITRION_SALA.setText(Perfil.USER_ID);
        LISTA_JUGADORES_SALA.add(new UserFriendProfile(Perfil.USER_ID,Perfil.URL_IMAGE_PROFILE.toString()));
        lblAnfitrion.setText("Partida de " + Perfil.USER_ID);



        IV_P2_SALA.setEnabled(false);
        IV_P3_SALA.setEnabled(false);
        IV_P4_SALA.setEnabled(false);

        LBL_P2_SALA.setEnabled(false);
        LBL_P3_SALA.setEnabled(false);
        LBL_P4_SALA.setEnabled(false);


        DBrefUsuario = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        DBrefUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Nodos) {
                for(DataSnapshot nodo: Nodos.child(Perfil.USER_ID).getChildren()){
                    switch (nodo.getKey()){
                        case "Amigos":
                            for(DataSnapshot amigo: nodo.getChildren()){
                                UserFriendProfile _objFriend = new UserFriendProfile(Nodos.child(amigo.getValue().toString()).
                                                                                           child("Perfil").
                                                                                           child("usuario").getValue().toString(),
                                                                                            Nodos.child(amigo.getValue().toString()).
                                                                                                  child("Perfil").
                                                                                                  child("urlProfileImage").getValue().toString());
                                _dataListAmigosAdd.add(_objFriend);
                            }
                            for (UserFriendProfile ufp: _dataListAmigosAdd){
                                _dataListAmigosAddFilter.add(ufp);
                            }
                            break;
                    }
                }
                _adapterAmigosAdd.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        IV_P2_SALA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _sUserRemove = LBL_P2_SALA.getText().toString();
                for(int i = 0; i < LISTA_JUGADORES_SALA.size(); i++){
                    if (LISTA_JUGADORES_SALA.get(i).getUserID().equals(_sUserRemove)){
                        LISTA_JUGADORES_SALA.remove(i);
                    }
                }
                IV_P2_SALA.setEnabled(false);
                IV_P2_SALA.setImageResource(R.mipmap.ic_launcher);
                LBL_P2_SALA.setEnabled(false);
                LBL_P2_SALA.setText("Vacio");
            }
        });

        IV_P3_SALA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _sUserRemove = LBL_P3_SALA.getText().toString();
                for(int i = 0; i < LISTA_JUGADORES_SALA.size(); i++){
                    if (LISTA_JUGADORES_SALA.get(i).getUserID().equals(_sUserRemove)){
                        LISTA_JUGADORES_SALA.remove(i);
                    }
                }
                IV_P3_SALA.setEnabled(false);
                IV_P3_SALA.setImageResource(R.mipmap.ic_launcher);
                LBL_P3_SALA.setEnabled(false);
                LBL_P3_SALA.setText("Vacio");
            }
        });

        IV_P4_SALA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _sUserRemove = LBL_P4_SALA.getText().toString();
                for(int i = 0; i < LISTA_JUGADORES_SALA.size(); i++){
                    if (LISTA_JUGADORES_SALA.get(i).getUserID().equals(_sUserRemove)){
                        LISTA_JUGADORES_SALA.remove(i);
                    }
                }
                IV_P4_SALA.setEnabled(false);
                IV_P4_SALA.setImageResource(R.mipmap.ic_launcher);
                LBL_P4_SALA.setEnabled(false);
                LBL_P4_SALA.setText("Vacio");
            }
        });

        inputSearchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(inputSearchFriend.getText().toString().equalsIgnoreCase("")){
                    _dataListAmigosAddFilter.clear();
                    for (UserFriendProfile ufp: _dataListAmigosAdd){
                        _dataListAmigosAddFilter.add(ufp);
                    }
                    _adapterAmigosAdd.notifyDataSetChanged();
                    return;
                }
                _dataListAmigosAddFilter.clear();
                for (UserFriendProfile friendFilter: _dataListAmigosAdd){
                    if (friendFilter.getUserID().toLowerCase().contains(inputSearchFriend.getText().toString().toLowerCase())){
                        _dataListAmigosAddFilter.add(friendFilter);
                    }
                }
                _adapterAmigosAdd.notifyDataSetChanged();
            }
        });
    }

    public void ConfirmarPartida(View view){
        if(LISTA_JUGADORES_SALA.size() < 2){
            Snackbar.make(view,R.string.msg_error_min_opponent,Snackbar.LENGTH_LONG).show();

            return;
        }

        ArrayList<String> Secuencia = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int rand = (int) (Math.random() * 8) + 1;
            if(rand==1 ||rand==8){
                Secuencia.add("t");
            }
            else if(rand==2 ||rand==7){
                Secuencia.add("l");
            }
            else if(rand==3 ||rand==6){
                Secuencia.add("b");
            }
            else {
                Secuencia.add("r");
            }
        }
        CrearPartida Partida = new CrearPartida();

        ArrayList<Jugador> Jugadores = new ArrayList<>();
        for(UserFriendProfile jugador: LISTA_JUGADORES_SALA){
            if(jugador.getUserID().equals(Perfil.USER_ID)){
                Jugadores.add(new Jugador(jugador.getUserID(),1,Perfil.URL_IMAGE_PROFILE.toString()));
            }
            else{

                Jugadores.add(new Jugador(jugador.getUserID(),0,jugador.getUrlImageProfile()));
            }

        }
        Partida.setCantidadJugadores(LISTA_JUGADORES_SALA.size());
        Partida.setJugadores(Jugadores);
        Partida.setProximoJugador(Perfil.USER_ID);
        Partida.setSecuencia(Secuencia);
        Partida.setID(Partida.GenerarIDPartida(LISTA_JUGADORES_SALA.toString() + Secuencia.toString() + (int) (Math.random() * 999999)));
        Partida.setEstado(1);
        Partida.setAnfitrion(Perfil.USER_ID);

        DBrefPartidas = FirebaseDatabase.getInstance().getReference().child("Partidas");

        try{
            //Creando partida en Firebase
            DBrefPartidas.child(Partida.getID()).setValue(Partida);
            //Creando partida en DB3

            // Enviando la solicitud de partida a todos los participantes
            for(UserFriendProfile participante : LISTA_JUGADORES_SALA){
                if(participante.getUserID().equals(Perfil.USER_ID)){
                    DBrefUsuario.child(Perfil.USER_ID).child("Partidas").push().setValue(Partida.getID());
                    continue;
                }

                DBrefUsuario.child(participante.getUserID()).child("Partidas").push().setValue(Partida.getID());
            }




        }
        catch (Exception e){
            Toast.makeText(CreacionPartida.this,"Ocurrió un error al crear partida",Toast.LENGTH_LONG).show();
        }



        LISTA_JUGADORES_SALA.clear();
        Intent i = new Intent(this, InicioActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        LISTA_JUGADORES_SALA.clear();
        Intent i = new Intent(CreacionPartida.this,InicioActivity.class);
        startActivity(i);
        finish();
    }

    public void Back(View v){
        LISTA_JUGADORES_SALA.clear();
        Intent i = new Intent(CreacionPartida.this,InicioActivity.class);
        startActivity(i);
        finish();
    }


}
