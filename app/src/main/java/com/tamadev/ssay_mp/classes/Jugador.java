package com.tamadev.ssay_mp.classes;

public class Jugador {
    private String user;
    private int estado;

    public Jugador(String user, int estado) {
        this.user = user;
        this.estado = estado;
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
}
