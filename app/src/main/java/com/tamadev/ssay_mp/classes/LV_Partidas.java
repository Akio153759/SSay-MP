package com.tamadev.ssay_mp.classes;

public class LV_Partidas {
    private String _sPartidaCon;
    private String _sEstado;
    private String _sParticipantes;

    public LV_Partidas(String _sPartidaCon, String _sEstado, String _sParticipantes){
        this._sPartidaCon = _sPartidaCon;
        this._sEstado = _sEstado;
        this._sParticipantes = _sParticipantes;
    }

    public String get_sPartidaCon() {
        return _sPartidaCon;
    }

    public void set_sPartidaCon(String _sPartidaCon) {
        this._sPartidaCon = _sPartidaCon;
    }

    public String get_sEstado() {
        return _sEstado;
    }

    public void set_sEstado(String _sEstado) {
        this._sEstado = _sEstado;
    }

    public String get_sParticipantes() {
        return _sParticipantes;
    }

    public void set_sParticipantes(String _sParticipantes) {
        this._sParticipantes = _sParticipantes;
    }
}
