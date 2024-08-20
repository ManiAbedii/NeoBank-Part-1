package ir.ac.kntu;

import ir.ac.kntu.util.Calendar;
import java.time.Instant;

enum Type {
    DEPOSIT("Deposit"),
    TRANSFER("Transfer"),
    RECEIVE("Receive");

    private final String displayName;

    Type(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

public class Transaction implements Comparable<Transaction> {

    private Type type;
    private double amount;
    private String receiverName;
    private String receiverAccNum;
    private Instant date;

    public Transaction(Type type, double amount) {
        this.type = type;
        this.amount = amount;
        this.date = Calendar.now();
    }

    public Transaction(Type type, double amount, String receiverAccNum, String receiverName) {
        this.type = type;
        this.amount = amount;
        this.receiverAccNum = receiverAccNum;
        this.receiverName = receiverName;
        this.date = Calendar.now();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionTime() {
        return date.toString().substring(0, 10) + " " + date.toString().substring(12, 19);
    }

    public Instant getTime() {
        return date;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAccNum() {
        return receiverAccNum;
    }

    public void setReceiverAccNum(String receiverAccNum) {
        this.receiverAccNum = receiverAccNum;
    }

    public String disDeposit() {
        String output = "";
        output += "Deposit: " + Colors.GREEN + "+" + getAmount() + "$"  + Colors.RESET
                + "\nDate Of Transaction: " + Colors.BLUE + getTransactionTime() + Colors.RESET
                + "\n";
        return output;
    }

    public String disReceive() {
        String output = "";
        output += "Receive: " + Colors.GREEN + "+" + getAmount() + "$"  + Colors.RESET
                + "\nDate Of Transaction: " + Colors.BLUE + getTransactionTime() + Colors.RESET
                + "\n";
        return output;
    }

    public String disTransfer() {
        String output = "";
        output += "Transfer: " + Colors.RED + "-" + getAmount() + "$" + Colors.RESET
                + "\nTo " + Colors.BLUE + getReceiverName() + " (" + getReceiverAccNum() + ")" + Colors.RESET
                + "\nDate Of Transaction: " + Colors.BLUE + getTransactionTime() + Colors.RESET
                + "\n";
        return output;
    }

    @Override
    public String toString() {
        String output = "";
        if (getType().equals(Type.TRANSFER)) {
            output += Colors.RED + getType().getDisplayName() + ": "
                    + "-" + getAmount()+"$" + Colors.RESET;
        } else {
            output += Colors.GREEN + getType().getDisplayName() + ": "
                    + "+" + getAmount()+"$" + Colors.RESET;
        }
        return output;
    }

    @Override
    public int compareTo(Transaction transaction) {
        if (this.getTime().isAfter(transaction.getTime())) {
            return 1;
        } else if (this.getTime().isBefore(transaction.getTime())) {
            return -1;
        } else {
            return 0;
        }
    }
}
