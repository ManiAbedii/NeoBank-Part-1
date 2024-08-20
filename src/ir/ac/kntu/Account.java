package ir.ac.kntu;

import java.time.Instant;
import java.util.*;

public class Account {

    //Ordinary Fields:
    private String accountNumber;
    private double balance;
    private List<Transaction> recentTransacs = new ArrayList<>();
    private List<Receipt> trfrReceipts = new ArrayList<>();
    private List<String> recentTfedAccs = new ArrayList<>();

    public Account(String phoneNum) {
        this.accountNumber = phoneNum + "9306";
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        balance = Math.round(balance*100);
        balance /= 100.00;
        return (balance);
    }

    public void showBalance() {
        System.out.println("Your Balance is currently " + Colors.BLUE + getBalance() + Colors.RESET + "$\n");
    }

    public List<Transaction> getrecentTransacs() {
        return new ArrayList<>(recentTransacs);
    }

    public void addTransaction(Transaction transaction) {
        recentTransacs.add(transaction);
    }

    public void addReceipt(Receipt receipt) {
        trfrReceipts.add(receipt);
    }

    public void addToRecentTfed(String accNum) {
        recentTfedAccs.add(accNum);
    }

    //Deposit:
    public void deposit(SimpleUser user) {
        double deposit = getAmount();
        if (deposit <= 0) {
            System.out.println(Colors.RED + "Deposited amount have to be a positive amount");
            System.out.println("Unsuccessful Deposit\n" + Colors.RESET);
            return;
        }
        setBalance(getBalance() + deposit);
        System.out.println(Colors.GREEN + "The amount of " + deposit + "$ was successfully deposited into your account\n" + Colors.RESET);
        Transaction transaction = new Transaction(Type.DEPOSIT, deposit);
        user.getAccount().addTransaction(transaction);
    }

