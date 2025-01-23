package io.workshop.enriquecimiento.models;

public class BankAccount {
    private String clabe;
    private String identity;
    private String account;

    public BankAccount() {}

    public BankAccount(String clabe, String identity, String account) {
        this.clabe = clabe;
        this.identity = identity;
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getClabe() {
        return clabe;
    }

    public void setClabe(String clabe) {
        this.clabe = clabe;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}