package org.example;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class LockBankAccount {
    private double balance = 0.0;

    // Explicit Lock: The "true" parameter enables fairness (First-In-First-Out)
    private final ReentrantLock lock = new ReentrantLock(true);

    public void deposit(double amount) {
        lock.lock(); // Explicitly acquire the lock
        try {
            if (amount > 0) {
                System.out.println(Thread.currentThread().getName() + " is depositing $" + amount);
                balance += amount;
                System.out.println(" -> Deposit successful. New Balance: $" + balance);
            }
        } finally {
            // Must always unlock in a finally block so the lock is released even if an error occurs
            lock.unlock();
        }
    }

    public void withdraw(double amount) {
        boolean isLocked = false;
        try {
            // Advanced feature: Try to get the lock for 50 milliseconds.
            // If it can't, it skips instead of freezing forever.
            isLocked = lock.tryLock(50, TimeUnit.MILLISECONDS);

            if (isLocked) {
                System.out.println(Thread.currentThread().getName() + " is trying to withdraw $" + amount);
                if (amount > 0 && balance >= amount) {
                    balance -= amount;
                    System.out.println(" -> Withdrawal successful. New Balance: $" + balance);
                } else {
                    System.out.println(" -> Withdrawal FAILED. Insufficient funds for " + Thread.currentThread().getName());
                }
            } else {
                // Advanced feature: Handling a busy system
                System.out.println(Thread.currentThread().getName() + " could not access the account. System is busy.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.out.println(Thread.currentThread().getName() + " was interrupted.");
        } finally {
            if (isLocked) {
                lock.unlock();
            }
        }
    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}