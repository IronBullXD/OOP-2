import java.util.Scanner;

public class Menu {

    private Bank bank;
    private Scanner scanner = new Scanner(System.in);

    public Menu(Bank bank) {
        this.bank = bank;
    }

    public void start() {
        int choice;

        do {
            System.out.println("\n===== BANKING SYSTEM =====");
            System.out.println("1. Create Savings Account");
            System.out.println("2. Create Current Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. View Accounts");
            System.out.println("7. Exit");
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
                    System.out.println("Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 7);
    }

    private void createSavings() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        System.out.print("Account Holder: ");
        String holder = scanner.nextLine();
        System.out.print("Initial Balance: ");
        double balance = scanner.nextDouble();

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
        bank.deposit(accNo, amount);
    }

    private void withdraw() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        bank.withdraw(accNo, amount);
    }

    private void transfer() {
        System.out.print("From Account: ");
        String from = scanner.nextLine();
        System.out.print("To Account: ");
        String to = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        bank.transfer(from, to, amount);
    }
}
