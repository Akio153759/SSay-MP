package com.tamadev.ssay_mp.classes;

import android.widget.Button;

public class LV_Usuario {
    private String Solicitud_usuario;


    public LV_Usuario(String solicitud_usuario) {
        Solicitud_usuario = solicitud_usuario;

    }

    public String getSolicitud_usuario() {
        return Solicitud_usuario;
    }

    public void setSolicitud_usuario(String solicitud_usuario) {
        Solicitud_usuario = solicitud_usuario;
    }


}
