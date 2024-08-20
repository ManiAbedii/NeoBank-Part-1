package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SupportMenus {

    private static List<String> allAuthenReq = new ArrayList<>();

    public static void supportMainMenu() {
        LoginMenu.supportLogin();
        supportUsersFeatures();
    }

    //Features:
    public static void supportUsersFeatures() {
        disSupportFeatures();
        int suppFeatureChoice = Helper.returnChoice(1, 4);
        handleFeature(suppFeatureChoice);
    }

    private static void disSupportFeatures() {
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Support User" + Colors.RESET);
        System.out.println("1. " + Colors.BLUE + "Authentication Requests" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "Tickets" + Colors.RESET);
        System.out.println("3. " + Colors.BLUE + "Users" + Colors.RESET);
        System.out.println("4. " + Colors.RED + "Exit" + Colors.RESET);
    }

    private static void handleFeature(int choice) {
        switch (choice) {
            case 1 -> {
                System.out.println("\n" + Colors.BLUE_BACKGROUND + "Authentication Requests" + Colors.RESET);
                authenticationRequestsMenu();
            }
            case 2 -> {
                System.out.println("\n" + Colors.BLUE_BACKGROUND + "Tickets" +  Colors.RESET);
                checkTickets();
            }
            case 3 -> {
                System.out.println("\n" + Colors.BLUE_BACKGROUND + "Users" +  Colors.RESET);
                usersList();
            }
            case 4 -> FirstMenu.runFirstMenu();
            default -> System.out.println();
        }
    }

    //Authentication Requests:
    private static void authenticationRequestsMenu() {
        refreshRequests();
        if (allAuthenReq.isEmpty()) {
            System.out.println(Colors.RED + "Requests List is Empty" + Colors.RESET);
            System.out.println("Heading to Main Menu\n");
            SupportMenus.supportUsersFeatures();
        }
        int counter = 0;
        for (String request : allAuthenReq) {
            counter++;
            System.out.println(counter + ". " + request);
        }
        System.out.println("Choose a request (or enter '0' to " + Colors.RED + "Exit" + Colors.RESET + ")\n");
        int chooseRequest = Helper.returnChoice(0, counter) - 1;
        if (chooseRequest == -1) {
            SupportMenus.supportUsersFeatures();
        }
        SimpleUser user = BankUsersDataBase.getUser(allAuthenReq.get(chooseRequest));
        acceptOrReject(user);
    }

    private static void acceptOrReject(SimpleUser user) {
        System.out.println(user.toString());
        disOptions();
        int choice = Helper.returnChoice(1, 3);
        handleChoice(choice, user);
    }

    private static void disOptions() {
        System.out.println("\n1. " + Colors.GREEN + "Confirm Authentication Request" + Colors.RESET);
        System.out.println("2. " + Colors.RED + "Reject Authentication Request" + Colors.RESET);
        System.out.println("3. " + "Return to previous menu\n");
    }

    private static void handleChoice(int choice, SimpleUser user) {
        switch (choice) {
            case 1 -> {
                user.setAuthenStatus(AuthenticationStatus.OK.getDisplayName());
                refreshRequests();
                System.out.println(Colors.GREEN + "Confirmed!\n" + Colors.RESET);
                authenticationRequestsMenu();
            }
            case 2 -> {
                String reason;
                System.out.print("Enter the reason you are rejecting the request for: ");
                reason = Helper.SCANNER.nextLine();
                user.setAuthenStatus(reason);
                refreshRequests();
                System.out.println(Colors.GREEN + "Done\n" + Colors.RESET);
                authenticationRequestsMenu();
            }
            case 3 -> authenticationRequestsMenu();
            default -> System.out.println();
        }
    }

    public static void refreshRequests() {
        allAuthenReq = LoginMenu.getUncheckedUsers();
    }

    //Tickets:
    public static void checkTickets() {
        List<Ticket> tickets = BankUsersDataBase.getCopyOfAllTickets();
        turnSubmittedToInProgress(tickets);
        disFilters();
        int filter = Helper.returnChoice(1, 4);
        List<Ticket> shownTickets = displayList(filter, tickets);
        disNRespond(shownTickets);
    }

    private static void disNRespond(List<Ticket> shownTickets) {
        disTickets(shownTickets);
        respondToTicket(shownTickets);
        disNRespond(shownTickets);
    }

    private static void disTickets(List<Ticket> shownTickets) {
        if (shownTickets.isEmpty()) {
            System.out.println(Colors.RED + "Tickets list is empty\n" + Colors.RESET);
            checkTickets();
        }
        int counter = 0;
        for (Ticket ticket : shownTickets) {
            counter++;
            System.out.println(counter + ". " + Colors.BLUE + ticket.getSubject() + Colors.RESET
                    + "\nTicket State: " + Colors.BLUE + ticket.getState().getDisplayName() + Colors.RESET + "\n");
        }
    }

    private static void respondToTicket(List<Ticket> shownTickets) {
        System.out.println("Choose a ticket for more info & response (or enter '0' to " + Colors.RED + "Exit" + Colors.RESET + ")\n");
        int chooseATicket = Helper.returnChoice(0, shownTickets.size()) - 1;
        if (chooseATicket == -1) {
            checkTickets();
        }
        System.out.println(shownTickets.get(chooseATicket));
        System.out.print("Enter your response to this ticket: ");
        String response = Helper.SCANNER.nextLine();
        shownTickets.get(chooseATicket).setResponse(response);
        shownTickets.get(chooseATicket).setState(TicketState.CLOSED);
        System.out.println(Colors.GREEN + "Ticket Responded Successfully\n" + Colors.RESET);
    }

    private static void turnSubmittedToInProgress(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            if (ticket.getState().equals(TicketState.SUBMITTED)) {
                ticket.setState(TicketState.IN_PROGRESS);
            }
        }
    }

    private static void disFilters() {
        System.out.println("Display tickets filtered by: ");
        System.out.println("1. " + Colors.BLUE + "Ticket State" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "Ticket Section" + Colors.RESET);
        System.out.println("3. " + Colors.BLUE + "Applicant User" + Colors.RESET);
        System.out.println("4. " + Colors.RED + "Exit\n" + Colors.RESET);
    }

    private static List<Ticket> displayList(int filter, List<Ticket> tickets) {
        List<Ticket> shownList = new ArrayList<>();
        switch (filter) {
            case 1 -> {
                //Ticket State:
                System.out.println("Show tickets whose status is:");
                System.out.println("1. In Progress\n2. Closed\n");
                int status = Helper.returnChoice(1, 2);
                handleStatus(status, shownList, tickets);
            }
            case 2 -> {
                //Ticket Section:
                System.out.println("Show tickets which belong to: ");
                System.out.println("1. Report\n2. Contacts\n3. Transfer\n4. Settings");
                int section = Helper.returnChoice(1, 3);
                handleSection(section, shownList, tickets);
            }
            case 3 -> {
                //Applicant User:
                String phoneNum;
                System.out.println("Filter By Applicant User: ");
                System.out.println("Enter the phone number Of your desired user: ");
                phoneNum = Helper.scanPhoneNum();
                handlePhNum(phoneNum, shownList, tickets);
            }
            case 4 -> supportUsersFeatures();
            default -> System.out.println();
        }
        return shownList;
    }

    private static void handlePhNum(String phoneNum, List<Ticket> shownList, List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            if (ticket.getApplicantUser().getPhoneNum().equals(phoneNum)) {
                shownList.add(ticket);
            }
        }
    }

    private static void handleStatus(int status, List<Ticket> list, List<Ticket> tickets) {
        if (status == 1) {
            for (Ticket ticket : tickets) {
                if (ticket.getState().equals(TicketState.IN_PROGRESS)) {
                    list.add(ticket);
                }
            }
        } else {
            for (Ticket ticket : tickets) {
                if (ticket.getState().equals(TicketState.CLOSED)) {
                    list.add(ticket);
                }
            }
        }
    }

    private static void handleSection(int section, List<Ticket> list, List<Ticket> tickets) {
        Section ticketSection;
        ticketSection = switch (section) {
            case 1 -> Section.REPORTS;
            case 2 -> Section.CONTACTS;
            case 3 -> Section.TRANSFER;
            case 4 -> Section.SETTINGS;
            default -> null;
        };
        for (Ticket ticket : tickets) {
            if (ticket.getSection().equals(ticketSection)) {
                list.add(ticket);
            }
        }
    }

    //Users List
    private static void usersList() {
        disUsersOpts();
        int filter = Helper.returnChoice(1, 3);
        handleFilters(filter);
    }

    private static void disUsersOpts() {
        System.out.println("View Users Filtered by:");
        System.out.println("1. " + Colors.BLUE + "First Name, Last Name or Phone Number" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "View Without Filters" + Colors.RESET);
        System.out.println("3. " + Colors.RED + "Back to Previous Menu\n" + Colors.RESET);
    }

    private static void handleFilters(int choice){
        List<SimpleUser> users = new ArrayList<>();
        switch (choice) {
            case 1 -> {
                users = goddamnFilters();
            }
            case 2 -> {
                users = allUsersOfBank();
            }
            case 3 -> supportUsersFeatures();
            default -> System.out.println();
        }
        displayAllUsers(users);
        chooseForInfo(users);
        usersList();
    }

    private static void displayAllUsers(List<SimpleUser> users) {
        if (users.isEmpty()) {
            System.out.println(Colors.RED + "No user was found\n" + Colors.RESET);
            usersList();
        }
        int counter = 0;
        System.out.println();
        for (SimpleUser user : users) {
            counter++;
            System.out.println(counter + ". " + user.getFirstName() + " " + user.getLastName());
        }
    }

    private static List<SimpleUser> allUsersOfBank() {
        List<SimpleUser> users = new ArrayList<>();
        for (Map.Entry<String, SimpleUser> entry : BankUsersDataBase.getSimpleUsers().entrySet()) {
            users.add(entry.getValue());
        }
        return users;
    }

    private static List<SimpleUser> goddamnFilters() {
        List<SimpleUser> usersWithFilters = allUsersOfBank();
        System.out.println("\nEnter the First Name: ");
        System.out.println("(Simply enter '#' if you don't want any filters on the first name)");
        String firstName = Helper.SCANNER.nextLine();
        System.out.println("\nEnter the Last Name: ");
        System.out.println("(Simply enter '#' if you don't want any filters on the last name)");
        String lastName = Helper.SCANNER.nextLine();
        System.out.println("\nEnter the Phone Number: ");
        System.out.println("(Simply enter '#' if you don't want any filters on the phone number)");
        String phoneNum = Helper.SCANNER.nextLine();

        if (!"#".equals(firstName)) {
            usersWithFilters.removeIf(user -> Similarity.similarity(user.getFirstName(), firstName) < 0.5);
        }

        if (!"#".equals(lastName)) {
            usersWithFilters.removeIf(user -> Similarity.similarity(user.getLastName(), lastName) < 0.5);
        }

        if (!"#".equals(phoneNum)) {
            usersWithFilters.removeIf(user -> Similarity.similarity(user.getPhoneNum(), phoneNum) < 0.5);
        }

        if (usersWithFilters.equals(allUsersOfBank())) {
            System.out.println(Colors.RED + "\nNo filters were applied\n" + Colors.RESET);
            usersList();
        }
        return usersWithFilters;
    }

    private static void chooseForInfo(List<SimpleUser> users) {
        System.out.println("Choose a User For More info (Or enter '0' to " + Colors.RED + "Exit" + Colors.RESET + "):\n");
        int chosenUser = Helper.returnChoice(0, users.size()) - 1;
        if (chosenUser == -1) {
            usersList();
        }
        System.out.println(users.get(chosenUser).showUserInfo());
    }
}
