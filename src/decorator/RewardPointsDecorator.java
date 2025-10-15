package decorator;

import account.Account;

public class RewardPointsDecorator extends AccountDecorator {
    private int points = 0;
    private final double pointsPerDepositUnit = 0.1; // 0.1 point per money unit


    public RewardPointsDecorator(Account wrapped) {
        super(wrapped);
        System.out.printf("%s: Reward program enabled.\n", wrapped.getAccountId());
    }


    @Override
    public void deposit(double amount) {
        super.deposit(amount);
        int earned = (int) (amount * pointsPerDepositUnit);
        points += earned;
        System.out.printf("%s: Earned %d points (total %d).\n", wrapped.getAccountId(), earned, points);
    }


    @Override
    public String getDescription() {
        return wrapped.getDescription() + " + RewardPoints(" + points + ")";
    }


    public int getPoints() { return points; }
}