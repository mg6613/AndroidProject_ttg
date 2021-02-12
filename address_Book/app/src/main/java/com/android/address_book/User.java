package com.android.address_book;

public class User {

    String userName;
    String userEmail;
    String userPW;
    String userPhone;

    public User(String userName, String userEmail, String userPW, String userPhone) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPW = userPW;
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPW() {
        return userPW;
    }

    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
