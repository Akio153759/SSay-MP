package com.tamadev.ssay_mp.tablas_db_despliegue;

public class Friends {
    private String Usuario, Nombre;
    private int FlagAmigoConfirmado;

    public Friends() {
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getFlagAmigoConfirmado() {
        return FlagAmigoConfirmado;
    }

    public void setFlagAmigoConfirmado(int flagAmigoConfirmado) {
        FlagAmigoConfirmado = flagAmigoConfirmado;
    }
}
