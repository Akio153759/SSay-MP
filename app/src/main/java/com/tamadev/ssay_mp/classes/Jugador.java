package com.tamadev.ssay_mp.classes;

public class Jugador {
    private String user;
    private int estado;
    private String urlImageUser;

    public Jugador(String user, int estado, String urlImageUser) {
        this.user = user;
        this.estado = estado;
        this.urlImageUser = urlImageUser;
    }
    public Jugador(){

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getUrlImageUser() {
        return urlImageUser;
    }

    public void setUrlImageUser(String urlImageUser) {
        this.urlImageUser = urlImageUser;
    }
}
