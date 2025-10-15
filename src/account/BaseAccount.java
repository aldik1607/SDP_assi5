package account;

import java.util.UUID;


public abstract class BaseAccount implements Account {
    protected final String accountId;
    protected final String owner;
    protected double balance;
    protected boolean closed = false;


    public BaseAccount(String owner, double initialDeposit) {
        this.accountId = UUID.randomUUID().toString();
        this.owner = owner;
        this.balance = initialDeposit;
    }


    @Override
    public String getAccountId() { return accountId; }


    @Override
    public String getOwner() { return owner; }


    @Override
    public double getBalance() { return balance; }


    @Override
    public void deposit(double amount) {
        if (closed) {
            System.out.println("Cannot deposit to closed account: " + accountId);
            return;
        }
        if (amount <= 0) return;
        balance += amount;
        System.out.printf("%s: Deposited %.2f, balance now %.2f\n", accountId, amount, balance);
    }


    @Override
    public boolean withdraw(double amount) {
        if (closed) {
            System.out.println("Cannot withdraw from closed account: " + accountId);
            return false;
        }
        if (amount <= 0 || amount > balance) return false;
        balance -= amount;
        System.out.printf("%s: Withdrew %.2f, balance now %.2f\n", accountId, amount, balance);
        return true;
    }


    @Override
    public void close() {
        closed = true;
        System.out.printf("%s: Account closed. Final balance: %.2f\n", accountId, balance);
    }
}
