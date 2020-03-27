package com.tamadev.ssay_mp.classes;

import java.util.ArrayList;

public class SolicitudPartida {
    private String id, idPartida, anfitrion;
    private ArrayList<UserFriendProfile> jugadores;
    public SolicitudPartida(String id, String idPartida, ArrayList<UserFriendProfile> jugadores, String anfitrion) {
        this.id = id;
        this.idPartida = idPartida;
        this.jugadores = jugadores;
        this.anfitrion = anfitrion;
    }
    public SolicitudPartida(String idPartida, ArrayList<UserFriendProfile> jugadores, String anfitrion) {

        this.idPartida = idPartida;
        this.jugadores = jugadores;
        this.anfitrion = anfitrion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(String idPartida) {
        this.idPartida = idPartida;
    }

    public String getAnfitrion() {
        return anfitrion;
    }

    public void setAnfitrion(String anfitrion) {
        this.anfitrion = anfitrion;
    }

    public ArrayList<UserFriendProfile> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<UserFriendProfile> jugadores) {
        this.jugadores = jugadores;
    }
}
