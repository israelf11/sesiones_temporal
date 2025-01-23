package io.workshop.orquestation.model;

public class CreditCardPayment {

  private String orderNumber;
  private Customer customer;
  private double amount;

  public CreditCardPayment() {
  }

  public CreditCardPayment(String orderNumber, Customer customer, double amount) {
    this.amount = amount;
    this.orderNumber = orderNumber;
    this.customer = customer;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}
