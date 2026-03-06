import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Menu {
    @FunctionalInterface
    private interface RepeatableAction {
        boolean execute();
    }

    private final Bank bank;
    private final Scanner scanner = new Scanner(System.in);
    private final AccountAuthentication auth = new AccountAuthentication();
    private final TransactionHistoryService historyService;

    // Default user/password for the assumed database.
    private final User currentUser = new User("admin", "password123");

    public Menu(Bank bank) {
        this.bank = bank;
        this.historyService = bank.getTransactionHistoryService();
    }

    public void start() {
        if (!loginPrompt()) {
            clearScreen();
            System.out.println("Authentication failed. Exiting system...");
            return;
        }

        int choice;
        do {
            clearScreen();
            showMainMenu();
            choice = readIntChoice("Choose option: ");
            clearScreen();

            switch (choice) {
                case 1:
                    createSavingsPage();
                    break;
                case 2:
                    createCurrentPage();
                    break;
                case 3:
                    depositPage();
                    break;
                case 4:
                    withdrawPage();
                    break;
                case 5:
                    transferPage();
                    break;
                case 6:
                    viewAccountsPage();
                    break;
                case 7:
                    generateStatementPage();
                    break;
                case 8:
                    transactionHistoryMenu();
                    break;
                case 0:
                    System.out.println("Logging out... Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    pauseForContinue();
            }

        } while (choice != 0);
    }

    private void createSavingsPage() {
        runRepeatablePage("CREATE SAVINGS ACCOUNT", this::createSavings, "Create another account");
    }
    private void createCurrentPage() {
        runRepeatablePage("CREATE CURRENT ACCOUNT", this::createCurrent, "Create another account");
    }
    private void depositPage() {
        runRepeatablePage("DEPOSIT", this::deposit, "New deposit");
    }
    private void withdrawPage() {
        runRepeatablePage("WITHDRAW", this::withdraw, "New withdrawal");
    }
    private void transferPage() {
        runRepeatablePage("TRANSFER", this::transfer, "New transfer");
    }
    private void viewAccountsPage() {
        runBackOnlyPage("ACCOUNTS", bank::showAllAccounts);
    }
    private void generateStatementPage() {
        runRepeatablePage("BANK STATEMENT", this::generateStatement, "View another statement");
    }
    private boolean loginPrompt() {
        clearScreen();
        printSectionHeader("SYSTEM LOGIN");
        System.out.print("Username: ");
        String inputUsername = scanner.nextLine();

        System.out.print("Password: ");
        String inputPassword = scanner.nextLine();

        // AccountAuthentication to verify.
        if (auth.login(currentUser, inputUsername, inputPassword)) {
            System.out.println("Login successful! Welcome, " + currentUser.getUsername() + ".");
            pauseForContinue();
            return true;
        } else {
            System.out.println("Invalid username or password.");
            pauseForContinue();
            return false;
        }
    }
    private boolean generateStatement() {
        String accNo = readTextOrBack("Account Number");
        if (accNo == null) {
            return false;
        }
        bank.generateStatement(accNo);
        return true;
    }
    private boolean createSavings() {
        String accNo = readTextOrBack("Account Number");
        if (accNo == null) {
            return false;
        }

        String holder = readTextOrBack("Account Holder");
        if (holder == null) {
            return false;
        }

        Double balance = readNonNegativeDoubleOrBack("Initial Balance");
        if (balance == null) {
            return false;
        }

        bank.addAccount(new SavingsAccount(accNo, holder, balance));
        return true;
    }
    private boolean createCurrent() {
        String accNo = readTextOrBack("Account Number");
        if (accNo == null) {
            return false;
        }

        String holder = readTextOrBack("Account Holder");
        if (holder == null) {
            return false;
        }

        Double balance = readNonNegativeDoubleOrBack("Initial Balance");
        if (balance == null) {
            return false;
        }

        bank.addAccount(new CurrentAccount(accNo, holder, balance));
        return true;
    }
    private boolean deposit() {
        String accNo = readTextOrBack("Account Number");
        if (accNo == null) {
            return false;
        }

        Double amount = readPositiveDoubleOrBack("Amount");
        if (amount == null) {
            return false;
        }

        Account account = bank.findAccount(accNo);
        if (account != null) {
            bank.deposit(account, amount);
        } else {
            System.out.println("Account not found.");
        }
        return true;
    }
    private boolean withdraw() {
        String accNo = readTextOrBack("Account Number");
        if (accNo == null) {
            return false;
        }

        Double amount = readPositiveDoubleOrBack("Amount");
        if (amount == null) {
            return false;
        }

        Boolean confirmed = confirmYesNoOrBack("Confirm withdrawal?");
        if (confirmed == null) {
            return false;
        }
        if (!confirmed) {
            System.out.println("Withdrawal cancelled.");
            return true;
        }

        Account account = bank.findAccount(accNo);
        if (account != null) {
            bank.withdraw(account, amount);
        } else {
            System.out.println("Account not found.");
        }
        return true;
    }
    private boolean transfer() {
        String from = readTextOrBack("From Account");
        if (from == null) {
            return false;
        }

        String to = readTextOrBack("To Account");
        if (to == null) {
            return false;
        }

        Double amount = readPositiveDoubleOrBack("Amount");
        if (amount == null) {
            return false;
        }

        Boolean confirmed = confirmYesNoOrBack("Confirm transfer?");
        if (confirmed == null) {
            return false;
        }
        if (!confirmed) {
            System.out.println("Transfer cancelled.");
            return true;
        }

        Account fromAccount = bank.findAccount(from);
        Account toAccount = bank.findAccount(to);
        if (fromAccount != null && toAccount != null) {
            bank.transfer(fromAccount, toAccount, amount);
        } else {
            System.out.println("Invalid account number.");
        }
        return true;
    }
    private void transactionHistoryMenu() {
        int choice;
        do {
            clearScreen();
            showTransactionHistoryMenu();
            choice = readIntChoice("Choose option: ");
            clearScreen();

            switch (choice) {
                case 1:
                    runBackOnlyPage("ALL TRANSACTIONS", historyService::showAllTransactions);
                    break;
                case 2:
                    runRepeatablePage("TRANSACTIONS BY ACCOUNT", this::viewByAccountNumber, "Check another account");
                    break;
                case 3:
                    filterTransactionsMenu();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice.");
                    pauseForContinue();
            }
        } while (choice != 0);
    }
    private boolean viewByAccountNumber() {
        String accNo = readTextOrBack("Account Number");
        if (accNo == null) {
            return false;
        }
        historyService.showTransactionsByAccount(accNo);
        return true;
    }
    private void filterTransactionsMenu() {
        int choice;
        do {
            clearScreen();
            showFilterTransactionsMenu();
            choice = readIntChoice("Choose option: ");
            clearScreen();

            switch (choice) {
                case 1:
                    runRepeatablePage("FILTER BY TYPE", this::filterByType, "Filter another type");
                    break;
                case 2:
                    runRepeatablePage("FILTER BY DATE RANGE", this::filterByDateRange, "Filter another date range");
                    break;
                case 3:
                    runRepeatablePage("FILTER BY MINIMUM AMOUNT", this::filterByMinimumAmount, "Filter another amount");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice.");
                    pauseForContinue();
            }
        } while (choice != 0);
    }
    private void runRepeatablePage(String title, RepeatableAction action, String repeatLabel) {
        int choice;
        do {
            clearScreen();
            printSectionHeader(title);
            printBackHint();
            boolean completed = action.execute();
            if (!completed) {
                return;
            }
            choice = showRepeatNavigation(repeatLabel);
        } while (choice == 1);
    }

    private void runBackOnlyPage(String title, Runnable action) {
        clearScreen();
        printSectionHeader(title);
        action.run();
        promptBack();
    }

    private int showRepeatNavigation(String repeatLabel) {
        while (true) {
            System.out.println();
            System.out.println("1. " + repeatLabel);
            System.out.println("0. Back");
            int choice = readIntChoice("Choose option: ");
            if (choice == 1 || choice == 0) {
                return choice;
            }
            System.out.println("Invalid choice.");
        }
    }

    private void promptBack() {
        System.out.print("\nPress Enter to go back...");
        scanner.nextLine();
    }

    private boolean filterByType() {
        String type = readTextOrBack("Enter type (Deposit/Withdraw/Transfer)");
        if (type == null) {
            return false;
        }
        historyService.showTransactionsByType(type);
        return true;
    }

    private boolean filterByDateRange() {
        LocalDate from = readDateOrBack("From date");
        if (from == null) {
            return false;
        }

        LocalDate to = readDateOrBack("To date");
        if (to == null) {
            return false;
        }

        if (to.isBefore(from)) {
            System.out.println("Invalid date range.");
            return true;
        }

        historyService.showTransactionsByDateRange(from, to);
        return true;
    }

    private boolean filterByMinimumAmount() {
        Double minAmount = readPositiveDoubleOrBack("Minimum amount");
        if (minAmount == null) {
            return false;
        }
        historyService.showTransactionsByMinAmount(minAmount);
        return true;
    }

    private void showMainMenu() {
        printSectionHeader("BANKING SYSTEM");
        System.out.println("1. Create Savings Account");
        System.out.println("2. Create Current Account");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Transfer");
        System.out.println("6. View Accounts");
        System.out.println("7. Generate Bank Statement");
        System.out.println("8. Transaction History");
        System.out.println("0. Exit");
    }

    private void showTransactionHistoryMenu() {
        printSectionHeader("TRANSACTION HISTORY");
        System.out.println("1. View All Transactions");
        System.out.println("2. View By Account Number");
        System.out.println("3. Filter Transactions");
        System.out.println("0. Back");
    }

    private void showFilterTransactionsMenu() {
        printSectionHeader("FILTER TRANSACTIONS");
        System.out.println("1. By Type");
        System.out.println("2. By Date Range");
        System.out.println("3. By Minimum Amount");
        System.out.println("0. Back");
    }

    private void printSectionHeader(String title) {
        System.out.println("===== " + title + " =====");
    }

    private int readIntChoice(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    private String readTextOrBack(String fieldLabel) {
        while (true) {
            System.out.print(fieldLabel + ": ");
            String input = scanner.nextLine().trim();
            if ("0".equals(input)) {
                return null;
            }
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(fieldLabel + " cannot be empty.");
        }
    }

    private Double readPositiveDoubleOrBack(String fieldLabel) {
        while (true) {
            System.out.print(fieldLabel + ": ");
            String input = scanner.nextLine().trim();
            if ("0".equals(input)) {
                return null;
            }
            try {
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    System.out.println(fieldLabel + " must be greater than zero.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a number.");
            }
        }
    }

    private Double readNonNegativeDoubleOrBack(String fieldLabel) {
        while (true) {
            System.out.print(fieldLabel + ": ");
            String input = scanner.nextLine().trim();
            if ("0".equals(input)) {
                return null;
            }
            try {
                double value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.println(fieldLabel + " cannot be negative.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a number.");
            }
        }
    }

    private LocalDate readDateOrBack(String fieldLabel) {
        while (true) {
            System.out.print(fieldLabel + " (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            if ("0".equals(input)) {
                return null;
            }
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }

    private Boolean confirmYesNoOrBack(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if ("0".equals(input)) {
                return null;
            }
            if ("y".equals(input) || "yes".equals(input)) {
                return true;
            }
            if ("n".equals(input) || "no".equals(input)) {
                return false;
            }
            System.out.println("Invalid input. Enter Y, N, or 0.");
        }
    }

    private void printBackHint() {
        System.out.println("Type 0 at any prompt to go back.\n");
    }

    private void pauseForContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void clearScreen() {
        String osName = System.getProperty("os.name", "").toLowerCase();
        try {
            ProcessBuilder clearCommand;
            if (osName.contains("win")) {
                clearCommand = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                clearCommand = new ProcessBuilder("clear");
            }

            clearCommand.inheritIO();
            Process process = clearCommand.start();
            if (process.waitFor() == 0) {
                return;
            }
        } catch (Exception ignored) {
            // Fall through to ANSI/spacer fallback.
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();
        for (int i = 0; i < 6; i++) {
            System.out.println();
        }
    }
}
