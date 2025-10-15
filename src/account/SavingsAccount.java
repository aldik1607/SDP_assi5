package account;

public class SavingsAccount extends BaseAccount {
    public SavingsAccount(String owner, double initialDeposit) {
        super(owner, initialDeposit);
    }


    @Override
    public String getDescription() {
        return "SavingsAccount{" + "id=" + accountId + ", owner='" + owner + '\'' + ", balance=" + String.format("%.2f", balance) + '}';
    }
}
