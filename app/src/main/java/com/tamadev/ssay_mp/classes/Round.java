package com.tamadev.ssay_mp.classes;

import java.util.HashMap;

public class Round {
    private HashMap<String,Integer> score;
    private boolean finalizada;

    public Round() {

    }

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
}
