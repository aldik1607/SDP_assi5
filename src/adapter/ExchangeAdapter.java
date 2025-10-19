package adapter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeAdapter implements CurrencyConverter {

    private final SimpleRatesProvider provider;
    private final int scale;

    public ExchangeAdapter(SimpleRatesProvider provider) {
        this(provider, 2);
    }

    public ExchangeAdapter(SimpleRatesProvider provider, int scale) {
        this.provider = provider;
        this.scale = scale;
    }

    @Override
    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (amount == null) throw new IllegalArgumentException("Amount is null");
        String from = (fromCurrency == null) ? "USD" : fromCurrency.toUpperCase();
        String to = (toCurrency == null) ? "USD" : toCurrency.toUpperCase();

        double fromRate = provider.getRate(from);
        double toRate = provider.getRate(to);

        if (fromRate == 0 || toRate == 0) throw new IllegalArgumentException("Unknown currency");

        // convert via USD
        BigDecimal usd = amount.divide(BigDecimal.valueOf(fromRate), scale + 6, RoundingMode.HALF_UP);
        BigDecimal result = usd.multiply(BigDecimal.valueOf(toRate));
        return result.setScale(scale, RoundingMode.HALF_UP);
    }
}
