package io.workshop.trazabilidad;

public class Operation {
    private String name;
    private String operation;
    private double amount;

    public Operation() {
    }

    public Operation(String name, String operation, double amount) {
        this.name = name;
        this.operation = operation;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperation() {
        return operation;
    }

    public void setoperation(String operation) {
        this.operation = operation;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Name: " + getName() + " operation: " + getOperation() + " amount: " + getAmount();
    }
}
