package com.tamadev.ssay_mp.tablas_db_despliegue;

public class Matches {
    private String ID;
    private int Cantidad_Jugadores, FlagTurno;

    public Matches() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getCantidad_Jugadores() {
        return Cantidad_Jugadores;
    }

    public void setCantidad_Jugadores(int cantidad_Jugadores) {
        Cantidad_Jugadores = cantidad_Jugadores;
    }

    public int getFlagTurno() {
        return FlagTurno;
    }

    public void setFlagTurno(int flagTurno) {
        FlagTurno = flagTurno;
    }
}
