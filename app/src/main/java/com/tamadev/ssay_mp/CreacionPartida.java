package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import database.SQLiteDB;

public class CreacionPartida extends AppCompatActivity {

    private CheckBox cbAmigo1, cbAmigo2, cbAmigo3, cbAmigo4;
    private SQLiteDB helper;
    private String _sUsuario;
    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_partida);

        cbAmigo1 = (CheckBox)findViewById(R.id.cbAmigo1);
        cbAmigo2 = (CheckBox)findViewById(R.id.cbAmigo2);
        cbAmigo3 = (CheckBox)findViewById(R.id.cbAmigo3);
        cbAmigo4 = (CheckBox)findViewById(R.id.cbAmigo4);

        helper = new SQLiteDB(this,"db",null,1);
        Cursor c = helper.ConsultarAmigos();
        if(c.moveToFirst()){
            cbAmigo1.setText(c.getString(0));
        }
        if(c.moveToNext()){
            cbAmigo2.setText(c.getString(0));
        }
        if(c.moveToNext()){
            cbAmigo3.setText(c.getString(0));
        }
        if(c.moveToNext()){
            cbAmigo4.setText(c.getString(0));
        }

        Cursor _cUsuario = helper.GetUsuario();
        if(_cUsuario.moveToFirst()){
            _sUsuario = _cUsuario.getString(0);
        }
        helper.close();
    }

    public void ConfirmarPartida(View view){
        ArrayList<String> Jugadores = new ArrayList<>();
        ArrayList<String> Secuencia = new ArrayList<>();
        Jugadores.add(_sUsuario);

        if(cbAmigo1.isChecked()){
            Jugadores.add(cbAmigo1.getText().toString());
        }
        if(cbAmigo2.isChecked()){
            Jugadores.add(cbAmigo2.getText().toString());
        }
        if(cbAmigo3.isChecked()){
            Jugadores.add(cbAmigo3.getText().toString());
        }
        if(cbAmigo4.isChecked()){
            Jugadores.add(cbAmigo4.getText().toString());
        }

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


        Partida.setCantidadJugadores(Jugadores.size());
        Partida.setJugadores(Jugadores);
        Partida.setProximoJugador(_sUsuario);
        Partida.setSecuencia(Secuencia);
        Partida.setID(Partida.GenerarIDPartida(Jugadores.toString() + Secuencia.toString() + (int) (Math.random() * 999999)));

        dbref = FirebaseDatabase.getInstance().getReference().child("Partidas");

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Despliegue a DB Firebase
        try{
            dbref.child(Partida.getID()).setValue(Partida);
            helper = new SQLiteDB(this,"db",null,1);
            helper.CrearPartida(Partida.getID(),Partida.getCantidadJugadores(),1);
            for(final String amigo: Partida.getJugadores()){
                if(amigo.equals(_sUsuario)){
                    continue;
                }
                helper.CrearRelacionPartidaAmigos(Partida.getID(),amigo);
            }
            helper.PushDB();
            helper.close();
            Toast.makeText(this,"Partida creada con éxito",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this,"Ocurrió un error al crear partida",Toast.LENGTH_SHORT).show();
        }

        Intent i = new Intent(this,MenuPrincipal.class);
        startActivity(i);
        finish();
    }
}
