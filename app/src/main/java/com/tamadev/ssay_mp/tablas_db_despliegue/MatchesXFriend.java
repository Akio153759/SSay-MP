package com.tamadev.ssay_mp.tablas_db_despliegue;

public class MatchesXFriend {
    private int ID;
    private String ID_Partida, Participante;

    public MatchesXFriend() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getID_Partida() {
        return ID_Partida;
    }

    public void setID_Partida(String ID_Partida) {
        this.ID_Partida = ID_Partida;
    }

    public String getParticipante() {
        return Participante;
    }

    public void setParticipante(String participante) {
        Participante = participante;
    }
}
