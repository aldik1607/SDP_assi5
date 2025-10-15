package factory;

import account.Account;
import account.SavingsAccount;
import account.InvestmentAccount;
import decorator.InsuranceDecorator;
import decorator.RewardPointsDecorator;
import decorator.TaxOptimizerDecorator;
import builder.AccountBuilder;
import builder.AccountBuilder.AccountType;

public class AccountFactory {

    public static Account createSavings(String owner, double initialDeposit) {
        return new SavingsAccount(owner, initialDeposit);
    }


    public static Account createInvestment(String owner, double initialDeposit) {
        return new InvestmentAccount(owner, initialDeposit);
    }


    public static Account createSavingsWithBenefits(String owner, double initialDeposit) {
        Account acc = createSavings(owner, initialDeposit);
        acc = new RewardPointsDecorator(acc);
        acc = new InsuranceDecorator(acc);
        return acc;
    }


    public static Account createInvestmentSafetyMode(String owner, double initialDeposit) {
        Account acc = createInvestment(owner, initialDeposit);
        acc = new TaxOptimizerDecorator(acc);
        acc = new InsuranceDecorator(acc);
        return acc;
    }


    public static Account createCustom(String owner,
                                       AccountType type,
                                       double initialDeposit,
                                       boolean withReward,
                                       boolean withTaxOptimizer,
                                       boolean withInsurance) {
        Account acc;
        if (type == AccountType.INVESTMENT) {
            acc = createInvestment(owner, initialDeposit);
        } else {
            acc = createSavings(owner, initialDeposit);
        }
        if (withReward) acc = new RewardPointsDecorator(acc);
        if (withTaxOptimizer) acc = new TaxOptimizerDecorator(acc);
        if (withInsurance) acc = new InsuranceDecorator(acc);
        return acc;
    }


    public static Account createWithBuilder(AccountType type,
                                            String owner,
                                            double initialDeposit,
                                            boolean withReward,
                                            boolean withTaxOptimizer,
                                            boolean withInsurance) {
        AccountBuilder b = new AccountBuilder()
                .type(type)
                .owner(owner)
                .initialDeposit(initialDeposit);
        if (withReward) b.withRewardPoints();
        if (withTaxOptimizer) b.withTaxOptimizer();
        if (withInsurance) b.withInsurance();
        return b.build();
    }
}

