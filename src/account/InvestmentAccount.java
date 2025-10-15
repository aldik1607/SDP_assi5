package account;

public class InvestmentAccount extends BaseAccount {
    public InvestmentAccount(String owner, double initialDeposit) {
        super(owner, initialDeposit);
    }


    public void invest(double percentReturn) {
        if (closed) {
            System.out.println("Cannot invest on closed account: " + accountId);
            return;
        }
        double gain = balance * percentReturn / 100.0;
        balance += gain;
        System.out.printf("%s: Invested, return %.2f%% -> gain %.2f, balance now %.2f\n", accountId, percentReturn, gain, balance);
    }


    @Override
    public String getDescription() {
        return "InvestmentAccount{" + "id=" + accountId + ", owner='" + owner + '\'' + ", balance=" + String.format("%.2f", balance) + '}';
    }
}
