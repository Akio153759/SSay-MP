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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.herramientas_list_view.LV_SolicitudesAmistad;
import com.tamadev.ssay_mp.tablas_db_despliegue.Friends;

import java.util.ArrayList;
import java.util.List;

import database.SQLiteDB;

public class Amigos extends AppCompatActivity {
    private SQLiteDB helper;
    private ListView lvAmigos, lvSolicitudes;
    private EditText etSearchAmigos;
    private Button btnEnviarSolicitud;
    private TextView tvResultadoBusqueda;
    private DatabaseReference dbref;
    private String _sUsuarioAgregar, _sNombreAgregar;
    private List_Adapter _lSolicitudesAdapter;
    private ArrayList<LV_SolicitudesAmistad> _lSolicitudes;
    private ArrayList<String> _lAmigos;
    private ArrayAdapter<String> _lamigosAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        lvAmigos = (ListView)findViewById(R.id.lvAmigos);
        lvSolicitudes = (ListView)findViewById(R.id.lvSolicitudes);

        etSearchAmigos = (EditText)findViewById(R.id.etSearchAmigos);

        tvResultadoBusqueda = (TextView)findViewById(R.id.tvResultadoBusqueda);

        btnEnviarSolicitud = (Button)findViewById(R.id.btnEnviarSolicitud);

        btnEnviarSolicitud.setVisibility(View.INVISIBLE);


        SearchAmigos();
        SearchSolicitudes();
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
        _lAmigos = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                _lAmigos.add((c.getString(0)));
            }while (c.moveToNext());
        }
        else{
            _lAmigos.add("Aún no tienes amigos");

        }
        helper.close();
        _lamigosAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, _lAmigos);
        _lamigosAdapter.setNotifyOnChange(true);
        lvAmigos.setAdapter(_lamigosAdapter);
    }
    public void SearchSolicitudes(){
        helper = new SQLiteDB(this,"db",null,1);

        Cursor c = helper.ConsultarSolicitudesAmistad();
        _lSolicitudes = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                _lSolicitudes.add(new LV_SolicitudesAmistad(c.getString(0)));
            }while (c.moveToNext());
        }
        else{

        }
        helper.close();
        _lSolicitudesAdapter = new List_Adapter(this,R.layout.item_row,_lSolicitudes);
        _lSolicitudesAdapter.setNotifyOnChange(true);
        lvSolicitudes.setAdapter(_lSolicitudesAdapter);

    }

    public void EnviarSolicitud(View view){
        // Agrego el amigo en estado 0 (solicitud pendiente) en la db3
        helper.AgregarAmigo(_sUsuarioAgregar,_sNombreAgregar,0);

        // Utilizo la clase friends para crear el objeto de solicitud de amistad
        // ya que tiene la misma estructura que la tabla Amigos en la db3
        // y lo inserto en Firebase en estado de "Solicitud recibida"
        Friends SolicitudAmistad = new Friends();
        SolicitudAmistad.setUsuario(helper.GetDatoPerfil("Usuario"));
        SolicitudAmistad.setNombre(helper.GetDatoPerfil("Nombre"));
        SolicitudAmistad.setEstado(1);


        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(_sUsuarioAgregar).child("Amigos").child(helper.GetDatoPerfil("Usuario"));
        dbref.setValue(SolicitudAmistad);

        helper.close();
        Toast.makeText(this,"Solicitud de amistad enviada a " + _sUsuarioAgregar,Toast.LENGTH_SHORT).show();
        btnEnviarSolicitud.setVisibility(View.INVISIBLE);

    }

    private class List_Adapter extends ArrayAdapter<LV_SolicitudesAmistad> {

        private List<LV_SolicitudesAmistad> mList;
        private Context mContext;
        private int resourceLayout;
        SQLiteDB helper;
        public List_Adapter(@NonNull Context context, int resource, @NonNull List<LV_SolicitudesAmistad> objects) {
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
            final LV_SolicitudesAmistad modelo = mList.get(position);

            TextView tvUsuarioSolicitud = view.findViewById(R.id.tvUsuarioSolicitud);
            tvUsuarioSolicitud.setText(modelo.getSolicitud_usuario());

            Button btnAceptarSolicitud = view.findViewById(R.id.btnAceptarSolicitud);
            Button btnRechazarSolicitd = view.findViewById(R.id.btnRechazarSolicitud);

            btnAceptarSolicitud.setBackgroundResource(R.drawable.btnconfirmar);
            btnRechazarSolicitd.setBackgroundResource(R.drawable.btncancelar);

            helper = new SQLiteDB(mContext,"db",null,1);

            btnAceptarSolicitud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.Confirmar_Rechazar_SolicitudAmistad(modelo.getSolicitud_usuario(),2);

                    // Sitúo la referencia a firebase en el nodo de la persona que envio la solicitud - en sus amigos -
                    // en mi registro de amigo, donde el estado actual es 0 por que el envió la solicitud
                    // y asi poder cambiarlo a 2 (Aceptado)
                    dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(modelo.getSolicitud_usuario()).child("Amigos").child(helper.GetDatoPerfil("Usuario")).child("estado");
                    dbref.setValue("2");

                    // aca limpio los dataList de las solicitudes y los amigos y los recargo
                    _lSolicitudes.clear();
                    _lAmigos.clear();

                    Cursor _cSolicitudes = helper.ConsultarSolicitudesAmistad();
                    Cursor _cAmigos = helper.ConsultarAmigos();

                    if(_cAmigos.moveToFirst()){
                        do{
                            _lAmigos.add((_cAmigos.getString(0)));
                        }while (_cAmigos.moveToNext());
                    }
                    else{
                        _lAmigos.add("Aún no tienes amigos");

                    }

                    if(_cSolicitudes.moveToFirst()){
                        do{
                            _lSolicitudes.add(new LV_SolicitudesAmistad(_cSolicitudes.getString(0)));
                        }while (_cSolicitudes.moveToNext());
                    }
                    else{

                    }
                    _lSolicitudesAdapter.notifyDataSetChanged();
                    _lamigosAdapter.notifyDataSetChanged();
                    helper.close();



                }
            });

            btnRechazarSolicitd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.Confirmar_Rechazar_SolicitudAmistad(modelo.getSolicitud_usuario(),3);

                    // Sitúo la referencia a firebase en el nodo de la persona que envio la solicitud - en sus amigos -
                    // en mi registro de amigo, donde el estado actual es 0 por que el envió la solicitud
                    // y asi poder cambiarlo a 3 (Rechazado)
                    dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(modelo.getSolicitud_usuario()).child("Amigos").child(helper.GetDatoPerfil("Usuario")).child("estado");
                    dbref.setValue("3");

                    // aca limpio el dataList de las solicitudes y lo recargo
                    _lSolicitudes.clear();

                    Cursor c = helper.ConsultarSolicitudesAmistad();

                    if(c.moveToFirst()){
                        do{
                            _lSolicitudes.add(new LV_SolicitudesAmistad(c.getString(0)));
                        }while (c.moveToNext());
                    }
                    else{
                        _lSolicitudes.add(new LV_SolicitudesAmistad("No tienes solicitudes pendientes"));
                    }
                    _lSolicitudesAdapter.notifyDataSetChanged();
                    helper.close();


                }
            });

            return  view;
        }
    }
}
