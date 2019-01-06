package com.example.ibrahim.resfor;

public class Users {

    private String name;
    private String phone;
    private String email;
    private String password;
    private String type;

    public Users(/*String name, String phone, String email, String password, String type*/) {
        /*this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.type = type;*/
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }
}
