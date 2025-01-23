package io.workshop.enriquecimiento.models;

public class Balance {
    private String balance;
    private String period;
    private String account;

    public Balance() {}

    public Balance(String balance, String period, String account) {
        this.balance = balance;
        this.period = period;
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}