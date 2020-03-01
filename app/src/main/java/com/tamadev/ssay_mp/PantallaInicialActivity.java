package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import database.SQLiteDB;

public class PantallaInicialActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicial);

        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.loguinButton);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                goMainScreen();
            }

            @Override
            public void onCancel() {
                Toast.makeText(PantallaInicialActivity.this,"Cancelado",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(PantallaInicialActivity.this,"Error" + error,Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        /*
        final EditText etUsuario = (EditText)findViewById(R.id.etUsuarioIngreso);
        final EditText etPassword = (EditText)findViewById(R.id.etPasswordIngreso);

        Button btnIngresar = (Button)findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _sUserInput = etUsuario.getText().toString();
                final String _sPassInput = etPassword.getText().toString();

                if (_sUserInput.equals("") || _sUserInput.isEmpty())
                {
                    Toast.makeText(PantallaInicialActivity.this,"Ingrese un usuario",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (_sPassInput.equals("") || _sPassInput.isEmpty())
                {
                    Toast.makeText(PantallaInicialActivity.this,"Ingrese la contraseña",Toast.LENGTH_SHORT).show();
                    return;
                }

                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            try
                            {
                                String _sUser = dataSnapshot.child(_sUserInput).child("Perfil").child("usuario").getValue().toString();
                                String _sPassword = dataSnapshot.child(_sUserInput).child("Perfil").child("password").getValue().toString();

                                if(_sUserInput.equals(_sUser)&&_sPassInput.equals(_sPassword)){
                                    SQLiteDB helper = new SQLiteDB(PantallaInicialActivity.this,"db",null,1);
                                    helper.GuardarInicioSesion(_sUser);
                                    helper.close();
                                    Intent i = new Intent(PantallaInicialActivity.this,MenuPrincipalActivity.class);
                                    startActivity(i);
                                    finish();
                                    return;
                                }
                                else
                                {
                                    Toast.makeText(PantallaInicialActivity.this,"Usuario y/o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(PantallaInicialActivity.this,"Usuario y/o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        TextView tvRegistro = (TextView)findViewById(R.id.tvRegistro);
        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PantallaInicialActivity.this,RegistroActivity.class);
                startActivity(i);
                finish();
            }
        });
        */
    }

    private void goMainScreen() {
        Intent i = new Intent(PantallaInicialActivity.this,MenuPrincipalActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
