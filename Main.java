public class Main {
    public static void main(String[] args) {

        BankAccount alice = new BankAccount("Alice", Currency.RUB, 100_00, 50_000);
        BankAccount bob   = new BankAccount("Bob",   Currency.USD, 0,        1_000_000);

        System.out.println(alice);
        System.out.println(bob);
        System.out.println("-----");

        alice.deposit(25_00);
        alice.withdraw(10_00);
        System.out.println("После deposit/withdraw: " + alice);

        alice.transferTo(bob, 1_000_0);
        System.out.println("После перевода:");
        System.out.println("  " + alice);
        System.out.println("  " + bob);

        Balance usd = bob.getBalance();
        Balance eur = usd.convertTo(Currency.EUR);
        System.out.println("Конвертация: " + usd + " -> " + eur);
        System.out.println(bob.getBalance());

    }
}