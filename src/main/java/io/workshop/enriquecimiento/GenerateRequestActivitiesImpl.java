package io.workshop.enriquecimiento;

import io.workshop.enriquecimiento.models.Balance;
import io.workshop.enriquecimiento.models.BankAccount;
import io.workshop.enriquecimiento.models.User;


public class GenerateRequestActivitiesImpl implements GenerateRequestActivities {

    private int attemptCount = 0;

    @Override
    public Balance balanceActivity(String id) {
        System.out.println("Fetching balance. id:" + id);

        while (attemptCount < 3) {
            attemptCount++;
            throw new RuntimeException("Intento fallido #" + attemptCount);
        }

        Balance balance = new Balance("1876.00", "January-February", "1234567");
        return balance;
    }

    @Override
    public BankAccount bankAccountActivity(String id) {
        System.out.println("Fetching bankAccount. id:" + id);

        BankAccount bankAccount = new BankAccount("12345", "1236748", "1234567");
        return bankAccount;
    }

    @Override
    public User userActivity(String id) {
        System.out.println("Fetching user. id:" + id);

        User user = new User("Israel", "Evergreen 123", "1234567");
        return user;
    }
}