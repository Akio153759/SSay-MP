package com.tamadev.ssay_mp.classes;

public class UsuarioEnFirebase {
    private String id, usuario;

    public UsuarioEnFirebase(String id, String usuario) {
        this.id = id;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
