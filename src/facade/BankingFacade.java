package facade;

import account.Account;
import account.InvestmentAccount;
import account.SavingsAccount;
import decorator.InsuranceDecorator;
import decorator.RewardPointsDecorator;
import decorator.TaxOptimizerDecorator;

import java.util.*;


public class BankingFacade {
    private final Map<String, Account> accounts = new HashMap<>();


    public Account openAccountWithBenefits(String owner, String type, double initialDeposit) {
        Account acc;
        if ("savings".equalsIgnoreCase(type)) {
            acc = new SavingsAccount(owner, initialDeposit);
            acc = new RewardPointsDecorator(acc);
            acc = new InsuranceDecorator(acc);
        } else if ("investment".equalsIgnoreCase(type)) {
            acc = new InvestmentAccount(owner, initialDeposit);
        } else {
            throw new IllegalArgumentException("Unknown account type: " + type);
        }
        accounts.put(acc.getAccountId(), acc);
        System.out.printf("Opened %s\n", acc.getDescription());
        return acc;
    }


    public Account investWithSafetyMode(String owner, double initialDeposit) {
        Account acc = new InvestmentAccount(owner, initialDeposit);
        acc = new TaxOptimizerDecorator(acc);
        acc = new InsuranceDecorator(acc);
        accounts.put(acc.getAccountId(), acc);
        System.out.printf("Opened (safety-mode) %s\n", acc.getDescription());
        return acc;
    }


    public void closeAccount(Account acc) {
        if (acc == null) return;
        acc.close();
        accounts.remove(acc.getAccountId());
        System.out.printf("Account %s removed from facade registry.\n", acc.getAccountId());
    }


    public Optional<Account> findById(String id) {
        return Optional.ofNullable(accounts.get(id));
    }


    public void depositTo(Account acc, double amount) { acc.deposit(amount); }


    public void withdrawFrom(Account acc, double amount) { acc.withdraw(amount); }
}