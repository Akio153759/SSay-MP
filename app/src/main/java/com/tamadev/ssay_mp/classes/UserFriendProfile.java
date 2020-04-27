package com.tamadev.ssay_mp.classes;

public class UserFriendProfile {
    private String userID;
    private String urlImageProfile;
    private int desempenio;
    private boolean online;

    public UserFriendProfile(String userID, String urlImageProfile, boolean online) {
        this.userID = userID;
        this.urlImageProfile = urlImageProfile;
        this.online = online;
    }

    public int getDesempenio() {
        return desempenio;
    }

    public void setDesempenio(int desempenio) {
        this.desempenio = desempenio;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUrlImageProfile() {
        return urlImageProfile;
    }

    public void setUrlImageProfile(String urlImageProfile) {
        this.urlImageProfile = urlImageProfile;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
