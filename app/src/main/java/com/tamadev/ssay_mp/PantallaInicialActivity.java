package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.classes.Perfil;

import java.security.MessageDigest;

import database.SQLiteDB;

public class PantallaInicialActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference DBRefInicial ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_pantalla_inicial);


        DBRefInicial = FirebaseDatabase.getInstance().getReference();


        DBRefInicial.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean _bServerOnline = Boolean.parseBoolean(dataSnapshot.child("ServidorOnline").getValue().toString());
                double _dVersionActual = Double.parseDouble(dataSnapshot.child("Version").getValue().toString());

                if(!_bServerOnline){
                    finish();
                    return;
                }
                if(Perfil.VERSION_APK < _dVersionActual){
                    Toast.makeText(PantallaInicialActivity.this,"Version vieja", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton)findViewById(R.id.loguinButton);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    goSplashScreen();
                }
            }
        };






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

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void goSplashScreen() {
        Intent i = new Intent(PantallaInicialActivity.this,SplashScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
