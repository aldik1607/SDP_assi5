package account;

public class SavingsAccount extends BaseAccount {
    public SavingsAccount(String owner, double initialDeposit) {
        super(owner, initialDeposit);
    }

    public SavingsAccount(String owner, double initialDeposit, String explicitId) {
        super(owner, initialDeposit, explicitId);
    }

    @Override
    protected String generateId() {
        return String.format("SAV-%03d", SAVINGS_COUNTER.getAndIncrement());
    }

    @Override
    public String getDescription() {
        return "SavingsAccount{" + "id=" + accountId + ", owner='" + owner + '\'' + ", balance=" + String.format("%.2f", balance) + '}';
    }
}
