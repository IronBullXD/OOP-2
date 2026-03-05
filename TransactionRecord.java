import java.time.LocalDateTime;
public class TransactionRecord {

    private String type;
    private String accountNumber;
    private String targetAccountNumber;
    private double amount;
    private LocalDateTime date;

    public TransactionRecord(String type, String accountNumber, double amount) {
        this(type, accountNumber, null, amount);
    }

    public TransactionRecord(String type, String accountNumber, String targetAccountNumber, double amount) {
        this.type = type;
        this.accountNumber = accountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }
    public String getType() {
        return type;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }
    public double getAmount() {
        return amount;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public boolean involvesAccount(String accountNumber) {
        if (accountNumber == null) {
            return false;
        }

        if (this.accountNumber.equals(accountNumber)) {
            return true;
        }

        return targetAccountNumber != null && targetAccountNumber.equals(accountNumber);
    }
    public void display() {
        String transferInfo = "";
        if (targetAccountNumber != null) {
            transferInfo = " -> " + targetAccountNumber;
        }
        System.out.println(type + " | Account: " + accountNumber + transferInfo +
                " | Amount: " + amount +
                " | Date: " + date);
    }
}
