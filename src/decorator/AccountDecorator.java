package decorator;

import account.Account;

public abstract class AccountDecorator implements Account {
    public final Account wrapped;


    public AccountDecorator(Account wrapped) {
        this.wrapped = wrapped;
    }


    @Override
    public String getAccountId() { return wrapped.getAccountId(); }


    @Override
    public String getOwner() { return wrapped.getOwner(); }


    @Override
    public double getBalance() { return wrapped.getBalance(); }


    @Override
    public void deposit(double amount) { wrapped.deposit(amount); }


    @Override
    public boolean withdraw(double amount) { return wrapped.withdraw(amount); }


    @Override
    public void close() { wrapped.close(); }


    @Override
    public String getDescription() { return wrapped.getDescription(); }
}
