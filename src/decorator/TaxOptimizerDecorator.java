package decorator;

import account.Account;
import account.InvestmentAccount;

public class TaxOptimizerDecorator extends AccountDecorator {
    public TaxOptimizerDecorator(Account wrapped) {
        super(wrapped);
        System.out.printf("%s: Tax optimizer applied.\n", wrapped.getAccountId());
    }


    @Override
    public void deposit(double amount) {
        super.deposit(amount);
    }


    @Override
    public String getDescription() {
        return wrapped.getDescription() + " + TaxOptimizer";
    }


    public void optimizedInvest(double percentReturn) {
        if (wrapped instanceof InvestmentAccount) {
            System.out.printf("%s: Running optimized investment routine...\n", wrapped.getAccountId());
            double bonus = 0.10 * percentReturn;
            ((InvestmentAccount) wrapped).invest(percentReturn + bonus);
            System.out.printf("%s: Tax-optimized extra return applied (%.2f%% bonus).\n", wrapped.getAccountId(), bonus);
        } else {
            System.out.println("Optimized invest is available only for InvestmentAccount.");
        }
    }
}
