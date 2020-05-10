package com.tamadev.ssay_mp.classes;

public class IndexPuntaje {
    private int index;
    private int puntaje;

    public IndexPuntaje(int index, int puntaje) {
        this.index = index;
        this.puntaje = puntaje;
    }

    public IndexPuntaje() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
}
