package com.example.bhiwandicomadmin;

public class Users {
    public String name, phone, email, gender, image;

    public Users() {
    }

    public Users(String name, String phone, String email, String gender, String image) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.image = image;
    }

    public String getNamee() {
        return name;
    }

    public void setNamee(String name) {
        this.name = name;
    }

    public String getPhonee() {
        return phone;
    }

    public void setPhonee(String phone) {
        this.phone = phone;
    }

    public String getEmaill() {
        return email;
    }

    public void setEmaill(String email) {
        this.email = email;
    }

    public String getGenderr() {
        return gender;
    }

    public void setGenderr(String gender) {
        this.gender = gender;
    }

    public String getImagee() {
        return image;
    }

    public void setImagee(String image) {
        this.image = image;
    }
}
