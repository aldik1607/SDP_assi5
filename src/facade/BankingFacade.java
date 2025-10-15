package facade;

import account.Account;
import factory.AccountFactory;
import builder.AccountBuilder.AccountType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class BankingFacade {

    private final Map<String, Account> accounts = new HashMap<>();


    public Account openAccountWithBenefits(String owner, String type, double initialDeposit) {
        Account acc;
        if ("savings".equalsIgnoreCase(type)) {
            acc = AccountFactory.createSavingsWithBenefits(owner, initialDeposit);
        } else if ("investment".equalsIgnoreCase(type)) {
            acc = AccountFactory.createInvestmentSafetyMode(owner, initialDeposit);
        } else {
            acc = AccountFactory.createCustom(owner, AccountType.SAVINGS, initialDeposit, true, false, true);
        }
        accounts.put(acc.getAccountId(), acc);
        System.out.printf("Opened %s%n", acc.getDescription());
        return acc;
    }


    public Account investWithSafetyMode(String owner, double initialDeposit) {
        Account acc = AccountFactory.createInvestmentSafetyMode(owner, initialDeposit);
        accounts.put(acc.getAccountId(), acc);
        System.out.printf("Opened (safety-mode) %s%n", acc.getDescription());
        return acc;
    }


    public Account openCustom(String owner,
                              AccountType type,
                              double initialDeposit,
                              boolean withReward,
                              boolean withTaxOptimizer,
                              boolean withInsurance) {
        Account acc = AccountFactory.createWithBuilder(type, owner, initialDeposit, withReward, withTaxOptimizer, withInsurance);
        accounts.put(acc.getAccountId(), acc);
        System.out.printf("Opened custom %s%n", acc.getDescription());
        return acc;
    }


    public void closeAccount(Account acc) {
        if (acc == null) return;
        acc.close();
        accounts.remove(acc.getAccountId());
        System.out.printf("Account %s removed from facade registry.%n", acc.getAccountId());
    }

    public Optional<Account> findById(String id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public void depositTo(Account acc, double amount) {
        if (acc == null) return;
        acc.deposit(amount);
    }

    public void withdrawFrom(Account acc, double amount) {
        if (acc == null) return;
        acc.withdraw(amount);
    }
}
