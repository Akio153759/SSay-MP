package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.classes.LV_Usuario;
import com.tamadev.ssay_mp.classes.UsuarioEnFirebase;
import com.tamadev.ssay_mp.utils.AlertDialogSearchUser;

import java.util.ArrayList;
import java.util.List;

import database.SQLiteDB;

public class AmigosActivity extends AppCompatActivity {
    private SQLiteDB helper = new SQLiteDB(AmigosActivity.this,"db",null,1);
    private ListView lvAmigos, lvSolicitudes;
    private EditText etSearchAmigos;
    private Button btnEnviarSolicitud;
    private TextView tvResultadoBusqueda;
    private DatabaseReference dbref;
    private String _sUsuarioAgregar, _sNombreAgregar;
    private List_Adapter _lSolicitudesAdapter;
    private ArrayList<UsuarioEnFirebase> _lSolicitudes;
    private ArrayList<String> _lAmigos;
    private ArrayAdapter<String> _lamigosAdapter;
    private DatabaseReference DBrefUsuario ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        lvAmigos = (ListView)findViewById(R.id.lvAmigos);
        lvSolicitudes = (ListView)findViewById(R.id.lvSolicitudes);


        _lAmigos = new ArrayList<>();
        _lamigosAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, _lAmigos);
        _lamigosAdapter.setNotifyOnChange(true);
        lvAmigos.setAdapter(_lamigosAdapter);

        _lSolicitudes = new ArrayList<>();
        _lSolicitudesAdapter = new List_Adapter(this,R.layout.item_row_solicitudes,_lSolicitudes);
        _lSolicitudesAdapter.setNotifyOnChange(true);
        lvSolicitudes.setAdapter(_lSolicitudesAdapter);

        DBrefUsuario = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(helper.GetUser());
        /*
        etSearchAmigos = (EditText)findViewById(R.id.etSearchAmigos);

        tvResultadoBusqueda = (TextView)findViewById(R.id.tvResultadoBusqueda);

        btnEnviarSolicitud = (Button)findViewById(R.id.btnEnviarSolicitud);
*/
        DBrefUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Nodos) {
                _lSolicitudes.clear();
                _lAmigos.clear();
                for(DataSnapshot nodo: Nodos.getChildren()){
                    switch (nodo.getKey()){
                        case "SolicitudesAmistad":

                            for(DataSnapshot solicitud: nodo.getChildren()){

                                _lSolicitudes.add(new UsuarioEnFirebase(solicitud.getKey(),solicitud.getValue().toString()));
                            }

                            break;
                        case "Amigos":

                            for(DataSnapshot amigo: nodo.getChildren()) {

                                _lAmigos.add(amigo.getValue().toString());
                            }

                            break;
                    }
                }
                _lSolicitudesAdapter.notifyDataSetChanged();
                _lamigosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    public void Regresar(View view){
        Intent i = new Intent(this, MenuPrincipalActivity.class);
        startActivity(i);
        finish();
    }
    /*
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
                        Toast.makeText(AmigosActivity.this,"No se encontró resultado",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
*/

    public void SearchAmigos(){




        //--------------------
        /*
        helper = new SQLiteDB(this,"db",null,1);

        Cursor c = helper.ConsultarAmigos();

        if(c.moveToFirst()){
            do{
                _lAmigos.add((c.getString(0)));
            }while (c.moveToNext());
        }
        else{
            _lAmigos.add("Aún no tienes amigos");

        }
        helper.close();

         */

    }


    public void SearchSolicitudes(){


        //---------------------------------------
        /*
        helper = new SQLiteDB(this,"db",null,1);

        Cursor c = helper.ConsultarSolicitudesAmistad();

        if(c.moveToFirst()){
            do{
                _lSolicitudes.add(new LV_Usuario(c.getString(0)));
            }while (c.moveToNext());
        }
        else{

        }
        helper.close();


         */

    }
/*
    public void EnviarSolicitud(View view){
        // Agrego el amigo en estado 0 (solicitud pendiente) en la db3
        helper.AgregarAmigo(_sUsuarioAgregar,_sNombreAgregar,0);

        // Utilizo la clase friends para crear el objeto de solicitud de amistad
        // ya que tiene la misma estructura que la tabla AmigosActivity en la db3
        // y lo inserto en Firebase en estado de "Solicitud recibida"
        Friends SolicitudAmistad = new Friends();
        SolicitudAmistad.setUsuario(helper.GetDatoPerfil("Usuario"));
        SolicitudAmistad.setNombre(helper.GetDatoPerfil("Nombre"));
        SolicitudAmistad.setEstado(1);


        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(_sUsuarioAgregar).child("AmigosActivity").child(helper.GetDatoPerfil("Usuario"));
        dbref.setValue(SolicitudAmistad);
        helper.PushDB();
        helper.close();
        Toast.makeText(this,"Solicitud de amistad enviada a " + _sUsuarioAgregar,Toast.LENGTH_SHORT).show();
        btnEnviarSolicitud.setVisibility(View.INVISIBLE);

    }
*/

    private class List_Adapter extends ArrayAdapter<UsuarioEnFirebase> {

        private List<UsuarioEnFirebase> mList;
        private Context mContext;
        private int resourceLayout;

        public List_Adapter(@NonNull Context context, int resource, @NonNull List<UsuarioEnFirebase> objects) {
            super(context, resource, objects);
            this.mList = objects;
            this.mContext = context;
            this.resourceLayout = resource;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if(view == null){
                view = LayoutInflater.from(mContext).inflate(resourceLayout,null);
            }
            final UsuarioEnFirebase modelo = mList.get(position);

            TextView tvUsuarioSolicitud = view.findViewById(R.id.tvUsuarioSolicitud);
            tvUsuarioSolicitud.setText(modelo.getUsuario());

            Button btnAceptarSolicitud = view.findViewById(R.id.btnAceptarSolicitud);
            Button btnRechazarSolicitd = view.findViewById(R.id.btnRechazarSolicitud);

            btnAceptarSolicitud.setBackgroundResource(R.drawable.btnconfirmar);
            btnRechazarSolicitd.setBackgroundResource(R.drawable.btncancelar);

            helper = new SQLiteDB(mContext,"db",null,1);

            btnAceptarSolicitud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBrefUsuario.child("Amigos").push().setValue(modelo.getUsuario());
                    DatabaseReference DBrefAmigo = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(modelo.getUsuario()).child("Amigos");
                    DBrefAmigo.push().setValue(helper.GetUser());
                    DBrefUsuario.child("SolicitudesAmistad").child(modelo.getId()).removeValue();
                }
            });

            btnRechazarSolicitd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBrefUsuario.child("SolicitudesAmistad").child(modelo.getId()).removeValue();
                }
            });

            return view;
        }
    }
    public void BuscarAmigo(View view){
        new AlertDialogSearchUser(AmigosActivity.this);
    }
}
