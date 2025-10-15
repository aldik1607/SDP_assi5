package menu;

import account.Account;
import account.InvestmentAccount;
import decorator.InsuranceDecorator;
import decorator.TaxOptimizerDecorator;
import decorator.RewardPointsDecorator;
import facade.BankingFacade;
import factory.AccountFactory;
import builder.AccountBuilder;
import builder.AccountBuilder.AccountType;

import java.util.*;


public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, Account> accounts = new LinkedHashMap<>();
    private final BankingFacade facade = new BankingFacade();

    public static void main(String[] args) {
        new ConsoleMenu().run();
    }

    private void run() {
        printHeader();
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": createSavings(); break;
                case "2": createInvestment(); break;
                case "3": createSavingsWithBenefits(); break;
                case "4": createInvestmentWithSafety(); break;
                case "5": listAccounts(); break;
                case "6": depositToAccount(); break;
                case "7": withdrawFromAccount(); break;
                case "8": applyDecoratorMenu(); break;
                case "9": investOnAccount(); break;
                case "10": closeAccount(); break;
                case "11": createWithBuilder(); break;
                case "12": createWithFactoryCustom(); break;
                case "0": running = false; break;
                default:
                    System.out.println("Неверный выбор. Попробуй снова.");
            }
        }
        System.out.println("Выход. Пока!");
    }

    private void printHeader() {
        System.out.println("=== Banking & Investment Demo — Console Menu (Factory + Builder) ===");
        System.out.println("Цель: демонстрация паттернов Decorator, Facade, Builder и Factory");
        System.out.println();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("Меню:");
        System.out.println("1) Создать SavingsAccount (через Factory)");
        System.out.println("2) Создать InvestmentAccount (через Factory)");
        System.out.println("3) Открыть Savings с преимуществами (через Facade)");
        System.out.println("4) Открыть Investment в \"safety mode\" (через Facade)");
        System.out.println("5) Показать все аккаунты");
        System.out.println("6) Внести средства на счёт");
        System.out.println("7) Снять средства со счёта");
        System.out.println("8) Добавить декоратор (Insurance / TaxOptimizer / RewardPoints) к существующему счёту");
        System.out.println("9) Инвестировать (для InvestmentAccount или TaxOptimizerDecorator)");
        System.out.println("10) Закрыть счёт");
        System.out.println("11) Создать аккаунт через Builder (диалог)");
        System.out.println("12) Создать кастомный аккаунт через Factory.createCustom (флаги)");
        System.out.println("0) Выход");
        System.out.print("Выбери пункт: ");
    }

    private void createSavings() {
        System.out.print("Владелец: ");
        String owner = scanner.nextLine().trim();
        double init = askForDouble("Начальный депозит: ");
        Account acc = AccountFactory.createSavings(owner, init);
        accounts.put(acc.getAccountId(), acc);
        System.out.println("Создан (Factory): " + acc.getDescription());
    }

    private void createInvestment() {
        System.out.print("Владелец: ");
        String owner = scanner.nextLine().trim();
        double init = askForDouble("Начальный депозит: ");
        Account acc = AccountFactory.createInvestment(owner, init);
        accounts.put(acc.getAccountId(), acc);
        System.out.println("Создан (Factory): " + acc.getDescription());
    }

    private void createSavingsWithBenefits() {
        System.out.print("Владелец: ");
        String owner = scanner.nextLine().trim();
        double init = askForDouble("Начальный депозит: ");
        Account acc = facade.openAccountWithBenefits(owner, "savings", init);
        accounts.put(acc.getAccountId(), acc);
        System.out.println("Создан через фасад: " + acc.getDescription());
    }

    private void createInvestmentWithSafety() {
        System.out.print("Владелец: ");
        String owner = scanner.nextLine().trim();
        double init = askForDouble("Начальный депозит: ");
        Account acc = facade.investWithSafetyMode(owner, init);
        accounts.put(acc.getAccountId(), acc);
        System.out.println("Создан через фасад: " + acc.getDescription());
    }

    private void listAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("Нет доступных аккаунтов.");
            return;
        }
        System.out.println("Список аккаунтов:");
        for (Account a : accounts.values()) {
            System.out.printf("- %s : %s\n", a.getAccountId(), a.getDescription());
        }
    }

    private void depositToAccount() {
        Account a = chooseAccount();
        if (a == null) return;
        double amt = askForDouble("Сумма для депозита: ");
        a.deposit(amt);
        System.out.println("После депозита: " + a.getDescription());
    }

    private void withdrawFromAccount() {
        Account a = chooseAccount();
        if (a == null) return;
        double amt = askForDouble("Сумма для снятия: ");
        boolean ok = a.withdraw(amt);
        System.out.println(ok ? "Операция успешна." : "Не удалось снять (возможно недостаточно средств или счёт закрыт).");
        System.out.println("Текущее состояние: " + a.getDescription());
    }

    private void applyDecoratorMenu() {
        Account a = chooseAccount();
        if (a == null) return;

        System.out.println("Выбери декоратор:");
        System.out.println("1) Insurance");
        System.out.println("2) TaxOptimizer");
        System.out.println("3) RewardPoints");
        System.out.print("Выбор: ");
        String ch = scanner.nextLine().trim();
        Account wrapped;
        switch (ch) {
            case "1":
                wrapped = new InsuranceDecorator(a);
                accounts.put(wrapped.getAccountId(), wrapped);
                System.out.println("Insurance добавлен.");
                break;
            case "2":
                wrapped = new TaxOptimizerDecorator(a);
                accounts.put(wrapped.getAccountId(), wrapped);
                System.out.println("TaxOptimizer добавлен.");
                break;
            case "3":
                wrapped = new RewardPointsDecorator(a);
                accounts.put(wrapped.getAccountId(), wrapped);
                System.out.println("RewardPoints добавлен.");
                break;
            default:
                System.out.println("Неверный выбор.");
                return;
        }
        accounts.put(wrapped.getAccountId(), wrapped);
        System.out.println("Новый статус: " + wrapped.getDescription());
    }

    private void investOnAccount() {
        Account a = chooseAccount();
        if (a == null) return;
        double percent = askForDouble("Процент возврата (например, 5 для 5%): ");
        if (a instanceof TaxOptimizerDecorator) {
            ((TaxOptimizerDecorator) a).optimizedInvest(percent);
        } else if (a instanceof InvestmentAccount) {
            ((InvestmentAccount) a).invest(percent);
        } else {
            System.out.println("Инвестирование доступно только для InvestmentAccount или TaxOptimizerDecorator.");
        }
    }

    private void closeAccount() {
        Account a = chooseAccount();
        if (a == null) return;
        facade.closeAccount(a);
        accounts.remove(a.getAccountId());
        System.out.println("Счёт закрыт и удалён из локальном списке.");
    }


    private void createWithBuilder() {
        System.out.print("Владелец: ");
        String owner = scanner.nextLine().trim();
        double init = askForDouble("Начальный депозит: ");
        System.out.print("Тип (1 - SAVINGS, 2 - INVESTMENT): ");
        String t = scanner.nextLine().trim();
        AccountType type = "2".equals(t) ? AccountType.INVESTMENT : AccountType.SAVINGS;

        System.out.print("Добавить RewardPoints? (y/n): ");
        boolean reward = scanner.nextLine().trim().equalsIgnoreCase("y");
        System.out.print("Добавить TaxOptimizer? (y/n): ");
        boolean tax = scanner.nextLine().trim().equalsIgnoreCase("y");
        System.out.print("Добавить Insurance? (y/n): ");
        boolean ins = scanner.nextLine().trim().equalsIgnoreCase("y");

        AccountBuilder builder = new AccountBuilder()
                .type(type)
                .owner(owner)
                .initialDeposit(init);
        if (reward) builder.withRewardPoints();
        if (tax) builder.withTaxOptimizer();
        if (ins) builder.withInsurance();

        Account acc = builder.build();
        accounts.put(acc.getAccountId(), acc);
        System.out.println("Создан через Builder: " + acc.getDescription());
    }


    private void createWithFactoryCustom() {
        System.out.print("Владелец: ");
        String owner = scanner.nextLine().trim();
        double init = askForDouble("Начальный депозит: ");
        System.out.print("Тип (1 - SAVINGS, 2 - INVESTMENT): ");
        String t = scanner.nextLine().trim();
        AccountType type = "2".equals(t) ? AccountType.INVESTMENT : AccountType.SAVINGS;

        System.out.print("Добавить RewardPoints? (y/n): ");
        boolean reward = scanner.nextLine().trim().equalsIgnoreCase("y");
        System.out.print("Добавить TaxOptimizer? (y/n): ");
        boolean tax = scanner.nextLine().trim().equalsIgnoreCase("y");
        System.out.print("Добавить Insurance? (y/n): ");
        boolean ins = scanner.nextLine().trim().equalsIgnoreCase("y");

        Account acc = AccountFactory.createWithBuilder(type, owner, init, reward, tax, ins);
        accounts.put(acc.getAccountId(), acc);
        System.out.println("Создан через Factory+Builder: " + acc.getDescription());
    }


    private Account chooseAccount() {
        if (accounts.isEmpty()) {
            System.out.println("Сначала создайте аккаунт.");
            return null;
        }
        listAccounts();
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        if (!accounts.containsKey(id)) {
            System.out.println("Аккаунт с таким ID не найден.");
            return null;
        }
        return accounts.get(id);
    }

    private double askForDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line.replace(',', '.'));
            } catch (NumberFormatException ex) {
                System.out.println("Некорректное число. Попробуй ещё.");
            }
        }
    }
}
