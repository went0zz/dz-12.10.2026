import java.util.Objects;
public final class Balance {

    private final long amountInKopecks;
    private final Currency currency;

    public Balance(long amountInKopecks, Currency currency) {
        this.currency = Objects.requireNonNull(currency, "currency не может быть null");
        this.amountInKopecks = amountInKopecks;
    }

    public long getAmountInKopecks() {
        return amountInKopecks;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Balance add(long kopecks) {
        if (kopecks < 0) {
            throw new IllegalArgumentException("Нельзя прибавить отрицательную сумму.");
        }

        return new Balance(Math.addExact(amountInKopecks, kopecks), currency);
    }
    public Balance subtract(long kopecks) {
        if (kopecks < 0) {
            throw new IllegalArgumentException(
                    "Нельзя вычесть отрицательную сумму.");
        }
        if (kopecks > amountInKopecks) {
            throw new IllegalStateException(
                    "Недостаточно средств.");
        }
        return new Balance(amountInKopecks - kopecks, currency);
    }

    public Balance convertTo(Currency target) {
        Objects.requireNonNull(target, "target не может быть null");
        if (target == currency) {
            return this;
        }
        long rublesKopecks = Math.multiplyExact(amountInKopecks,
                currency.getPriceInRublesKopecks());
        long converted = rublesKopecks / target.getPriceInRublesKopecks();
        return new Balance(converted, target);
    }


    @Override
    public String toString() {
        long major = amountInKopecks / 100;
        long minor = amountInKopecks % 100;
        return String.format("%d.%02d %s (%s)",
                major, minor, currency.getSymbol(), currency.getCode());
    }
}