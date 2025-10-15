package demo;

import account.Account;
import account.InvestmentAccount;
import account.SavingsAccount;
import decorator.AccountDecorator;
import decorator.InsuranceDecorator;
import decorator.RewardPointsDecorator;
import decorator.TaxOptimizerDecorator;
import facade.BankingFacade;

public class Demo {
    public static void main(String[] args) {
        BankingFacade facade = new BankingFacade();


        Account savings = new SavingsAccount("Alice", 1000.0);
        savings = new RewardPointsDecorator(savings);
        savings = new InsuranceDecorator(savings);
        System.out.println("-- Savings account setup complete --");


        savings.deposit(200);
        System.out.println(savings.getDescription());


        Account investment = new InvestmentAccount("Bob", 5000.0);
        TaxOptimizerDecorator taxOptimized = new TaxOptimizerDecorator(investment);
        System.out.println("-- Investment account setup complete --");


        taxOptimized.deposit(500);
        taxOptimized.optimizedInvest(5.0); // base 5% return + tax optimizer bonus
        System.out.println(taxOptimized.getDescription());


        Account prefSavings = facade.openAccountWithBenefits("Charlie", "savings", 300.0);
        facade.depositTo(prefSavings, 50);


        Account safeInvestment = facade.investWithSafetyMode("Diana", 2000.0);
        if (safeInvestment instanceof TaxOptimizerDecorator) {
            ((TaxOptimizerDecorator) safeInvestment).optimizedInvest(4.0);
        } else if (safeInvestment instanceof AccountDecorator) {
            AccountDecorator dec = (AccountDecorator) safeInvestment;
            Account inner = dec.wrapped;
            if (inner instanceof TaxOptimizerDecorator) {
                ((TaxOptimizerDecorator) inner).optimizedInvest(4.0);
            }
        }


// Close accounts
        facade.closeAccount(prefSavings);
        facade.closeAccount(safeInvestment);


// Close manual accounts
        savings.close();
        taxOptimized.close();


        System.out.println("Demo finished.");
    }
}
