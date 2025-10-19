package adapter;

import java.util.Map;
import java.util.HashMap;


public class SimpleRatesProvider {
    private final Map<String, Double> rates = new HashMap<>();

    public SimpleRatesProvider() {
        rates.put("USD", 1.0);
        rates.put("EUR", 0.86);
        rates.put("KZT", 540.0);
        rates.put("RUB", 81.0);
    }

    public double getRate(String currency) {
        if (currency == null) return 1.0;
        return rates.getOrDefault(currency.toUpperCase(), 1.0);
    }

    public void setRate(String currency, double rate) {
        if (currency == null) return;
        rates.put(currency.toUpperCase(), rate);
    }
}