    private double getAmount() {
        String input;
        double deposit = 0.0;
        boolean flag = true;
        System.out.print("Enter the amount you are willing to add to your balance($): ");
        do {
            input = Helper.SCANNER.nextLine();
            try {
                deposit = Double.parseDouble(input);
                flag = false;
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED + "Enter the amount in numbers($):");
            }
        } while (flag);
        return deposit;
    }

    //Transfer:
    public void transfer(SimpleUser user) {
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Transfer" + Colors.RESET);
        boolean[] trueOrFalse = new boolean[1]; //:)

        SimpleUser destUser = chooseDest(user, trueOrFalse);
        checkDestUser(user, destUser);
        boolean throughContacts = trueOrFalse[0];

        double transferAmount = getAndCheckTransferAmount();
        double trfrAmountPlusFee = transferAmount * 1.001;

        checkBalance(user, trfrAmountPlusFee);
        checkTransferConfirmed(user, destUser, transferAmount);

        setUserNDestUserBalance(user, destUser, transferAmount);

        String nameInReceipt;
        if (throughContacts) {
            List<Contact> contacts = user.getContacts();
            nameInReceipt = returnContactNameOfUser(contacts, destUser);
        } else {
            nameInReceipt = destUser.getFirstName() + " " + destUser.getLastName();
        }

        Receipt receipt = new Receipt(transferAmount, getAccountNumber(), destUser.getAccount().getAccountNumber(), nameInReceipt);
        Transaction transaction = new Transaction(Type.TRANSFER, transferAmount, destUser.getAccount().getAccountNumber(), nameInReceipt);

        addTransactionReceiptTransfer(user, destUser, transaction, receipt);

        transaction = new Transaction(Type.RECEIVE, transferAmount);
        destUser.getAccount().addTransaction(transaction);

        System.out.println(Colors.GREEN + "Successful Transfer\n" + Colors.RESET + receipt);
        UserMenus.usersFeatures(user.getPhoneNum());
    }

    private void checkDestUser(SimpleUser user, SimpleUser destUser) {
        if (!destUser.getAuthenStatus().equals(AuthenticationStatus.OK.getDisplayName())) {
            System.out.println(Colors.RED + "Your destination account owner isn't verified\n" + Colors.RESET);
            transfer(user);
        }
        if (user.equals(destUser)) {
            System.out.println(Colors.RED + "Seriously? (az in jib be oon jib?)\n" + Colors.RESET);
            transfer(user);
        }
    }

    private void addTransactionReceiptTransfer(SimpleUser user, SimpleUser destUser, Transaction transaction, Receipt receipt) {
        user.getAccount().addTransaction(transaction);
        user.getAccount().addReceipt(receipt);
        user.getAccount().addToRecentTfed(destUser.getAccount().getAccountNumber());
    }

    private void setUserNDestUserBalance(SimpleUser user, SimpleUser destUser, double transferAmount) {
        user.getAccount().setBalance(getBalance() - transferAmount*1.001);
        destUser.getAccount().setBalance(destUser.getAccount().getBalance() + transferAmount);
    }

    private void checkBalance(SimpleUser user, double amount) {
        if (amount > user.getAccount().getBalance()) {
            System.out.println(Colors.RED + "Account Balance is Insufficient\n" + Colors.RESET);
            transfer(user);
        }
    }

    private SimpleUser chooseDest(SimpleUser user, boolean[] trueOrFalse) {
        disTransferMethods();
        int transferChoice = Helper.returnChoice(1, 4);
        return handleDestChoose(transferChoice, user, trueOrFalse);
    }

    private SimpleUser handleDestChoose(int transferChoice, SimpleUser user, boolean[] trueOrFalse) {
        switch (transferChoice) {
            case 1 -> {
                return findAccountOwner(returningAccNum(user));
            }
            case 2 -> {
                return findAccountOwner(chooseAccNum(user));
            }
            case 3 -> {
                return transferViaContact(user, trueOrFalse);
            }
            case 4 -> UserMenus.usersFeatures(user.getPhoneNum());
            default -> System.out.println();
        }
        return null;    }

    private SimpleUser transferViaContact(SimpleUser user, boolean[] trueOrFalse) {
        checkContactEnableAndEmpty(user);
        printAllContacts(user);

        System.out.println("Choose a Contact to Proceed (or enter '0' to " + Colors.RED + "Exit" + Colors.RESET + ")\n");
        int contactIndex = Helper.returnChoice(0, user.getContacts().size()) - 1;
        if (contactIndex == -1) {
            transfer(user);
        }

        Contact contact = user.getContacts().get(contactIndex);
        SimpleUser contactUser = BankUsersDataBase.getUser(contact.getPhoneNum());
        if (!contactUser.isContactsEnabled()) {
            System.out.println(Colors.RED + "Your Contact's Contacts Panel is Disabled" + Colors.RESET);
            transfer(user);
        }
        if (!alsoAContact(user, contactUser)) {
            System.out.println(Colors.RED + "You have to be also their contact to transfer" + Colors.RESET);
            transfer(user);
        }
        trueOrFalse[0] = true;
        return contactUser;
    }

    private void printAllContacts(SimpleUser user) {
        System.out.println("Contacts: ");
        int counter = 0;
        for (Contact contact : user.getContacts()) {
            counter++;
            System.out.println(counter + ". \n" + contact);
            System.out.println();
        }
    }

    private void checkContactEnableAndEmpty(SimpleUser user) {
        if (!user.isContactsEnabled()) {
            System.out.println(Colors.RED + "Enable Contacts via Settings to Use this Feature\n" + Colors.RESET);
            transfer(user);
        }
        if (user.getContacts().isEmpty()) {
            System.out.println(Colors.RED + "Contacts' list is empty\n" + Colors.RESET);
            transfer(user);
        }
    }

    private String chooseAccNum(SimpleUser user) {
        if (recentTfedAccs.isEmpty()) {
            System.out.println(Colors.RED + "This list is empty!" + Colors.RESET);
            transfer(user);
        }
        System.out.println("Recent Transferred Accounts: ");
        disRecentAccounts();
        System.out.println("Choose an Account Number To Proceed (or enter '0' to " + Colors.RED + "Exit" + Colors.RESET + "):");
        int accIndex = Helper.returnChoice(0, recentTfedAccs.size()) - 1;
        if (accIndex == -1) {
            transfer(user);
        }
        return recentTfedAccs.get(accIndex);
    }

    private String returningAccNum(SimpleUser user) {
        System.out.println("Enter the Destination Account Number");
        String accNum = getAndCheckAccNum();
        if (!isAccNumValid(accNum)) {
            System.out.println(Colors.RED + "Account Number is Invalid" + Colors.RESET);
            transfer(user);
        }
        return accNum;
    }

    private void disTransferMethods() {
        System.out.println("How are you willing to choose the destination account?");
        System.out.println("1. " + Colors.BLUE + "Destination Account Number" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "Choose From Recent Transfers" + Colors.RESET);
        System.out.println("3. " + Colors.BLUE + "Choose From Contacts" + Colors.RESET);
        System.out.println("4. " + Colors.RED + "Exit\n" + Colors.RESET);
    }

    private String getAndCheckAccNum() {
        String accNum;
        do {
            System.out.print("(Account Number is a 15-digit number): ");
            accNum = Helper.SCANNER.nextLine();
        } while (!accNum.matches("[0-9]{15}"));
        return accNum;
    }

    private boolean isAccNumValid(String accNum) {
        Map<String, SimpleUser> allUsers = BankUsersDataBase.getSimpleUsers();
        for (Map.Entry<String, SimpleUser> entry : allUsers.entrySet()) {
            if (entry.getValue().getAccount().getAccountNumber().equals(accNum)) {
                return true;
            }
        }
        return false;
    }

    private SimpleUser findAccountOwner(String accNum) {
        Map<String, SimpleUser> allUsers = BankUsersDataBase.getSimpleUsers();
        for (Map.Entry<String, SimpleUser> entry : allUsers.entrySet()) {
            if (entry.getValue().getAccount().getAccountNumber().equals(accNum)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void disRecentAccounts() {
        int counter = 0;
        for (String accNum : recentTfedAccs) {
            counter++;
            System.out.println(counter + ". " + accNum);
        }
    }

    private boolean alsoAContact(SimpleUser user, SimpleUser contactUser) {
        for (Contact contact : contactUser.getContacts()) {
            if (contact.getPhoneNum().equals(user.getPhoneNum())) {
                return true;
            }
        }
        return false;
    }

    private double getAndCheckTransferAmount() {
        String strAmount;
        double dblAmount = 0.0;
        boolean flag = true;
        System.out.println("\nEnter the transfer Amount");
        do {
            System.out.print("(Amount > 0$): ");
            strAmount = Helper.SCANNER.nextLine();
            try {
                dblAmount = Double.parseDouble(strAmount);
                flag = false;
            } catch (NumberFormatException ignored) {
            }
        } while (flag || dblAmount <= 0);
        return dblAmount;
    }

    private void checkTransferConfirmed(SimpleUser user, SimpleUser destUser, double transferAmount) {
        System.out.println("\nAmount of " + Colors.BLUE + transferAmount + "$ (+0.1% Transfer Fee) " + Colors.RESET +
                "is being transferred to " + Colors.BLUE + destUser.getFirstName() + " " +
                destUser.getLastName() + Colors.RESET);
        System.out.println("Are you sure to proceed?\n1. Yes\n2. AwHellNah\n");
        int choice = Helper.returnChoice(1, 2);
        if (choice == 2) {
            transfer(user);
        }
    }

    private String returnContactNameOfUser(List<Contact> contacts, SimpleUser destUser) {
        for (Contact contact : contacts) {
            if (contact.getPhoneNum().equals(destUser.getPhoneNum())) {
                return contact.getFirstName() + " " + contact.getLastName();
            }
        }
        return null;
    }

    //Transaction Methods:
    public void viewRecentTransactions(SimpleUser user) {
        List<Transaction> transactions = filteredTransactions(user);
        chooseNDisplayTransaction(user, transactions);
    }

    private void chooseNDisplayTransaction(SimpleUser user, List<Transaction> transactions) {
        checkEmptyTransactions(transactions, user);
        Collections.sort(transactions);
        printTransactions(transactions);
        int index = Helper.returnChoice(0, transactions.size()) - 1;
        if (index == -1) {
            viewRecentTransactions(user);
        }
        viewTransaction(transactions.get(index));
        chooseNDisplayTransaction(user, transactions);
    }

    private void printTransactions(List<Transaction> transactions) {
        int counter = 0;
        for (Transaction transaction : transactions) {
            counter++;
            System.out.println(counter + ") " + transaction.toString());
        }
        System.out.println("\nChoose a Transaction for more info " +
                "(Or Enter '0' to " + Colors.RED + "Exit" + Colors.RESET + ")\n");
    }

    private void viewTransaction(Transaction transaction) {
        if (transaction.getType().equals(Type.DEPOSIT)) {
            System.out.println(transaction.disDeposit());
        } else if (transaction.getType().equals(Type.RECEIVE)) {
            System.out.println(transaction.disReceive());
        } else {
            System.out.println(transaction.disTransfer());
        }
    }

    private List<Transaction> filteredTransactions(SimpleUser user) {
        disFilters();
        int filterChoose = Helper.returnChoice(1, 3);
        ArrayList<Transaction> transactions = new ArrayList<>();
        if (filterChoose == 1) {
            return dateFilterTransactions(transactions);
        } else if (filterChoose == 2) {
            return getrecentTransacs();
        } else {
            UserMenus.usersFeatures(user.getPhoneNum());
            return null;
        }
    }

    private void disFilters() {
        System.out.println("1. " + Colors.BLUE + "Filter Transactions By Date" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "View Without Filters" + Colors.RESET);
        System.out.println("3. " + Colors.RED + "Exit\n" + Colors.RESET);
    }

    private List<Transaction> dateFilterTransactions(List<Transaction> transactions) {
        System.out.print("Enter the start date (YYYY-MM-DD): ");
        Instant startDate = Helper.scanDate();

        System.out.print("Enter the end date (YYYY-MM-DD): ");
        Instant endDate = Helper.scanDate();

        if (startDate.isAfter(endDate)) {
            Instant temp = endDate;
            endDate = startDate;
            startDate = temp;
        }

        for (Transaction transaction : getrecentTransacs()) {
            if (transaction.getTime().isAfter(startDate) && transaction.getTime().isBefore(endDate)) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    private void checkEmptyTransactions(List<Transaction> transactions, SimpleUser user) {
        if (transactions.isEmpty()) {
            System.out.println(Colors.RED + "No Transactions!\n" + Colors.RESET);
            UserMenus.usersFeatures(user.getPhoneNum());
        }
    }
}