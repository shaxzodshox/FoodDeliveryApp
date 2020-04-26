package com.shlsoft.fooddelivery.model;

public class User {
    //We will get phone by getKey that is why we do not need to write phone in our data class
    private String Name;
    private String Password;

    public User(){}

    public User(String name, String password) {
        Name = name;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
