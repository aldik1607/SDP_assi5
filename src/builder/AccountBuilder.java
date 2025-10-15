package builder;

import account.Account;
import account.SavingsAccount;
import account.InvestmentAccount;
import decorator.InsuranceDecorator;
import decorator.RewardPointsDecorator;
import decorator.TaxOptimizerDecorator;


public class AccountBuilder {
    public enum AccountType { SAVINGS, INVESTMENT }

    private AccountType type = AccountType.SAVINGS;
    private String owner = "Unknown";
    private double initialDeposit = 0.0;
    private boolean rewardPoints = false;
    private boolean insurance = false;
    private boolean taxOptimizer = false;

    public AccountBuilder type(AccountType t) { this.type = t; return this; }
    public AccountBuilder owner(String o) { this.owner = o; return this; }
    public AccountBuilder initialDeposit(double d) { this.initialDeposit = d; return this; }

    public AccountBuilder withRewardPoints() { this.rewardPoints = true; return this; }
    public AccountBuilder withInsurance() { this.insurance = true; return this; }
    public AccountBuilder withTaxOptimizer() { this.taxOptimizer = true; return this; }


    public Account build() {
        Account acc;
        if (type == AccountType.INVESTMENT) {
            acc = new InvestmentAccount(owner, initialDeposit);
        } else {
            acc = new SavingsAccount(owner, initialDeposit);
        }

        if (rewardPoints) {
            acc = new RewardPointsDecorator(acc);
        }
        if (taxOptimizer) {
            acc = new TaxOptimizerDecorator(acc);
        }
        if (insurance) {
            acc = new InsuranceDecorator(acc);
        }
        return acc;
    }
}
