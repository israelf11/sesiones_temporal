package io.workshop.enriquecimiento.models;

public class RequestToService {
    private Balance balance;
    private BankAccount bankAccount;
    private User user;

    public RequestToService() {
    }

    public RequestToService(Balance balance, BankAccount bankAccount, User user) {
        this.balance = balance;
        this.bankAccount = bankAccount;
        this.user = user;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void log() {
        System.out.println("Name: " + this.user.getName() +
                " Clabe: " + this.bankAccount.getClabe() +
                " Balance: " + this.balance.getBalance()
        );
    }
}