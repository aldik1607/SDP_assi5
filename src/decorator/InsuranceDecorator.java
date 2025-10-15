package decorator;

import account.Account;

public class InsuranceDecorator extends AccountDecorator {
    private final double annualInsuranceCost = 5.0; // flat example cost


    public InsuranceDecorator(Account wrapped) {
        super(wrapped);
        applyInsuranceFeeOnWrap();
    }


    private void applyInsuranceFeeOnWrap() {
        wrapped.withdraw(annualInsuranceCost);
        System.out.printf("%s: Insurance added (charged %.2f).\n", wrapped.getAccountId(), annualInsuranceCost);
    }


    @Override
    public void close() {
        System.out.printf("%s: Processing insurance cancellation...\n", wrapped.getAccountId());
        super.close();
    }


    @Override
    public String getDescription() {
        return wrapped.getDescription() + " + Insurance";
    }
}
