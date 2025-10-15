package account;

public interface Account {
    String getAccountId();
    String getOwner();
    double getBalance();
    void deposit(double amount);
    boolean withdraw(double amount);
    String getDescription();
    void close();
}
