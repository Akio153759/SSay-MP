package com.tamadev.ssay_mp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import database.SQLiteDB;

public class MenuPrincipal extends AppCompatActivity {
    private SQLiteDB helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
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
    public void AddAmigo(View view){
        Intent i = new Intent(this, Amigos.class);
        startActivity(i);
        finish();
    }
    public void CrearPerfil(View view){
        Intent i = new Intent(this, CreateNickActivity.class);
        startActivity(i);
        finish();
    }
}
