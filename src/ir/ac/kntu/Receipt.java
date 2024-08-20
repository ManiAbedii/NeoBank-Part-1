package ir.ac.kntu;

import java.time.Instant;
import ir.ac.kntu.util.Calendar;
import java.util.Random;

public class Receipt {

    private double trfrAmount;
    private String trfredAccNum;
    private String receiverAccNum;
    private String receiverName;
    private Instant trfrTime;
    private String trackingNum;

    private Random random = new Random();

    public Receipt(double trfrAmount, String trfredAccNum, String receiverAccNum, String receiverName) {
        this.trfrAmount = trfrAmount;
        this.trfredAccNum = trfredAccNum;
        this.receiverAccNum = receiverAccNum;
        this.receiverName = receiverName;
        this.trfrTime = Calendar.now();
        this.trackingNum = Integer.toString(random.nextInt(1000, 10000));
    }

    public double getTrfrAmount() {
        return trfrAmount;
    }

    public void setTrfrAmount(double trfrAmount) {
        this.trfrAmount = trfrAmount;
    }

    public String getTrfredAccNum() {
        return trfredAccNum;
    }

    public void setTrfredAccNum(String trfredAccNum) {
        this.trfredAccNum = trfredAccNum;
    }

    public String getReceiverAccNum() {
        return receiverAccNum;
    }

    public void setReceiverAccNum(String receiverAccNum) {
        this.receiverAccNum = receiverAccNum;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getTransferTime() {
        return trfrTime.toString().substring(0, 10) + " " + trfrTime.toString().substring(12, 19);
    }

    public Instant getTrfrTime() {
        return trfrTime;
    }

    public void setTrfrTime(Instant trfrTime) {
        this.trfrTime = trfrTime;
    }

    public String getTrackingNum() {
        return trackingNum;
    }

    public void setTrackingNum(String trackingNum) {
        this.trackingNum = trackingNum;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public String toString() {
        return "\nTransfer Receipt: \n" +
                "Amount of: " + Colors.BLUE + getTrfrAmount()+"$" +  Colors.RESET
                + "\nFrom: " + Colors.BLUE + getTrfredAccNum() + Colors.RESET
                + "\nTo : " + Colors.BLUE + getReceiverName() + Colors.RESET
                + "\nAccount Number: " + Colors.BLUE + getReceiverAccNum() + Colors.RESET
                + "\nTransfer Time: " + Colors.BLUE + getTransferTime() + Colors.RESET
                + "\nTracking Number: " + Colors.BLUE + getTrackingNum() + Colors.RESET
                + "\n";
    }
}