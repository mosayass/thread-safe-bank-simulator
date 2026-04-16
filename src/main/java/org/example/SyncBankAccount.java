package org.example;

public class SyncBankAccount {
    private double balance = 0.0;

    // Synchronized method: Implicit lock on the object
    public synchronized void deposit(double amount) {
        if (amount > 0) {
            System.out.println(Thread.currentThread().getName() + " is depositing $" + amount);
            balance += amount;
            System.out.println(" -> Deposit successful. New Balance: $" + balance);
        }
    }

    // Synchronized method: Implicit lock on the object
    public synchronized void withdraw(double amount) {
        System.out.println(Thread.currentThread().getName() + " is trying to withdraw $" + amount);

        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println(" -> Withdrawal successful. New Balance: $" + balance);
        } else {
            System.out.println(" -> Withdrawal FAILED. Insufficient funds for " + Thread.currentThread().getName());
        }
    }

    // Synchronized getter
    public synchronized double getBalance() {
        return balance;
    }
}