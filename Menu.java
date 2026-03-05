import java.util.Scanner;

public class Menu {

    private Bank bank;
    private Scanner scanner = new Scanner(System.in);
    private AccountAuthentication auth = new AccountAuthentication();

    //Default pass ni siya and user for the "Assume database"
    private User currentUser = new User("admin", "password123");

    public Menu(Bank bank) {
        this.bank = bank;
    }

    public void start() {
        
        if (!loginPrompt()) {
            System.out.println("Authentication failed. Exiting system...");
            return; 
        }

        int choice;
        do {
            System.out.println("\n===== BANKING SYSTEM =====");
            System.out.println("1. Create Savings Account");
            System.out.println("2. Create Current Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. View Accounts");
            System.out.println("7. Generate Bank Statement");
            System.out.println("8. Exit");
            System.out.print("Choose option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    createSavings();
                    break;
                case 2:
                    createCurrent();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    withdraw();
                    break;
                case 5:
                    transfer();
                    break;
                case 6:
                    bank.showAllAccounts();
                    break;
                case 7:
                    generateStatement();
                    break;
                case 8:
                    System.out.println("Logging out... Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 8);
    }

    private boolean loginPrompt() {
        System.out.println("\n===== SYSTEM LOGIN =====");
        System.out.print("Username: ");
        String inputUsername = scanner.nextLine();
        
        System.out.print("Password: ");
        String inputPassword = scanner.nextLine();

        //AccountAuthentication  to verify 
        if (auth.login(currentUser, inputUsername, inputPassword)) {
            System.out.println("Login successful! Welcome, " + currentUser.getUsername() + ".");
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }

    private void generateStatement() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        bank.generateStatement(accNo);
    }

    private void createSavings() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        System.out.print("Account Holder: ");
        String holder = scanner.nextLine();
        System.out.print("Initial Balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); 
        bank.addAccount(new SavingsAccount(accNo, holder, balance));
    }

    private void createCurrent() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        System.out.print("Account Holder: ");
        String holder = scanner.nextLine();
        System.out.print("Initial Balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); 

        bank.addAccount(new CurrentAccount(accNo, holder, balance));
    }

    private void deposit() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); 
        Account account = bank.findAccount(accNo);
        if (account != null) {
            bank.deposit(account, amount);
        } else {
            System.out.println("Account not found.");
        }
    }

    private void withdraw() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        Account account = bank.findAccount(accNo);
        if (account != null) {
            bank.withdraw(account, amount);
        } else {
            System.out.println("Account not found.");
        }
    }

    private void transfer() {
        System.out.print("From Account: ");
        String from = scanner.nextLine();
        System.out.print("To Account: ");
        String to = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        Account fromAccount = bank.findAccount(from);
        Account toAccount = bank.findAccount(to);
        if (fromAccount != null && toAccount != null) {
            bank.transfer(fromAccount, toAccount, amount);
        } else {
            System.out.println("Invalid account number.");
        }
    }
}