package com.tamadev.ssay_mp.herramientas_list_view;

import android.widget.Button;

public class LV_SolicitudesAmistad {
    private String Solicitud_usuario;


    public LV_SolicitudesAmistad(String solicitud_usuario) {
        Solicitud_usuario = solicitud_usuario;

    }

    public String getSolicitud_usuario() {
        return Solicitud_usuario;
    }

    public void setSolicitud_usuario(String solicitud_usuario) {
        Solicitud_usuario = solicitud_usuario;
    }


}
