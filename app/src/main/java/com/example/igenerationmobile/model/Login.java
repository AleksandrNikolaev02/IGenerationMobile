package com.example.igenerationmobile.model;

public class Login {
    private String email;
    private String password;
    private boolean login1 = false;;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public boolean getLogin1() {
        return login1;
    }

    public void setLogin1(boolean login1) {
        this.login1 = login1;
    }

}
