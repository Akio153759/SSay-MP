package com.tamadev.ssay_mp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

public class CrearPartida {
    private int CantidadJugadores;
    private ArrayList<String> Jugadores;
    private ArrayList <String> Secuencia;
    private String ProximoJugador;
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public CrearPartida() {
    }

    public int getCantidadJugadores() {
        return CantidadJugadores;
    }

    public void setCantidadJugadores(int cantidadJugadores) {
        CantidadJugadores = cantidadJugadores;
    }

    public ArrayList<String> getJugadores() {
        return Jugadores;
    }

    public void setJugadores(ArrayList<String> jugadores) {
        Jugadores = jugadores;
    }

    public ArrayList<String> getSecuencia() {
        return Secuencia;
    }

    public void setSecuencia(ArrayList<String> secuencia) {
        Secuencia = secuencia;
    }

    public String getProximoJugador() {
        return ProximoJugador;
    }

    public void setProximoJugador(String proximoJugador) {
        ProximoJugador = proximoJugador;
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
}
