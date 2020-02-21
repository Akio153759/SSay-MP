package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.classes.Profile;

import database.SQLiteDB;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etUsuario, etEmail, etPass, etRepPass;
    private DatabaseReference dbref;
    Boolean bUsuarioExistente = false;
    private SQLiteDB helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_nick);

        etNombre = (EditText)findViewById(R.id.etNombre);
        etUsuario = (EditText)findViewById(R.id.etUsuario);
        etEmail= (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPassword);
        etRepPass = (EditText)findViewById(R.id.etRepPass);

        dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios");


    }

    public void CrearPerfil(View view){

        final String sNombre = etNombre.getText().toString().trim();
        final String sUsuario = etUsuario.getText().toString().trim();
        final String sEmail = etEmail.getText().toString().trim();
        final String sPassword = etPass.getText().toString();
        String sRepPass = etRepPass.getText().toString();

        if(sUsuario.isEmpty()){
            Toast.makeText(this,"El usuario es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }
        if(sNombre.isEmpty()){
            Toast.makeText(this,"El nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }
        if(sPassword.isEmpty()){
            Toast.makeText(this,"La contraseña es obligatoria", Toast.LENGTH_SHORT).show();
            return;
        }
        if(sEmail.isEmpty()){
            Toast.makeText(this,"El mail es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!sPassword.equals(sRepPass)){
            Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            return;
        }
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try
                    {
                        String _sConsultaUser = dataSnapshot.child(sUsuario).child("Perfil").child("usuario").getValue().toString();
                        String _sConsultaEmail = dataSnapshot.child(sUsuario).child("Perfil").child("email").getValue().toString();
                        if(_sConsultaUser.equals(sUsuario))
                        {
                            Toast.makeText(RegistroActivity.this,"El usuario ingresado ya se encuentra en uso",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    catch (Exception e)
                    {
                        try {
                            Profile _objPerfil = new Profile();
                            _objPerfil.setUsuario(sUsuario);
                            _objPerfil.setPassword(sPassword);
                            _objPerfil.setNombre(sNombre);
                            _objPerfil.setEmail(sEmail);
                            dbref.child(sUsuario).child("Perfil").setValue(_objPerfil);
                            helper = new SQLiteDB(RegistroActivity.this, "db", null, 1);
                            helper.GuardarInicioSesion(sUsuario);

                            helper.close();

                            Toast.makeText(RegistroActivity.this, "Perfil registrado exitosamente", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(RegistroActivity.this, MenuPrincipalActivity.class);
                            startActivity(i);
                            finish();
                            return;
                        }
                        catch (Exception ex){
                            Toast.makeText(RegistroActivity.this,"Error al registrar perfil",Toast.LENGTH_SHORT).show();
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
