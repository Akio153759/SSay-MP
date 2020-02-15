package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamadev.ssay_mp.tablas_db_despliegue.Friends;
import com.tamadev.ssay_mp.tablas_db_despliegue.Matches;
import com.tamadev.ssay_mp.tablas_db_despliegue.MatchesXFriend;
import com.tamadev.ssay_mp.tablas_db_despliegue.Profile;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SQLiteDB extends SQLiteOpenHelper {
    public SQLiteDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String crearPerfil = "create table Perfil(Usuario VARCHAR(15), Nombre VARCHAR(30), Password VARCHAR(12), Email VARCHAR(30))";
        String crearAmigos = "create table Amigos(Usuario VARCHAR(15) primary key, Nombre VARCHAR(30), Estado Integer default 0)";
        String crearPartidas = "create table Partidas(" +
                               "ID VARCHAR(32) primary key," +
                               "Cantidad_Jugadores Integer," +
                               "FlagTurno Integer," +
                               "Estado Integer)";
        String crearPartidaXAmigos = "create table PartidaXAmigos(ID Integer primary key autoincrement," +
                                                                 "ID_Partida VARCHAR(20), " +
                                                                 "Participante VARCHAR(15), " +
                                                                 "foreign key(Participante) references Amigos(Usuario)," +
                                                                 "foreign key(ID_Partida) references Partidas(ID))";


        db.execSQL(crearPerfil);
        db.execSQL(crearAmigos);
        db.execSQL(crearPartidas);
        db.execSQL(crearPartidaXAmigos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Cursor ConsultarAmigos () throws SQLException {
        String query = "select * from Amigos where Estado = 2";
        Cursor c = this.getReadableDatabase().rawQuery(query,null);

        return c;
    }
    public Cursor ConsultarSolicitudesAmistad() throws SQLException {
        String query = "select * from Amigos where Estado = 1";
        Cursor c = this.getReadableDatabase().rawQuery(query,null);

        return c;
    }
    public void AgregarAmigo(String strUsuario, String strNombre, int intEstado){
        ContentValues valores = new ContentValues();
        valores.put("Usuario",strUsuario);
        valores.put("Nombre",strNombre);
        valores.put("Estado",intEstado);
        this.getWritableDatabase().insert("Amigos",null,valores);
    }
    public void Confirmar_Rechazar_SolicitudAmistad(String Usuario, int Estado ) throws SQLException{
        ContentValues datos = new ContentValues();
        datos.put("Estado",Estado);
        try{
            SQLiteDB.this.getWritableDatabase().update("Amigos",datos,"Usuario = '" + Usuario + "'",null);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean BorrarDatos(String usuario){
        boolean _bRes=false;
        try {
            SQLiteDB.this.getWritableDatabase().delete("Perfil","Usuario = '" + usuario+"'",null);
            SQLiteDB.this.getWritableDatabase().rawQuery("delete from Amigos",null);
            SQLiteDB.this.getWritableDatabase().rawQuery("delete from Partidas",null);
            SQLiteDB.this.getWritableDatabase().rawQuery("delete from PartidaXAmigos",null);
            _bRes = true;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return  _bRes;
    }

    public String GetDatoPerfil(String datoAObtener) throws SQLException{
        String _sDato = "";
        Cursor c = this.getReadableDatabase().rawQuery("select " + datoAObtener + " from Perfil",null);
        if(c.moveToFirst()){
            _sDato = c.getString(0);
        }
        else {
            _sDato = "";
        }

        return _sDato;
    }



    public String PushDB() throws SQLException{
        String _mensajeFinal = "";
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        Profile tablaPerfil = new Profile();
        Friends tablaAmigos = new Friends();
        Matches tablaPartidas = new Matches();
        MatchesXFriend tablaPartidaXAmigos = new MatchesXFriend();

        Cursor _cPerfil = this.getReadableDatabase().rawQuery("select * from Perfil",null);
        Cursor _cAmigos= this.getReadableDatabase().rawQuery("select * from Amigos",null);
        Cursor _cPartidas= this.getReadableDatabase().rawQuery("select * from Partidas",null);
        Cursor _cPartidaXAmigos = this.getReadableDatabase().rawQuery("select * from PartidaXAmigos",null);
        try {
            if (_cPerfil.moveToFirst()) {
                do {
                    tablaPerfil.setUsuario(_cPerfil.getString(0));
                    tablaPerfil.setNombre(_cPerfil.getString(1));
                    tablaPerfil.setPassword(_cPerfil.getString(2));
                    tablaPerfil.setEmail(_cPerfil.getString(3));
                    dbref.child(tablaPerfil.getUsuario()).child("Perfil").setValue(tablaPerfil);
                }while (_cPerfil.moveToNext());

            } else {
                _mensajeFinal = "Necesita crear el perfil para realizar el despliegue de datos";
                return _mensajeFinal;
            }

            if (_cAmigos.moveToFirst()) {
                do {
                    tablaAmigos.setUsuario(_cAmigos.getString(0));
                    tablaAmigos.setNombre(_cAmigos.getString(1));
                    tablaAmigos.setEstado(_cAmigos.getInt(2));

                    dbref.child(tablaPerfil.getUsuario()).child("Amigos").child(tablaAmigos.getUsuario()).setValue(tablaAmigos);
                } while (_cAmigos.moveToNext());

            } else {
                dbref.child(tablaPerfil.getUsuario()).child("Amigos").setValue("Sin Datos");
            }

            if (_cPartidas.moveToFirst()) {
                do {
                    tablaPartidas.setId(_cPartidas.getString(0));
                    tablaPartidas.setCantidad_Jugadores(_cPartidas.getInt(1));
                    tablaPartidas.setFlagTurno(_cPartidas.getInt(2));
                    tablaPartidas.setEstado(_cPartidas.getInt(3));

                    dbref.child(tablaPerfil.getUsuario()).child("Partidas").child(tablaPartidas.getId()).setValue(tablaPartidas);
                } while (_cPartidas.moveToNext());
            } else {
                dbref.child(tablaPerfil.getUsuario()).child("Partidas").setValue("Sin Datos");
            }

            if (_cPartidaXAmigos.moveToFirst()) {
                do {
                    tablaPartidaXAmigos.setId(_cPartidaXAmigos.getInt(0));
                    tablaPartidaXAmigos.setId_Partida(_cPartidaXAmigos.getString(1));
                    tablaPartidaXAmigos.setParticipante(_cPartidaXAmigos.getString(2));

                    dbref.child(tablaPerfil.getUsuario()).child("PartidasXAmigos").child(String.valueOf(tablaPartidaXAmigos.getId())).setValue(tablaPartidaXAmigos);
                } while (_cPartidaXAmigos.moveToNext());
            }
            else {
                dbref.child(tablaPerfil.getUsuario()).child("PartidasXAmigos").setValue("Sin Datos");
            }
            _mensajeFinal = "Datos desplegados exitosamente";
        }
        catch (Exception e){
            _mensajeFinal = "Ocurrió un error al desplegar datos";
        }
        return _mensajeFinal;
    }

    public void GetDB(final String Usuario, boolean LogueoInicial) throws SQLException{



        if(LogueoInicial == true){

        }
        else {

        }

        try {

            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(Usuario);


            // Obtengo los datos del nodo Perfil, elimino los datos de la tabla perfil y los inserto nuevamente
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot nodo : dataSnapshot.getChildren()) {
                        switch (nodo.getKey()) {
                            case "Perfil":
                                try{
                                    SQLiteDB.this.getWritableDatabase().delete("Perfil","Usuario='" +Usuario + "'",null);
                                }
                                catch (SQLException e){
                                    e.printStackTrace();
                                }

                                Profile nodoPerfil = dataSnapshot.child(nodo.getKey()).getValue(Profile.class);

                                SQLiteDB.this.RegistrarPerfil(nodoPerfil.getUsuario(), nodoPerfil.getNombre(), nodoPerfil.getPassword(), nodoPerfil.getEmail());
                                break;

                            case "Amigos":
                                try{
                                    SQLiteDB.this.getWritableDatabase().execSQL("delete from Amigos");
                                }
                                catch (SQLException e){
                                    e.printStackTrace();
                                }

                                for (DataSnapshot amigo : nodo.getChildren()) {
                                    Friends nodoAmigos = nodo.child(amigo.getKey()).getValue(Friends.class);
                                    SQLiteDB.this.AgregarAmigo(nodoAmigos.getUsuario(), nodoAmigos.getNombre(), nodoAmigos.getEstado());

                                }
                                break;
                            case "Partidas":
                                try{
                                    SQLiteDB.this.getWritableDatabase().execSQL("delete from Partidas");
                                }
                                catch (SQLException e){
                                    e.printStackTrace();
                                }
                                for (DataSnapshot partida : nodo.getChildren()) {
                                    Matches nodoPartida = nodo.child(partida.getKey()).getValue(Matches.class);
                                    SQLiteDB.this.CrearPartida(nodoPartida.getId(), nodoPartida.getCantidad_Jugadores(), nodoPartida.getFlagTurno(), nodoPartida.getEstado());
                                }
                                break;
                            case "PartidasXAmigos":
                                try{
                                    SQLiteDB.this.getWritableDatabase().execSQL("delete from PartidaXAmigos");
                                }
                                catch (SQLException e){
                                    e.printStackTrace();
                                }
                                for (DataSnapshot relacion : nodo.getChildren()) {
                                    MatchesXFriend nodoPartidaXAmigos = nodo.child(relacion.getKey()).getValue(MatchesXFriend.class);

                                    ContentValues datosRelaciones = new ContentValues();

                                    datosRelaciones.put("ID", nodoPartidaXAmigos.getId());
                                    datosRelaciones.put("ID_Partida", nodoPartidaXAmigos.getId_Partida());
                                    datosRelaciones.put("Participante", nodoPartidaXAmigos.getParticipante());

                                    SQLiteDB.this.getWritableDatabase().insert("PartidaXAmigos", null, datosRelaciones);
                                }
                                break;
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        catch (Exception e){
            System.out.println(e);
        }


    }

    public void RegistrarPerfil(String strUsuario, String strNombre, String strPassword, String strEmail){
        ContentValues valores = new ContentValues();
        valores.put("Usuario",strUsuario);
        valores.put("Nombre",strNombre);
        valores.put("Password",strPassword);
        valores.put("Email",strEmail);

        this.getWritableDatabase().insert("Perfil",null,valores);
    }

    public static boolean copiaBD(String from, String to) {
        /*
        ------ Asi deben definirse los parametros para copiar la db -------

        String pathDB = getDatabasePath("db").toString();
        String to = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + getPackageName() + "/" + "dbBK";
         */
        boolean result = false;
        try{
            File dir = new File(to.substring(0, to.lastIndexOf('/')));
            dir.mkdirs();
            File tof = new File(dir, to.substring(to.lastIndexOf('/') + 1));
            int byteread;
            File oldfile = new File(from);
            if(oldfile.exists()){
                InputStream inStream = new FileInputStream(from);
                FileOutputStream fs = new FileOutputStream(tof);
                byte[] buffer = new byte[1024];
                while((byteread = inStream.read(buffer)) != -1){
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
            result = true;
        }catch (Exception e){
            Log.e("copyFile", "Error copiando archivo: " + e.getMessage());
        }
        return result;
    }
    public void CrearPartida(String ID, int CantidadJugadores, int FlagTurno, int Estado){
        ContentValues datosDePartida = new ContentValues();
        datosDePartida.put("ID", ID);
        datosDePartida.put("Cantidad_Jugadores",CantidadJugadores);
        datosDePartida.put("FlagTurno",FlagTurno);
        datosDePartida.put("Estado", Estado);

        this.getWritableDatabase().insert("Partidas",null,datosDePartida);
    }
    public void CrearRelacionPartidaAmigos(String ID_Partida,String Participante){
        ContentValues datos = new ContentValues();
        datos.put("ID_Partida", ID_Partida);
        datos.put("Participante", Participante);
        this.getWritableDatabase().insert("PartidaXAmigos",null,datos);
    }
}
