package com.tamadev.ssay_mp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Round implements Parcelable {
    private HashMap<String,Integer> score;
    private String escenario;
    private boolean finalizada;

    public Round() {

    }

    protected Round(Parcel in) {
        finalizada = in.readByte() != 0;
        score = (HashMap<String, Integer>)in.readSerializable();
        escenario = in.readString();
    }

    public static final Creator<Round> CREATOR = new Creator<Round>() {
        @Override
        public Round createFromParcel(Parcel in) {
            return new Round(in);
        }

        @Override
        public Round[] newArray(int size) {
            return new Round[size];
        }
    };

    public HashMap<String, Integer> getScore() {
        return score;
    }

    public void setScore(HashMap<String, Integer> score) {
        this.score = score;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public String getEscenario() {
        return escenario;
    }

    public void setEscenario(String escenario) {
        this.escenario = escenario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (finalizada ? 1 : 0));
        dest.writeSerializable(score);
        dest.writeString(escenario);
    }
}
