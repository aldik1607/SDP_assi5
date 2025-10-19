package persistence;

import facade.BankingFacade;
import factory.AccountFactory;
import builder.AccountBuilder.AccountType;
import account.Account;
import decorator.RewardPointsDecorator;
import decorator.InsuranceDecorator;
import decorator.TaxOptimizerDecorator;

import java.io.*;
import java.util.*;


public class PersistenceManagerTxt {

    private static final String SEP = "\\|"; // regex for split
    private static final String OUTSEP = "|";
    private static final String DECSEP = ";";
    private static final String DEC_PARAM = ":";


    public static void saveAll(BankingFacade facade, String filePath) throws IOException {
        Collection<Account> accounts = facade.getAllAccounts();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(filePath))) {
            for (Account acc : accounts) {
                StringBuilder line = new StringBuilder();
                String id = acc.getAccountId();
                String owner = acc.getOwner();
                double balance = acc.getBalance();
                String baseType = acc.getClass().getSimpleName().toUpperCase();
                if (baseType.contains("SAVINGS")) baseType = "SAVINGS";
                else if (baseType.contains("INVESTMENT")) baseType = "INVESTMENT";

                line.append(escape(id)).append(OUTSEP)
                        .append(escape(owner)).append(OUTSEP)
                        .append(balance).append(OUTSEP)
                        .append(baseType).append(OUTSEP);

                List<String> decs = new ArrayList<>();

                Account current = acc;
                boolean progress = true;
                while (progress) {
                    progress = false;
                    try {
                        if (current instanceof RewardPointsDecorator) {
                            RewardPointsDecorator r = (RewardPointsDecorator) current;
                            decs.add("RewardPoints:" + r.getPoints());
                            current = r.getWrapped();
                            progress = true;
                        } else if (current instanceof TaxOptimizerDecorator) {
                            TaxOptimizerDecorator t = (TaxOptimizerDecorator) current;
                            decs.add("TaxOptimizer");
                            current = t.getWrapped();
                            progress = true;
                        } else if (current instanceof InsuranceDecorator) {
                            InsuranceDecorator ins = (InsuranceDecorator) current;
                            decs.add("Insurance");
                            current = ins.getWrapped();
                            progress = true;
                        }
                    } catch (Throwable e) {
                        // If getWrapped() isn't accessible, break to avoid infinite loop
                        break;
                    }
                }

                // Join decorators with DECSEP
                line.append(String.join(DECSEP, decs));

                w.write(line.toString());
                w.newLine();
            }
        }
    }


    public static void loadAll(BankingFacade facade, String filePath) throws IOException {
        File f = new File(filePath);
        if (!f.exists()) return;

        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(SEP, -1);
                if (parts.length < 5) continue;
                String id = unescape(parts[0]);
                String owner = unescape(parts[1]);
                double balance;
                try {
                    balance = Double.parseDouble(parts[2]);
                } catch (NumberFormatException ex) {
                    balance = 0.0;
                }
                String baseType = parts[3].trim().toUpperCase();
                String decPart = parts[4].trim();

                AccountType type = "INVESTMENT".equals(baseType) ? AccountType.INVESTMENT : AccountType.SAVINGS;

                Account acc = AccountFactory.createCustom(owner, type, 0.0, false, false, false);

                if (balance > 0) {
                    acc.deposit(balance);
                }

                if (!decPart.isEmpty()) {
                    String[] decs = decPart.split(DECSEP);
                    for (String d : decs) {
                        if (d == null || d.trim().isEmpty()) continue;
                        String dd = d.trim();
                        if (dd.startsWith("RewardPoints")) {
                            int pts = 0;
                            if (dd.contains(DEC_PARAM)) {
                                String[] kv = dd.split(DEC_PARAM, 2);
                                try { pts = Integer.parseInt(kv[1]); } catch (Exception ignored) {}
                            }
                            acc = new RewardPointsDecorator(acc);
                            try { ((RewardPointsDecorator) acc).setPoints(pts); } catch (Throwable ignored) {}
                        } else if ("TaxOptimizer".equalsIgnoreCase(dd)) {
                            acc = new TaxOptimizerDecorator(acc);
                        } else if ("Insurance".equalsIgnoreCase(dd)) {
                            acc = new InsuranceDecorator(acc);
                        } else {
                        }
                    }
                }

                facade.addAccount(acc);
            }
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("|", "\\|");
    }

    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\|", "|").replace("\\\\", "\\");
    }
}
