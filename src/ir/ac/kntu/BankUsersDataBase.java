package ir.ac.kntu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankUsersDataBase {

    //Simple Users Data Base:
    private static Map<String, SimpleUser> simpleUsers = new HashMap<>();

    public static Map<String, SimpleUser> getSimpleUsers() {
        return new HashMap<>(simpleUsers);
    }

    public static SimpleUser getUser(String phoneNum) {
        return simpleUsers.get(phoneNum);
    }

    public static void addUser(String phoneNum, SimpleUser user) {
        simpleUsers.put(phoneNum, user);
    }

    public static boolean userAlreadySignedUp(String phoneNum) {
        return simpleUsers.containsKey(phoneNum);
    }

    //Support Data Base:
    private static Map<String, SupportUser> supportUsers = new HashMap<>();

    public static SupportUser getSupportUser(String username) {
        return supportUsers.get(username);
    }

    //This Method is to test the code
    public static void addManiSupport() {
        SupportUser supportUser = new SupportUser("mniabedii", "3962");
        supportUsers.put(supportUser.getUsername(), supportUser);
    }

    public static void addArminUser() {
        SimpleUser simpleUser = new SimpleUser("Armin", "Fakhar", "09903567137", "12741543", "Arfa2005@");
        simpleUsers.put(simpleUser.getPhoneNum(), simpleUser);
    }

    public static void addManiUser() {
        SimpleUser simpleUser = new SimpleUser("Mani", "Abedii", "09906868721", "0521643082", "Mni@3962");
        simpleUsers.put(simpleUser.getPhoneNum(), simpleUser);
    }
    //

    public static boolean supportExists(String username) {
        return supportUsers.containsKey(username);
    }

    //Tickets Data Base:
    private static List<Ticket> allTickets = new ArrayList<>();

    public static void addTicket(Ticket ticket) {
        allTickets.add(ticket);
    }

    public static List<Ticket> getCopyOfAllTickets() {
        return new ArrayList<>(allTickets);
    }
}