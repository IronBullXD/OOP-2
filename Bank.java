import java.util.ArrayList;

public class Bank implements Transaction {

    private ArrayList<Account> accounts = new ArrayList<>();
    private ArrayList<TransactionRecord> records = new ArrayList<>();

    public void addAccount(Account account) {
        accounts.add(account);
        System.out.println("Account created successfully.");
    }

    public Account findAccount(String accNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accNumber)) {
                return acc;
            }
        }
        return null;
    }

    public void generateStatement(String accNumber) {
        Account acc = findAccount(accNumber);
        if (acc != null) {
            BankStatement statement = new BankStatement(acc);
            statement.printStatement();
        } else {
            System.out.println("Account not found.");
        }
    }

    @Override
    public void deposit(Account account, double amount) {
        if (account != null) {
            account.deposit(amount);
            records.add(new TransactionRecord("Deposit", account.getAccountNumber(), amount));
        } else {
            System.out.println("Account not found.");
        }
    }

    @Override
    public void withdraw(Account account, double amount) {
        if (account != null) {
            account.withdraw(amount);
            records.add(new TransactionRecord("Withdraw", account.getAccountNumber(), amount));
        } else {
            System.out.println("Account not found.");
        }
    }

    @Override
    public void transfer(Account fromAccount, Account toAccount, double amount) {
        if (fromAccount != null && toAccount != null) {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            records.add(new TransactionRecord("Transfer", fromAccount.getAccountNumber(), amount));
            System.out.println("Transfer successful.");
        } else {
            System.out.println("Invalid account number.");
        }
    }

    public void showAllAccounts() {
        for (Account acc : accounts) {
            System.out.println("Account: " + acc.getAccountNumber() +
                    " | Holder: " + acc.getAccountHolder() +
                    " | Balance: " + acc.getBalance());
        }
    }
}