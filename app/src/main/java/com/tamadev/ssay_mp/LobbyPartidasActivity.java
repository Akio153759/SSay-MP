package com.tamadev.ssay_mp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.classes.CrearPartida;
import com.tamadev.ssay_mp.classes.Jugador;
import com.tamadev.ssay_mp.classes.LV_Usuario;
import com.tamadev.ssay_mp.classes.SolicitudPartida;

import java.util.ArrayList;
import java.util.List;

import database.SQLiteDB;

public class LobbyPartidasActivity extends AppCompatActivity {

    private ListView _lvPartidas, _lvSolicitudesPartidas;
    private ArrayList<CrearPartida> _dataListPartidas = new ArrayList<>();
    private ArrayList<SolicitudPartida> _dataListSolicitudes= new ArrayList<>();
    private ArrayList<SolicitudPartida> _listaSolicitudPartidas = new ArrayList<>();
    private List_Adapter _adapterPartidas;
    private List_Adapter_Solicitudes _adapterSolicitudes;
    private SQLiteDB helper = new SQLiteDB(LobbyPartidasActivity.this,"db",null,1);

    private DatabaseReference DBrefUsuario;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_partidas);

        _lvPartidas = (ListView)findViewById(R.id.lvPartidas);
        _adapterPartidas = new List_Adapter(LobbyPartidasActivity.this,R.layout.item_row_partidas,_dataListPartidas);
        _adapterPartidas.setNotifyOnChange(true);

        _lvPartidas.setAdapter(_adapterPartidas);


        _lvSolicitudesPartidas = (ListView)findViewById(R.id.lvSolicPartidas);
        _adapterSolicitudes = new List_Adapter_Solicitudes(this,R.layout.item_row_solicitud_partida,_dataListSolicitudes);
        _adapterSolicitudes.setNotifyOnChange(true);

        _lvSolicitudesPartidas.setAdapter(_adapterSolicitudes);

        DBrefUsuario = FirebaseDatabase.getInstance().getReference();
        DBrefUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot NodosPrincipal) {
                _dataListPartidas.clear();
                _dataListSolicitudes.clear();
                for(DataSnapshot nodo: NodosPrincipal.child("Usuarios").child(helper.GetUser()).getChildren()){
                    switch (nodo.getKey()){
                        case "SolicitudesPartidas":
                            for(DataSnapshot partida: nodo.getChildren()){
                                ArrayList<LV_Usuario> jugadores = new ArrayList<>();
                                for(DataSnapshot jugador: nodo.child(partida.getKey()).child("jugadores").getChildren()){
                                    jugadores.add(new LV_Usuario(jugador.getValue().toString()));
                                }
                                SolicitudPartida sp = new SolicitudPartida(partida.getKey(),nodo.child(partida.getKey()).child("idPartida").getValue().toString(),jugadores,nodo.child(partida.getKey()).child("anfitrion").getValue().toString());
                                _dataListSolicitudes.add(sp);
                            }

                            break;
                        case "Partidas":
                            for(DataSnapshot partida: nodo.getChildren()){
                                _dataListPartidas.add(NodosPrincipal.child("Partidas").child(partida.getValue().toString()).getValue(CrearPartida.class));

                            }
                            break;
                    }
                }
                _adapterPartidas.notifyDataSetChanged();
                _adapterSolicitudes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        _lvPartidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvEstado = view.findViewById(R.id.tvEstado);
                if (tvEstado.getText().toString().equals("Tu turno")){
                    Toast.makeText(LobbyPartidasActivity.this,"Jugando",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LobbyPartidasActivity.this,MainActivity.class);
                    i.putExtra("IDPartida",_dataListPartidas.get(position).getID());
                    startActivity(i);
                    finish();
                }
                else if(tvEstado.getText().toString().equals("Esperando el turno")){
                    Toast.makeText(LobbyPartidasActivity.this,"Espera tu turno",Toast.LENGTH_SHORT).show();
                }
                else if(tvEstado.getText().toString().equals("Has perdido la partida")){
                    Toast.makeText(LobbyPartidasActivity.this,"Has quedado fuera de esta partida",Toast.LENGTH_SHORT).show();
                }
                else if(tvEstado.getText().toString().equals("Has ganado la partida")){
                    Toast.makeText(LobbyPartidasActivity.this,"La partida ya ha sido finalizada", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    /*
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
    }*/
    /*
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
    }*/

    public void CrearPartidaNueva(View view){
        Intent i = new Intent(LobbyPartidasActivity.this,CreacionPartida.class);
        startActivity(i);
        finish();
    }

    private class List_Adapter extends ArrayAdapter<CrearPartida> {

        private List<CrearPartida> mList;
        private Context mContext;
        private int resourceLayout;

        public List_Adapter(@NonNull Context context, int resource, @NonNull List<CrearPartida> objects) {
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
            final CrearPartida modelo = mList.get(position);
            String _sEstado = "";
            String _sParticipantes = "";
            boolean _bPartidaFinalizada = false;
            for(Jugador jugador: modelo.getJugadores()){
                _sParticipantes += jugador.getUser() + ", ";
                if(jugador.getUser().equals(helper.GetUser())){
                    if (jugador.getEstado()==1){
                        _sEstado = "Esperando el turno";
                    }
                    else if(jugador.getEstado()==3){
                        _sEstado = "Has perdido la partida";
                        _bPartidaFinalizada = true;
                    }
                    else if(jugador.getEstado()==4){
                        _sEstado = "Has ganado la partida";
                        _bPartidaFinalizada = true;
                    }

                }
            }
            if(modelo.getProximoJugador().equals(helper.GetUser()) && !_bPartidaFinalizada){
                _sEstado = "Tu turno";
            }

            TextView tvPartidaCon = view.findViewById(R.id.tvPartidaCon);
            TextView tvIntegrantes = view.findViewById(R.id.tvIntegrantes);
            TextView tvEstado = view.findViewById(R.id.tvEstado);
            tvPartidaCon.setText("Partida de "+ modelo.getAnfitrion());
            tvEstado.setText(_sEstado);
            tvIntegrantes.setText("Participantes: " + _sParticipantes.substring(0,_sParticipantes.length()-2));

            return view;
        }
    }

    private class List_Adapter_Solicitudes extends ArrayAdapter<SolicitudPartida> {

        private List<SolicitudPartida> mList;
        private Context mContext;
        private int resourceLayout;

        public List_Adapter_Solicitudes(@NonNull Context context, int resource, @NonNull List<SolicitudPartida> objects) {
            super(context, resource, objects);
            this.mList = objects;
            this.mContext = context;
            this.resourceLayout = resource;

        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if(view == null){
                view = LayoutInflater.from(mContext).inflate(resourceLayout,null);
            }
            final SolicitudPartida modelo = mList.get(position);

            String _sParticipantes = "";
            for(LV_Usuario jugador: modelo.getJugadores()){
                _sParticipantes += jugador.getSolicitud_usuario().replace("{solicitud_usuario=","").replace("}","") + ", ";
            }

            TextView tvPartidaDe = view.findViewById(R.id.tvInvitador);
            TextView tvIntegrantes = view.findViewById(R.id.tvPart);
            Button btnPos = view.findViewById(R.id.btnPos);
            Button btnNeg = view.findViewById(R.id.btnNegat);

            btnPos.setBackgroundResource(R.drawable.btnconfirmar);
            btnNeg.setBackgroundResource(R.drawable.btncancelar);
            tvPartidaDe.setText("Partida de "+ modelo.getAnfitrion());

            tvIntegrantes.setText("Participantes: " + _sParticipantes.substring(0,_sParticipantes.length()-2));

            btnPos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBrefUsuario.child("Usuarios").child(helper.GetUser()).child("Partidas").push().setValue(modelo.getIdPartida());
                    DBrefUsuario.child("Usuarios").child(helper.GetUser()).child("SolicitudesPartidas").child(modelo.getId()).removeValue();
                    for(int i = 0; i<modelo.getJugadores().size();i++)
                    {
                        if (modelo.getJugadores().get(i).getSolicitud_usuario().replace("{solicitud_usuario=","").replace("}","").equals(helper.GetUser())){
                            DBrefUsuario.child("Partidas").child(modelo.getIdPartida()).child("jugadores").child(String.valueOf(i)).child("estado").setValue(1);

                            break;
                        }
                    }

                }
            });

            btnNeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBrefUsuario.child("Usuarios").child(helper.GetUser()).child("SolicitudesPartidas").child(modelo.getId()).removeValue();
                    for(int i = 0; i<modelo.getJugadores().size();i++)
                    {
                        if (modelo.getJugadores().get(i).getSolicitud_usuario().replace("{solicitud_usuario=","").replace("}","").equals(helper.GetUser())){
                            DBrefUsuario.child("Partidas").child(modelo.getIdPartida()).child("jugadores").child(String.valueOf(i)).child("estado").setValue(2);
                            int pasarTurno = i;
                            while (pasarTurno < modelo.getJugadores().size() - 1){
                                pasarTurno++;
                                if(_dataListPartidas.get(position).getJugadores().get(pasarTurno).getEstado()==0 ||_dataListPartidas.get(position).getJugadores().get(pasarTurno).getEstado()==1){
                                    DBrefUsuario.child("Partidas").child(modelo.getIdPartida()).child("proximoJugador").setValue(_dataListPartidas.get(position).getJugadores().get(pasarTurno).getUser());
                                    return;
                                }
                            }
                            pasarTurno = 0;
                            while (pasarTurno != i){
                                pasarTurno++;
                                if(_dataListPartidas.get(position).getJugadores().get(pasarTurno).getEstado()==0 ||_dataListPartidas.get(position).getJugadores().get(pasarTurno).getEstado()==1){
                                    DBrefUsuario.child("Partidas").child(modelo.getIdPartida()).child("proximoJugador").setValue(_dataListPartidas.get(position).getJugadores().get(pasarTurno).getUser());
                                    return;
                                }
                            }

                        }
                    }
                }
            });

            return view;
        }
    }
    public void BacktoMenu(View v){
        Intent i = new Intent(LobbyPartidasActivity.this,MenuPrincipalActivity.class);
        startActivity(i);
        finish();
    }
}
