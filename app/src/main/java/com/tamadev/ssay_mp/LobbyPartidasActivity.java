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
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.LV_Partidas;

import java.util.ArrayList;
import java.util.List;

import database.SQLiteDB;

public class LobbyPartidasActivity extends AppCompatActivity {

    private ListView _lvPartidas;
    private ArrayList<LV_Partidas> _dataListPartidas = new ArrayList<>();
    private List_Adapter _adapterPartidas;
    private SQLiteDB helper;
    private DatabaseReference dbref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_partidas);

        _lvPartidas = (ListView)findViewById(R.id.lvPartidas);
        _adapterPartidas = new List_Adapter(LobbyPartidasActivity.this,R.layout.item_row_partidas,_dataListPartidas);
        _adapterPartidas.setNotifyOnChange(true);

        _lvPartidas.setAdapter(_adapterPartidas);

        SearchPartidass();
    }
    public void SearchPartidass(){
        helper = new SQLiteDB(LobbyPartidasActivity.this,"db",null,1);
        dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(helper.GetDatoPerfil("Usuario")).child("Partidas");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Partidas) {
                for(DataSnapshot partida: Partidas.getChildren()){
                    CrearPartida _oPartida = Partidas.child(partida.getKey()).getValue(CrearPartida.class);
                    String _sEstadoPartida = "";
                    switch (_oPartida.getEstado()){
                        case 0:
                            _sEstadoPartida = "¿Aceptar Partida?";
                            break;
                        case 1:
                            _sEstadoPartida = "Tu turno";
                            break;
                        case 2:
                            _sEstadoPartida = "En espera";
                            break;
                        case 3:
                            _sEstadoPartida = "Has perdido";
                            break;
                        case 4:
                            _sEstadoPartida = "Has rechazado la partida";
                            break;
                        case 5:
                            _sEstadoPartida = "Has ganado la partida";
                            break;
                    }
                    LV_Partidas lvp = new LV_Partidas(_oPartida.getAnfitrion(),_sEstadoPartida,_oPartida.getJugadores().toString());
                    _dataListPartidas.add(lvp);
                }
                _adapterPartidas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void SearchPartidas(){
        helper = new SQLiteDB(LobbyPartidasActivity.this,"db",null,1);
        Cursor _cPartidas = helper.ConsultarPartidas();
        dbref = FirebaseDatabase.getInstance().getReference().child("Partidas");

        if(_cPartidas.moveToFirst()){
            do{
                CrearPartida partida = helper.getPartida(_cPartidas.getString(0));
                Cursor _cParticipantes = helper.ConsultarParticipantesPartidas(_cPartidas.getString(0));
                ArrayList<String> _dataListParticipantes = partida.getJugadores();
                String _sAnfitrion = partida.getAnfitrion();


                if (_dataListParticipantes.contains(helper.GetDatoPerfil("Usuario"))){
                    _dataListParticipantes.remove(helper.GetDatoPerfil("Usuario"));
                    _dataListParticipantes.add("tú");
                }

                String _sEstado = "";
                switch (_cPartidas.getInt(3)){
                    case 0:
                        _sEstado = "¿Aceptar Partida?";
                        break;
                    case 1:
                        _sEstado = "Pendiente";
                        break;
                    case 2:
                        _sEstado = "En curso - Esperando";
                        break;
                    case 3:
                        _sEstado = "Finalizada";
                        break;
                    case 4:
                        _sEstado = "Rechazada";
                        break;
                }
                if(_cPartidas.getInt(3)==2 && _cPartidas.getInt(2) == 1){
                    _sEstado = "Tu turno";
                }
                LV_Partidas lvP = new LV_Partidas("Partida de " + _sAnfitrion,_sEstado,_dataListParticipantes.toString());
                _dataListPartidas.add(lvP);
            }while (_cPartidas.moveToNext());
        }
        else{
            LV_Partidas lvP = new LV_Partidas("Sin partidas","---","---");
            _dataListPartidas.add(lvP);
        }

        _adapterPartidas = new List_Adapter(LobbyPartidasActivity.this,R.layout.item_row_partidas,_dataListPartidas);
        _lvPartidas.setAdapter(_adapterPartidas);
    }

    public void CrearPartidaNueva(View view){
        Intent i = new Intent(LobbyPartidasActivity.this,CreacionPartida.class);
        startActivity(i);
        finish();
    }

    private class List_Adapter extends ArrayAdapter<LV_Partidas> {

        private List<LV_Partidas> mList;
        private Context mContext;
        private int resourceLayout;

        public List_Adapter(@NonNull Context context, int resource, @NonNull List<LV_Partidas> objects) {
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
            final LV_Partidas modelo = mList.get(position);

            TextView tvPartidaCon = view.findViewById(R.id.tvPartidaCon);
            TextView tvIntegrantes = view.findViewById(R.id.tvIntegrantes);
            TextView tvEstado = view.findViewById(R.id.tvEstado);
            tvPartidaCon.setText("Partida de "+ modelo.get_sPartidaCon());
            tvEstado.setText(modelo.get_sEstado());
            tvIntegrantes.setText("Participantes: " + modelo.get_sParticipantes().replace("[","").replace("]",""));

            return view;
        }
    }
    public void BacktoMenu(View v){
        Intent i = new Intent(LobbyPartidasActivity.this,MenuPrincipalActivity.class);
        startActivity(i);
        finish();
    }
}
