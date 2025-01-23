package io.workshop.enriquecimiento.models;

public class User {
    private String name;
    private String address;
    private String account;

    public User() {}

    public User(String name, String address, String account) {
        this.name = name;
        this.address = address;
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}