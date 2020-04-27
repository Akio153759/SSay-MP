package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.classes.Perfil;
import com.tamadev.ssay_mp.classes.UserFriendProfile;
import com.tamadev.ssay_mp.classes.UsuarioEnFirebase;
import com.tamadev.ssay_mp.utils.AlertDialogNewFriend;
import com.tamadev.ssay_mp.utils.AlertDialogSearchUser;
import com.tamadev.ssay_mp.utils.RVAdapterFriendList;
import com.tamadev.ssay_mp.utils.RVAdapterUserList;

import java.util.ArrayList;
import java.util.List;

public class AmigosActivity extends AppCompatActivity {



    private ArrayList<UsuarioEnFirebase> _dataListIDAmigos = new ArrayList<>();

    public static ArrayList<UserFriendProfile> _dataListAmigos = new ArrayList<>();
    public static DatabaseReference DBrefUsuarioFriends;
    public static ArrayList<String> _dataListSolicitudesEnviadas = new ArrayList<>();
    public static ArrayList<UserFriendProfile> _dataListAllUsers;
    public static ArrayList<UserFriendProfile> _dataListAllUsersFilter;


    private ArrayList<UserFriendProfile> _dataListAmigosFilter = new ArrayList<>();
    private ArrayList<UsuarioEnFirebase> _dataListIDAmigosFilter = new ArrayList<>();
    private RecyclerView recyclerView;
    public static EditText etSearchFriend;
    private LinearLayoutManager layoutManager;
    private RVAdapterFriendList adapterFriendList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AlertDialogNewFriend.adapterUserList = new RVAdapterUserList(_dataListAllUsersFilter,AmigosActivity.this);

        setContentView(R.layout.activity_amigos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_amigos);
        etSearchFriend = findViewById(R.id.etSearchFriend);

        Perfil.ACTIVITY_NAVIGATION = false;


        layoutManager = new LinearLayoutManager(AmigosActivity.this,LinearLayoutManager.VERTICAL,false);


        adapterFriendList = new RVAdapterFriendList(_dataListAmigosFilter,_dataListIDAmigosFilter,AmigosActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterFriendList);





        DBrefUsuarioFriends.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Nodos) {
                _dataListAmigos.clear();
                _dataListIDAmigos.clear();
                _dataListAmigosFilter.clear();
                _dataListIDAmigosFilter.clear();
                for(DataSnapshot nodo: Nodos.child(Perfil.USER_ID).getChildren()){
                    switch (nodo.getKey()){
                        case "Amigos":
                            for(DataSnapshot amigo: nodo.getChildren()){
                                _dataListIDAmigos.add(new UsuarioEnFirebase(amigo.getKey(),amigo.getValue().toString()));
                                UserFriendProfile _objFriend = new UserFriendProfile(Nodos.child(amigo.getValue().toString()).
                                        child("Perfil").
                                        child("usuario").getValue().toString(),
                                        Nodos.child(amigo.getValue().toString()).
                                                child("Perfil").
                                                child("urlProfileImage").getValue().toString(),
                                        Boolean.parseBoolean(
                                        Nodos.child(amigo.getValue().toString()).
                                                child("Perfil").
                                                child("enLinea").getValue().toString()));
                                _dataListAmigos.add(_objFriend);
                            }
                            for (UserFriendProfile ufp: _dataListAmigos){
                                _dataListAmigosFilter.add(ufp);
                            }
                            for (UsuarioEnFirebase uf: _dataListIDAmigos){
                                _dataListIDAmigosFilter.add(uf);
                            }
                            break;
                        case "SolicitudesEnviadas":
                            _dataListSolicitudesEnviadas.clear();
                            for(DataSnapshot user: nodo.getChildren()){
                                _dataListSolicitudesEnviadas.add(user.getValue().toString());
                            }
                    }
                }
                adapterFriendList.notifyDataSetChanged();
                AlertDialogNewFriend.adapterUserList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        etSearchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etSearchFriend.getText().toString().equalsIgnoreCase("")){
                    _dataListAmigosFilter.clear();
                    _dataListIDAmigosFilter.clear();
                    for (UserFriendProfile ufp: _dataListAmigos){
                        _dataListAmigosFilter.add(ufp);
                    }
                    for(UsuarioEnFirebase uf: _dataListIDAmigos){
                        _dataListIDAmigosFilter.add(uf);
                    }
                    adapterFriendList.notifyDataSetChanged();
                    return;
                }
                _dataListAmigosFilter.clear();
                _dataListIDAmigosFilter.clear();
                for (UserFriendProfile friendFilter: _dataListAmigos){
                    if (friendFilter.getUserID().toLowerCase().contains(etSearchFriend.getText().toString().toLowerCase())){
                        _dataListAmigosFilter.add(friendFilter);
                    }
                }
                for (UsuarioEnFirebase friendFilter: _dataListIDAmigos){
                    if (friendFilter.getUsuario().toLowerCase().contains(etSearchFriend.getText().toString().toLowerCase())){
                        _dataListIDAmigosFilter.add(friendFilter);
                    }
                }

                adapterFriendList.notifyDataSetChanged();
            }
        });

    }

    public void AddNewFriend(View view){
        new AlertDialogNewFriend(AmigosActivity.this);
    }

    public void Regresar(View view){
        Perfil.ACTIVITY_NAVIGATION = true;
        Intent i = new Intent(this, InicioActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onBackPressed() {
        Perfil.ACTIVITY_NAVIGATION = true;
        Intent i = new Intent(this, InicioActivity.class);
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



            btnAceptarSolicitud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBrefUsuarioFriends.child("Amigos").push().setValue(modelo.getUsuario());
                    DatabaseReference DBrefAmigo = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(modelo.getUsuario()).child("Amigos");
                    DBrefAmigo.push().setValue(Perfil.USER_ID);
                    DBrefUsuarioFriends.child("SolicitudesAmistad").child(modelo.getId()).removeValue();
                }
            });

            btnRechazarSolicitd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBrefUsuarioFriends.child("SolicitudesAmistad").child(modelo.getId()).removeValue();
                }
            });

            return view;
        }
    }
    public void BuscarAmigo(View view){
        new AlertDialogSearchUser(AmigosActivity.this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(Perfil.ONLINE && !Perfil.ACTIVITY_NAVIGATION){
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("enLinea").setValue(false);
            Perfil.ONLINE = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Perfil.ONLINE){
            InicioActivity.DBrefUsuario.child("Usuarios").child(Perfil.USER_ID).child("Perfil").child("enLinea").setValue(true);
            Perfil.ONLINE = true;
        }
    }
}
