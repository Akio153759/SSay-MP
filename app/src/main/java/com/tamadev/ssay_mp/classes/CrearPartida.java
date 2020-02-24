package com.tamadev.ssay_mp.classes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

//Clase utilizada para crear el objeto de la partida y subirlo a Firebase

public class CrearPartida {
    private int cantidadJugadores;
    private ArrayList<Jugador> jugadores;
    private ArrayList <String> secuencia;
    private String proximoJugador;
    private String id;
    private String anfitrion;

    public int getEstado() {
        return Estado;
    }


    public void setEstado(int estado) {
        Estado = estado;
    }

    private int Estado;

    public String getID() {
        return id;
    }

    public void setID(String ID) {
        this.id = ID;
    }

    public CrearPartida() {
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }

    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public ArrayList<String> getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(ArrayList<String> secuencia) {
        this.secuencia = secuencia;
    }

    public String getProximoJugador() {
        return proximoJugador;
    }

    public void setProximoJugador(String proximoJugador) {
        this.proximoJugador = proximoJugador;
    }

    public String GenerarIDPartida(String _sTextoPlano){
        String _sIDFinal = "";

        try {
            String plaintext = _sTextoPlano;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            _sIDFinal = bigInt.toString(16);
// Now we need to zero pad it if you actually want the full 32 chars.
            while (_sIDFinal.length() < 32) {
                _sIDFinal = "0" + _sIDFinal;
            }
        }
        catch (Exception e){

        }

        return _sIDFinal;
    }

    public String getAnfitrion() {
        return anfitrion;
    }

    public void setAnfitrion(String anfitrion) {
        this.anfitrion = anfitrion;
    }
}
