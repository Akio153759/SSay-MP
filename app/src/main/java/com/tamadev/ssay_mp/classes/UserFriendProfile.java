package com.tamadev.ssay_mp.classes;

public class UserFriendProfile {
    private String userID;
    private String urlImageProfile;

    public UserFriendProfile(String userID, String urlImageProfile) {
        this.userID = userID;
        this.urlImageProfile = urlImageProfile;
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
}
