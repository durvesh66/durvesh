
import java.util.*;

class User {
    private String UserId;
    private String Pin;
    private double Balance;
    private final List TransactionHistory;

    public User(String userId, String pin, double balance,List transactionHistory ) {
        this.UserId = userId;
        this.Pin = pin;
        this.Balance = balance;
        this.TransactionHistory = transactionHistory;

    }

    public String getUserId() {
        return UserId;
    }

    public String getPin() {
        return Pin;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        this.Balance = balance;
    }
    public void addTransaction(TransactionType type, double amount) {
        Transaction transaction = new Transaction(type, amount, new Date());
        TransactionHistory.add(transaction);
    }
    public List<Transaction> getTransactionHistory() {
        return TransactionHistory;
    }
}
class Transaction {
    private TransactionType Type;
    private double Amount;
    private Date Timestamp;

    public Transaction(TransactionType Type, double Amount, Date Timestamp) {
        this.Type = Type;
        this.Timestamp = Timestamp;
        this.Amount = Amount;
    }

    public TransactionType getType () {
        return Type;
    }
    public Date getTimestamp () {
        return Timestamp;
    }
    public double getAmount() {return Amount;}
}
enum TransactionType {
    WITHDRAWAL,
    DEPOSIT,
    TRANSFER
}

class ATMSystem {
    private static Map<String, User> userDatabase = new HashMap<>();
    private static User currentUser;

    public static void main(String[] args) {
        initializeUsers();
        showLoginMenu();
    }

    private static void initializeUsers() {
        // Populate user database with sample users
        userDatabase.put("123456", new User("123456", "1234", 1000.0,new ArrayList<>()));
        userDatabase.put("1234567", new User("1234567", "7654321", 1500.0,new ArrayList<>()));
        userDatabase.put("12345678", new User("12345678", "187654321", 1000.0,new ArrayList<>()));
        userDatabase.put("12345", new User("12345", "54321", 1000.0,new ArrayList<>()));
        userDatabase.put("1234", new User("1234", "4321", 1000.0,new ArrayList<>()));
        userDatabase.put("123", new User("123", "321", 1000.0,new ArrayList<>()));
        userDatabase.put("12", new User("12", "21", 1000.0,new ArrayList<>()));

    }

    private static void showLoginMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== ATM System =====");
        System.out.print("Enter User ID: ");
        String userId = scanner.next();
        System.out.print("Enter PIN: ");
        String pin = scanner.next();

        if (authenticateUser(userId, pin)) {
            showMainMenu();
        } else {
            System.out.println("Authentication failed. Exiting...");
        }
    }

    private static boolean authenticateUser(String userId, String pin) {


        User user = userDatabase.get(userId);
        if (user != null && user.getPin().equals(pin)) {
            currentUser = user; // Set currentUser upon successful authentication
            return true;
        }
        else {
            return false;
        }
    }
    private static void showMainMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. View Balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.println("6.Transaction History");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewBalance();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    transfer();
                    break;
                case 5:
                    System.out.println("Exiting ATM. Thank you!");
                    break;
                case 6:
                    viewTransactionHistory();
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 6);
    }

    private static void viewBalance() {
        System.out.println("Current Balance: $" + currentUser.getBalance());
    }

    private static void withdraw() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Amount to withdraw: $");
        double amount = scanner.nextDouble();

        if (amount > 0 && amount <= currentUser.getBalance()) {
            currentUser.setBalance(currentUser.getBalance() - amount);
            currentUser.addTransaction(TransactionType.WITHDRAWAL, amount);
            System.out.println("Withdrawal successful. Remaining Balance: $" + currentUser.getBalance());
        } else {
            System.out.println("Invalid Amount or insufficient funds.");
        }
    }

    private static void deposit() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Amount to deposit: $");
        double amount = scanner.nextDouble();

        if (amount > 0) {
            currentUser.setBalance(currentUser.getBalance() + amount);
            currentUser.addTransaction(TransactionType.DEPOSIT,amount);
            System.out.println("Deposit successful. New Balance: $" + currentUser.getBalance());
        } else {
            System.out.println("Invalid Amount.");
        }
    }

    private static void transfer() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter recipient's User ID: ");
        String recipientId = scanner.next();

        if (userDatabase.containsKey(recipientId)) {
            System.out.print("Enter Amount to transfer: $");
            double amount = scanner.nextDouble();

            if (amount > 0 && amount <= currentUser.getBalance()) {
                currentUser.setBalance(currentUser.getBalance() - amount);
                User recipient = userDatabase.get(recipientId);
                recipient.setBalance(recipient.getBalance() + amount);
                currentUser.addTransaction(TransactionType.TRANSFER,amount);
                System.out.println("Transfer successful. Remaining Balance: $" + currentUser.getBalance());
            } else {
                System.out.println("Invalid Amount or insufficient funds.");
            }
        } else {
            System.out.println("Recipient not found.");
        }
    }
    private static void viewTransactionHistory() {
        List<Transaction> transactions = currentUser.getTransactionHistory();

        System.out.println("\n===== Transaction History =====");
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getType() + ": $" + transaction.getAmount() +
                    " on " + transaction.getTimestamp());
        }
    }
}
