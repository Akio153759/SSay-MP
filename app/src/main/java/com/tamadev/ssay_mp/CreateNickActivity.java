package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import database.SQLiteDB;

public class CreateNickActivity extends AppCompatActivity {

    private EditText etNombre, etUsuario, etEmail, etPass, etRepPass;
    DatabaseReference dbref;
    Perfil perfil;
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

        perfil = new Perfil();

        dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios");

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
    }

    public void CrearPerfil(View view){
        String sNombre = etNombre.getText().toString().trim();
        String sUsuario = etUsuario.getText().toString().trim();
        String sEmail = etEmail.getText().toString().trim();
        String sPassword = etPass.getText().toString();
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
        helper = new SQLiteDB(this,"db",null,1);
        helper.RegistrarPerfil(sUsuario,sNombre,sPassword,sEmail);
        helper.close();

        Toast.makeText(this,"Perfil registrado exitosamente",Toast.LENGTH_LONG).show();
        Intent i = new Intent(this,MenuPrincipal.class);
        startActivity(i);
        finish();
    }
}
