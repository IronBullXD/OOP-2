import java.time.LocalDateTime;

public class BankStatement {
    private Account account;
    private LocalDateTime dateGenerated;

    public BankStatement(Account account) {
        this.account = account;
        this.dateGenerated = LocalDateTime.now();
    }

    public void printStatement() {
        System.out.println("\n=== BANK STATEMENT ===");
        System.out.println("Date: " + dateGenerated);
        System.out.println("Account: " + account.getAccountNumber());
        System.out.println("Holder: " + account.getAccountHolder());
        System.out.println("Balance: $" + account.getBalance());
        System.out.println("=====================");
    }
}