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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.LV_Usuario;
import com.tamadev.ssay_mp.classes.Matches;

import java.util.ArrayList;
import java.util.List;

import database.SQLiteDB;

public class CreacionPartida extends AppCompatActivity {

    private CheckBox cbAmigo1, cbAmigo2, cbAmigo3, cbAmigo4;
    private SQLiteDB helper;
    private String _sUsuario;
    private DatabaseReference dbref;
    private ArrayList<LV_Usuario> _dataListJugadores = new ArrayList<>();
    private ArrayList<LV_Usuario> _dataListAmigosAdd = new ArrayList<>();
    private List_Adapter_Add _adapterAmigosAdd;
    private List_Adapter_Remove _adapterAmigosRemove;
    private ListView _lvParticipantes;
    private ListView _lvAmigos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_partida);

        _lvAmigos = (ListView)findViewById(R.id.lvAddAmigos);
        _lvParticipantes = (ListView)findViewById(R.id.lvParticipantes);


        helper = new SQLiteDB(this,"db",null,1);
        _sUsuario = helper.GetDatoPerfil("Usuario");
        Cursor _cAmigos = helper.ConsultarAmigos();
        if(_cAmigos.moveToFirst()){
            do{
                LV_Usuario lvu = new LV_Usuario(_cAmigos.getString(0));
                _dataListAmigosAdd.add(lvu);
            }while (_cAmigos.moveToNext());
        }


        _adapterAmigosAdd = new List_Adapter_Add(CreacionPartida.this,R.layout.item_row_participante_add,_dataListAmigosAdd);
        _adapterAmigosAdd.setNotifyOnChange(true);
        _lvAmigos.setAdapter(_adapterAmigosAdd);



        _adapterAmigosRemove = new List_Adapter_Remove(CreacionPartida.this,R.layout.item_row_participante_remove,_dataListJugadores);
        _adapterAmigosRemove.setNotifyOnChange(true);
        _lvParticipantes.setAdapter(_adapterAmigosRemove);

        helper.close();
    }

    public void ConfirmarPartida(View view){

        ArrayList<String> Secuencia = new ArrayList<>();
        _dataListJugadores.add(new LV_Usuario(_sUsuario));


        for (int i = 0; i < 4; i++) {
            int rand = (int) (Math.random() * 8) + 1;
            if(rand==1 ||rand==8){
                Secuencia.add("t");
            }
            else if(rand==2 ||rand==7){
                Secuencia.add("l");
            }
            else if(rand==3 ||rand==6){
                Secuencia.add("b");
            }
            else {
                Secuencia.add("r");
            }
        }
        CrearPartida Partida = new CrearPartida();

        ArrayList<String> Jugadores = new ArrayList<>();
        for(LV_Usuario jugador: _dataListJugadores){
            Jugadores.add(jugador.getSolicitud_usuario());
        }
        Partida.setCantidadJugadores(_dataListJugadores.size());
        Partida.setJugadores(Jugadores);
        Partida.setProximoJugador(_sUsuario);
        Partida.setSecuencia(Secuencia);
        Partida.setID(Partida.GenerarIDPartida(_dataListJugadores.toString() + Secuencia.toString() + (int) (Math.random() * 999999)));
        Partida.setEstado(1);
        Partida.setAnfitrion(_sUsuario);

        dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(_sUsuario);

        try{
            //Creando partida en Firebase
            dbref.child("Partidas").child(Partida.getID()).setValue(Partida);
            //Creando partida en DB3
            helper = new SQLiteDB(this,"db",null,1);
            helper.CrearPartida(Partida.getID(),Partida.getCantidadJugadores(),1,1,_sUsuario);
            CrearPartida SolicitudPartida = new CrearPartida();
            for(final String amigo: Partida.getJugadores()){

                if(amigo.equals(_sUsuario)){
                    continue;
                }
                DatabaseReference dbrefAmigo = FirebaseDatabase.getInstance().getReference();
                // Llenando tabla PartidaxAmigos
                helper.CrearRelacionPartidaAmigos(Partida.getID(),amigo);
                // Por cada participante de la partida
                // Utilizo la clase matches para crear solicitud de partida ya que el
                // objeto tiene la estrutura de la tabla Partidas en la db3
                SolicitudPartida.setID(Partida.getID());
                SolicitudPartida.setCantidadJugadores(Partida.getCantidadJugadores());
                SolicitudPartida.setJugadores(Partida.getJugadores());
                SolicitudPartida.setSecuencia(Partida.getSecuencia());
                SolicitudPartida.setProximoJugador(_sUsuario);
                SolicitudPartida.setEstado(0);
                SolicitudPartida.setAnfitrion(_sUsuario);

                dbrefAmigo.child("Usuarios").child(amigo).child("Partidas").child(SolicitudPartida.getID()).setValue(SolicitudPartida);
            }
            // Subiendo DB3
            helper.PushDB();
            helper.close();
            dbref = null;

            Toast.makeText(this,"Partida creada con éxito",Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){
            Toast.makeText(this,"Ocurrió un error al crear partida",Toast.LENGTH_SHORT).show();
        }




        Intent i = new Intent(this, MenuPrincipalActivity.class);
        startActivity(i);
        finish();
    }

    public void Back(View v){
        Intent i = new Intent(CreacionPartida.this,LobbyPartidasActivity.class);
        startActivity(i);
        finish();
    }

    private class List_Adapter_Add extends ArrayAdapter<LV_Usuario> {

        private List<LV_Usuario> mList;
        private Context mContext;
        private int resourceLayout;

        public List_Adapter_Add(@NonNull Context context, int resource, @NonNull List<LV_Usuario> objects) {
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
            final LV_Usuario modelo = mList.get(position);

            TextView tvParticip= view.findViewById(R.id.tvParticip);
            tvParticip.setText(modelo.getSolicitud_usuario());

            ImageButton btnAdd = view.findViewById(R.id.btnAddParticipante);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(_dataListJugadores.size()==3){
                        Toast.makeText(CreacionPartida.this,"El grupo no puede tener más participantes",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    _dataListJugadores.add(modelo);
                    _adapterAmigosRemove.notifyDataSetChanged();
                    _dataListAmigosAdd.remove(modelo);
                    _adapterAmigosAdd.notifyDataSetChanged();
                }
            });


            return view;
        }
    }

    private class List_Adapter_Remove extends ArrayAdapter<LV_Usuario> {

        private List<LV_Usuario> mList;
        private Context mContext;
        private int resourceLayout;

        public List_Adapter_Remove(@NonNull Context context, int resource, @NonNull List<LV_Usuario> objects) {
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
            final LV_Usuario modelo = mList.get(position);

            TextView tvParticip= view.findViewById(R.id.tvParticipp);
            tvParticip.setText(modelo.getSolicitud_usuario());

            ImageButton btnRemove = view.findViewById(R.id.btnRemoveParticipante);

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _dataListJugadores.remove(modelo);
                    _adapterAmigosRemove.notifyDataSetChanged();
                    _dataListAmigosAdd.add(modelo);
                    _adapterAmigosAdd.notifyDataSetChanged();
                }
            });




            return view;
        }
    }
}
