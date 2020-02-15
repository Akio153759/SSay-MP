package com.tamadev.ssay_mp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import database.SQLiteDB;

public class MenuPrincipal extends AppCompatActivity {
    private SQLiteDB helper;
    private String _sUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        helper = new SQLiteDB(this,"db",null,1);
        _sUsuario = helper.GetDatoPerfil("Usuario");
        String pathDB = getDatabasePath("db").toString();
        String to = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + getPackageName() + "/" + "dbBK";
        helper.copiaBD(pathDB,to);
        helper.close();
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
    public void BorrarDatos(View vie){
        helper = new SQLiteDB(this,"db",null,1);
        if(helper.BorrarDatos(_sUsuario) == true){
            Toast.makeText(this,"Datos borrados con éxito",Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this,"Error al eliminar datos",Toast.LENGTH_SHORT).show();
        }
    }
}
