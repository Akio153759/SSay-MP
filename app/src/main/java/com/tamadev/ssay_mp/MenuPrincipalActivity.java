package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
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
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.utils.AlertDialogIngreso;

public class MenuPrincipalActivity extends AppCompatActivity implements AlertDialogIngreso.AlertDialogIngresoCallback {
    private SQLiteDB helper;
    private DatabaseReference DBrefUsuario;
    private ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        DBrefUsuario = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){

            Perfil.NAME = user.getDisplayName();
            String _sEmail = user.getEmail();
            Perfil.URL_IMAGE_PROFILE = user.getPhotoUrl();
            Perfil.UID = user.getUid();

            ivProfile = (ImageView)findViewById(R.id.ivPerfil);

            Picasso.with(this).load(Perfil.URL_IMAGE_PROFILE).error(R.mipmap.ic_launcher).fit().centerInside().into(ivProfile);


            DBrefUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        try{
                            Perfil.USER_ID = dataSnapshot.child("IdentificadoresUnicos").child(Perfil.UID).getValue().toString();
                            Perfil.NAME = dataSnapshot.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("nombre").getValue().toString();
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
            DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("uid").setValue(Perfil.UID);
        }
    }
}
