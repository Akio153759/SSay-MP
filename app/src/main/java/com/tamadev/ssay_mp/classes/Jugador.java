package com.tamadev.ssay_mp.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Jugador implements Parcelable {
    private String user;
    private int estado;
    private int puntos;
    private String urlImageUser;

    public Jugador(String user, int estado, String urlImageUser, int puntos) {
        this.puntos = puntos;
        this.user = user;
        this.estado = estado;
        this.urlImageUser = urlImageUser;
    }
    public Jugador(){

    }

    protected Jugador(Parcel in) {
        user = in.readString();
        estado = in.readInt();
        puntos = in.readInt();
        urlImageUser = in.readString();
    }

    public static final Creator<Jugador> CREATOR = new Creator<Jugador>() {
        @Override
        public Jugador createFromParcel(Parcel in) {
            return new Jugador(in);
        }

        @Override
        public Jugador[] newArray(int size) {
            return new Jugador[size];
        }
    };

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user);
        dest.writeInt(estado);
        dest.writeInt(puntos);
        dest.writeString(urlImageUser);
    }
}
