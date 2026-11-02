import java.util.Objects;

public final class BankAccount {


    private final String owner;
    private final Currency baseCurrency;

    private Balance balance;

    private final long withdrawalLimitKopecks;

    private boolean blocked;

    public BankAccount(String owner, Currency baseCurrency,
                       long initialAmountKopecks, long withdrawalLimitKopecks) {
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("owner не может быть пустым.");
        }
        if (withdrawalLimitKopecks <= 0) {
            throw new IllegalArgumentException(
                    "Лимит на снятие должен быть положительным.");
        }
        this.owner = owner;
        this.baseCurrency = Objects.requireNonNull(baseCurrency, "baseCurrency null");
        this.balance = new Balance(initialAmountKopecks, baseCurrency);
        this.withdrawalLimitKopecks = withdrawalLimitKopecks;
        this.blocked = false;
    }
    public String getOwner() { return owner; }
    public Currency getBaseCurrency() { return baseCurrency; }
    public Balance getBalance() { return balance; }
    public long getWithdrawalLimitKopecks() { return withdrawalLimitKopecks; }
    public boolean isBlocked() { return blocked; }

    public void block()   { this.blocked = true; }
    public void unblock() { this.blocked = false; }

    private void check() {
        if (blocked) {
            throw new IllegalStateException("Счёт заблокирован");
        }
    }

    public void deposit(long kopecks) {
        check();
        balance = balance.add(kopecks);
    }

    public void withdraw(long kopecks) {
        check();
        if (kopecks < 0) {
            throw new IllegalArgumentException("Сумма снятия отрицательна.");
        }
        if (kopecks > withdrawalLimitKopecks) {
            throw new IllegalStateException(
                    "Превышен лимит снятия.");
        }
        balance = balance.subtract(kopecks);
    }

    public void transferTo(BankAccount target, long amountKopecks) {
        Objects.requireNonNull(target, "target не может быть null.");
        if (target == this) {
            throw new IllegalArgumentException("Нельзя перевести на тот же счёт.");
        }
        check();
        target.check();
        if (amountKopecks <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть > 0.");
        }
        if (amountKopecks > withdrawalLimitKopecks) {
            throw new IllegalStateException(
                    "Превышен лимит перевода.");
        }

        Balance debit = new Balance(amountKopecks, baseCurrency);
        Balance credit = debit.convertTo(target.baseCurrency);

        balance = balance.subtract(amountKopecks);
        target.balance = target.balance.add(credit.getAmountInKopecks());
    }
    @Override 
    public String toString() {
        return String.format(
                "BankAccount{owner='%s', balance=%s, limit=%d коп., blocked=%s}", owner, balance, withdrawalLimitKopecks, blocked);
    }
}