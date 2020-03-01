package com.tamadev.ssay_mp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import database.SQLiteDB;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

public class MenuPrincipalActivity extends AppCompatActivity {
    private SQLiteDB helper;
    private String _sUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        if(AccessToken.getCurrentAccessToken() == null){
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
    public void goAmigos(View view){

        Intent i = new Intent(this, AmigosActivity.class);
        startActivity(i);
        finish();
    }
    public void CrearPerfil(View view){
        Intent i = new Intent(this, RegistroActivity.class);
        startActivity(i);
        finish();
    }
    public void Jugar(View view){
        Intent i = new Intent(MenuPrincipalActivity.this,LobbyPartidasActivity.class);
        startActivity(i);
        finish();
    }
    public void BorrarDatos(View vie){
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
}
