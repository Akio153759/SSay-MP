package com.tamadev.ssay_mp.classes;

public class RequestFriend {
    private String friendKey;
    private String userID;
    private String urlImageProfile;


    public RequestFriend(String friendKey, String userID, String urlImageProfile) {
        this.friendKey = friendKey;
        this.userID = userID;
        this.urlImageProfile = urlImageProfile;
    }

    public String getFriendKey() {
        return friendKey;
    }

    public void setFriendKey(String friendKey) {
        this.friendKey = friendKey;
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
