package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import database.SQLiteDB;

public class Amigos extends AppCompatActivity {
    private SQLiteDB helper;
    private ListView lvAmigos;
    private EditText etSearchAmigos;
    private Button btnEnviarSolicitud;
    private TextView tvResultadoBusqueda;
    private DatabaseReference dbref;
    private String _sUsuarioAgregar, _sNombreAgregar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        lvAmigos = (ListView)findViewById(R.id.lvAmigos);

        etSearchAmigos = (EditText)findViewById(R.id.etSearchAmigos);
        tvResultadoBusqueda = (TextView)findViewById(R.id.tvResultadoBusqueda);
        btnEnviarSolicitud = (Button)findViewById(R.id.btnEnviarSolicitud);

        btnEnviarSolicitud.setVisibility(View.INVISIBLE);


        SearchAmigos();
    }
    public void Regresar(View view){
        Intent i = new Intent(this, MenuPrincipal.class);
        startActivity(i);
    }
    public void SearchUsuario(View view){
        final String _sInputAmigos = etSearchAmigos.getText().toString().trim();
        if(_sInputAmigos.isEmpty()){
            Toast.makeText(this,"Ingrese un usuario a buscar",Toast.LENGTH_SHORT).show();
            return;
        }
        dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    try {
                        String _sResult_Busqueda = dataSnapshot.child(_sInputAmigos).child("Perfil").child("usuario").getValue().toString() + " - " + dataSnapshot.child(_sInputAmigos).child("Perfil").child("nombre").getValue().toString();
                        if (!_sResult_Busqueda.isEmpty() || !_sResult_Busqueda.equals("")) {
                            _sUsuarioAgregar = dataSnapshot.child(_sInputAmigos).child("Perfil").child("usuario").getValue().toString();
                            _sNombreAgregar = dataSnapshot.child(_sInputAmigos).child("Perfil").child("nombre").getValue().toString();
                            tvResultadoBusqueda.setText(_sResult_Busqueda);
                            btnEnviarSolicitud.setVisibility(View.VISIBLE);

                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Amigos.this,"No se encontró resultado",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void SearchAmigos(){
        helper = new SQLiteDB(this,"db",null,1);

        Cursor c = helper.ConsultarAmigos();
        ArrayList<String> _lAmigos = new ArrayList<>();
        if(c != null){
            if(c.moveToFirst()){
                do{
                    _lAmigos.add((c.getString(0)));
                }while (c.moveToNext());

            }

        }
        else{
            Toast.makeText(this,"Sin amigos",Toast.LENGTH_LONG).show();
            return;
        }
        helper.close();
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, _lAmigos);
        lvAmigos.setAdapter(aa);
    }

    public void EnviarSolicitud(View view){

        helper.AgregarAmigo(_sUsuarioAgregar,_sNombreAgregar);
        helper.close();
    }
}
