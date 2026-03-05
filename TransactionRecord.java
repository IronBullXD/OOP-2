import java.time.LocalDateTime;
public class TransactionRecord {

    private String type;
    private String accountNumber;
    private double amount;
    private LocalDateTime date;

    public TransactionRecord(String type, String accountNumber, double amount) {
        this.type = type;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public void display() {
        System.out.println(type + " | Account: " + accountNumber +
                " | Amount: " + amount +
                " | Date: " + date);
    }
}
