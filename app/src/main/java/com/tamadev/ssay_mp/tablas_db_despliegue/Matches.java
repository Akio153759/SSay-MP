package com.tamadev.ssay_mp.tablas_db_despliegue;

public class Matches {
    private String id;
    private int cantidad_Jugadores, flagTurno, estado;

    public Matches() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCantidad_Jugadores() {
        return cantidad_Jugadores;
    }

    public void setCantidad_Jugadores(int cantidad_Jugadores) {
        this.cantidad_Jugadores = cantidad_Jugadores;
    }

    public int getFlagTurno() {
        return flagTurno;
    }

    public void setFlagTurno(int flagTurno) {
        this.flagTurno = flagTurno;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
