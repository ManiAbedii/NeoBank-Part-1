package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;

public class UserMenus {

    public static void userMainMenu() {
        disMainMenuOpts();
        int choice = Helper.returnChoice(1,3);
        handleChoice(choice);
    }

    private static void disMainMenuOpts() {
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "User Main Menu" + Colors.RESET);
        System.out.println("1. " + Colors.BLUE + "Login" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "Sign Up" + Colors.RESET);
        System.out.println("3. " + Colors.RED + "Return To Login Menu\n" + Colors.RESET);
    }

    private static void handleChoice(int choice) {
        switch (choice) {
            case 1 -> LoginMenu.userLogIn();
            case 2 -> SignUpMenu.userSignUp();
            case 3 -> {
                System.out.println(Colors.RED + "Returning..." + Colors.RESET);
                FirstMenu.runFirstMenu();
            }
            default -> System.out.println();
        }
    }

    //Features Menus:
    public static void usersFeatures(String phoneNum) {
        SimpleUser user = BankUsersDataBase.getUser(phoneNum);
        disFeaturesMenu(user);
        int choice = Helper.returnChoice(1, 6);
        handleFeatureChoice(choice, user, phoneNum);
    }

    private static void disFeaturesMenu(SimpleUser user) {
        System.out.println("Welcome " + Colors.BLUE + user.getFirstName() + Colors.RESET);
        System.out.println("1. " + Colors.BLUE + "Account Management" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "Contacts" + Colors.RESET);
        System.out.println("3. " + Colors.BLUE + "Transfer" + Colors.RESET);
        System.out.println("4. " + Colors.BLUE + "Ticket Services" + Colors.RESET);
        System.out.println("5. " + Colors.BLUE + "Settings" + Colors.RESET);
        System.out.println("6. " + Colors.RED + "Exit" + Colors.RESET);
    }

    private static void handleFeatureChoice(int choice, SimpleUser user, String phoneNum) {
        switch (choice) {
            case 1 -> {
                //Account Management:
                disAccManage();
                int accManageChoice = Helper.returnChoice(1, 4);
                handleAccManage(accManageChoice, user, phoneNum);
            }
            case 2 -> {
                //Contacts:
                if (!user.isContactsEnabled()) {
                    System.out.println(Colors.RED + "Enable Contacts via Settings to Use this Feature\n" + Colors.RESET);
                    usersFeatures(phoneNum);
                }
                disContacts();
                int contactsChoice = Helper.returnChoice(1, 3);
                handleContactsChoice(contactsChoice, user, phoneNum);
            }
            case 3 -> {
                //Transfer:
                user.getAccount().transfer(user);
            }
            case 4 -> {
                //Ticket Service:
                ticketsService(phoneNum);
            }
            case 5 -> {
                //Settings:
                disSettings();
                int settingsChoice = Helper.returnChoice(1, 4);
                handleSettingChoice(settingsChoice, user);
            }
            case 6 -> FirstMenu.runFirstMenu();
            default -> System.out.println();
        }
    }

    //Account Management:
    private static void disAccManage() {
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Account Management" + Colors.RESET);
        System.out.println("1. " + Colors.BLUE + "Deposit" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "View Balance" + Colors.RESET);
        System.out.println("3. " + Colors.BLUE + "View Recent Transactions" + Colors.RESET);
        System.out.println("4. " + Colors.RED + "Exit" + Colors.RESET);
    }

    private static void handleAccManage(int choice, SimpleUser user, String phoneNum) {
        switch (choice) {
            case 1 -> {
                System.out.println("\n" + Colors.BLUE_BACKGROUND + "Deposit" + Colors.RESET);
                user.getAccount().deposit(user);
                usersFeatures(phoneNum);
            }
            case 2 -> {
                System.out.println("\n" + Colors.BLUE_BACKGROUND + "View Balance" + Colors.RESET);
                user.getAccount().showBalance();
                usersFeatures(phoneNum);
            }
            case 3 -> {
                System.out.println("\n" + Colors.BLUE_BACKGROUND + "View Recent Transactions" + Colors.RESET);
                user.getAccount().viewRecentTransactions(user);
                usersFeatures(phoneNum);
            }
            case 4 -> usersFeatures(phoneNum);
            default -> System.out.println();
        }
    }

    //Contacts:
    private static void handleContactsChoice(int choice, SimpleUser user, String phoneNum) {
        switch (choice) {
            case 1 -> chooseContact(user);
            case 2 -> createContact(user);
            case 3 -> usersFeatures(phoneNum);
            default -> System.out.println();
        }
    }

    private static void disContacts() {
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Contacts" + Colors.RESET);
        System.out.println("1. " + Colors.BLUE + "View Contacts" + Colors.RESET + "\n2. " + Colors.BLUE +
                "Add Contact" + Colors.RESET + "\n3. " + Colors.RED + "Exit To Previous Menu" + Colors.RESET);
    }

    private static void chooseContact(SimpleUser user) {
        checkEmptyContacts(user);
        int contactIndex = disNChooseContacts(user);
        disChooseOpts();
        int chooseOpt = Helper.returnChoice(1, 3);
        switch (chooseOpt) {
            case 1 -> {
                user.editContact(contactIndex);
                chooseContact(user);
            }
            case 2 -> {
                user.removeContact(contactIndex);
                chooseContact(user);
            }
            case 3 -> chooseContact(user);
            default -> System.out.println();
        }
    }

    private static int disNChooseContacts(SimpleUser user) {
        int counter = 0;
        for (Contact contact : user.getContacts()) {
            counter++;
            System.out.println(counter + ". " + Colors.BLUE + contact.getFirstName() + " " + contact.getLastName() + Colors.RESET);
        }
        System.out.println("\n" + "Choose a contact for more info & settings (enter '0' to " + Colors.RED + "Exit" + Colors.RESET + "):\n");
        int contactIndex = Helper.returnChoice(0, counter) - 1;
        if (contactIndex == -1) {
            usersFeatures(user.getPhoneNum());
        }
        System.out.println(user.getContacts().get(contactIndex).toString());
        return contactIndex;
    }

    private static void checkEmptyContacts(SimpleUser user) {
        if (user.getContacts().isEmpty()) {
            System.out.println(Colors.RED + "Contacts' list is empty for this user\n" + Colors.RESET);
            UserMenus.usersFeatures(user.getPhoneNum());
        }
    }

    private static void disChooseOpts() {
        System.out.println("1. " + Colors.BLUE + "Edit Contact" + Colors.RESET + "\n2. " + Colors.BLUE +
                "Delete Contact" + Colors.RESET + "\n3. " + Colors.RED + "Exit To Previous Menu" + Colors.RESET);
    }

    private static void createContact(SimpleUser user) {
        String newFirstName, newLastName, newPhoneNum;
        System.out.print("Enter Phone Number: ");
        newPhoneNum = Helper.scanPhoneNum();
        if (!BankUsersDataBase.userAlreadySignedUp(newPhoneNum)) {
            System.out.println(Colors.RED + "This phone number does not belong to a valid user\n" + Colors.RESET);
            usersFeatures(user.getPhoneNum());
        }
        if (!BankUsersDataBase.getUser(newPhoneNum).getAuthenStatus().equals(AuthenticationStatus.OK.getDisplayName())) {
            System.out.println(Colors.RED + "The user which you are adding as a contact, isn't verified\n" + Colors.RESET);
            usersFeatures(user.getPhoneNum());
        }
        System.out.print("Enter First Name: ");
        newFirstName = Helper.SCANNER.nextLine();
        System.out.print("Enter Last Name: ");
        newLastName = Helper.SCANNER.nextLine();
        Contact contact = new Contact(newFirstName, newLastName, newPhoneNum);
        if (user.getContacts().contains(contact)) {
            System.out.println(Colors.RED + "You already have this user as your contact, edit if needed\n" + Colors.RESET);
            UserMenus.usersFeatures(user.getPhoneNum());
        }
        user.addContact(contact);
        System.out.println(Colors.GREEN + "Contact has been added Successfully\n" + Colors.RESET);
        usersFeatures(user.getPhoneNum());
    }

    //Tickets Service:
    public static void ticketsService(String phoneNum) {
        SimpleUser user = BankUsersDataBase.getUser(phoneNum);
        Ticket ticket = new Ticket(user);
        chooseAndSetSection(ticket, phoneNum);

        disTicketMenu();
        int ticketServChoose = Helper.returnChoice(1, 3);

        switch (ticketServChoose) {
            case 1 -> {
                System.out.println("\n" + Colors.BLUE_BACKGROUND + "Show Previous Tickets" + Colors.RESET);
                showPreviousTickets(ticket, user);
            }
            case 2 -> {
                System.out.println("\n" + Colors.BLUE_BACKGROUND + "Submit New Ticket" + Colors.RESET);
                submitNewTicket(ticket, user);
            }
            case 3 -> usersFeatures(phoneNum);
            default -> System.out.println();
        }
    }

    private static void chooseAndSetSection(Ticket ticket, String phoneNum) {
        System.out.println("\nWhich section do you want to submit or view tickets about: ");
        System.out.println("1. " + Colors.BLUE + Section.REPORTS.getDisplayName() + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + Section.CONTACTS.getDisplayName() + Colors.RESET);
        System.out.println("3. " + Colors.BLUE + Section.TRANSFER.getDisplayName() + Colors.RESET);
        System.out.println("4. " + Colors.BLUE + Section.SETTINGS.getDisplayName() + Colors.RESET);
        System.out.println("5. " + Colors.RED + "Exit\n" + Colors.RESET);

        int sectionChoose = Helper.returnChoice(1, 5);
        switch (sectionChoose) {
            case 1 -> ticket.setSection(Section.REPORTS);
            case 2 -> ticket.setSection(Section.CONTACTS);
            case 3 -> ticket.setSection(Section.TRANSFER);
            case 4 -> ticket.setSection(Section.SETTINGS);
            case 5 -> usersFeatures(phoneNum);
            default -> System.out.println();
        }
    }

    private static void disTicketMenu() {
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Ticket Services" + Colors.RESET);
        System.out.println("1. " + Colors.BLUE + "Show Previously Submitted tickets" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "Submit New Ticket" + Colors.RESET);
        System.out.println("3. " + Colors.RED + "Exit\n" + Colors.RESET);
    }

    private static void showPreviousTickets(Ticket ticket, SimpleUser user) {
        List<Ticket> ticketsByThisUser = new ArrayList<>();
        for (Ticket everyTicket : BankUsersDataBase.getCopyOfAllTickets()) {
            if (everyTicket.getSection().equals(ticket.getSection()) && everyTicket.getApplicantUser().equals(user)) {
                ticketsByThisUser.add(everyTicket);
            }
        }
        if (ticketsByThisUser.isEmpty()) {
            System.out.println(Colors.RED + "Tickets list is empty\n" + Colors.RESET);
            ticketsService(user.getPhoneNum());
        }
        int counter = 0;
        for (Ticket everyTicket : ticketsByThisUser) {
            counter++;
            System.out.println(counter + ") " + everyTicket.getSubject());
            System.out.println(everyTicket.showTicketToUserHimSelf());
        }
        ticketsService(user.getPhoneNum());
    }

    private static void submitNewTicket(Ticket ticket, SimpleUser user) {
        String explanation, subject;
        System.out.print("Enter the main subject of your request: ");
        subject = Helper.SCANNER.nextLine();
        ticket.setSubject(subject);
        System.out.print("Enter Additional Explanations: ");
        explanation = Helper.SCANNER.nextLine();
        ticket.setExplanation(explanation);
        BankUsersDataBase.addTicket(ticket);
        ticket.setState(TicketState.SUBMITTED);
        System.out.println(Colors.GREEN + "Ticket Submitted Successfully" + Colors.RESET);
        ticketsService(user.getPhoneNum());
    }

    //Settings:
    private static void disSettings() {
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Settings" + Colors.RESET);
        System.out.println("1. " + Colors.BLUE + "Change Login Password" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "Change Card Password" + Colors.RESET);
        System.out.println("3. " + Colors.BLUE + "Disable/Enable Contacts" + Colors.RESET);
        System.out.println("4. " + Colors.RED + "Exit\n" + Colors.RESET);
    }

    private static void handleSettingChoice(int choice, SimpleUser user) {
        switch (choice) {
            case 1 -> changePassword(user);
            case 2 -> changeCardPass(user);
            case 3 -> enDisContacts(user);
            case 4 -> usersFeatures(user.getPhoneNum());
            default -> System.out.println();
        }
    }

    private static void enDisContacts(SimpleUser user) {
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Enable/Disable Contacts" + Colors.RESET);
        printContactsStatus(user); //Enabled/Disabled
        System.out.println("\n1. " + Colors.BLUE + "Change" + Colors.RESET + "\n2. " + Colors.RED + "Exit" + Colors.RESET);
        int contactChoice = Helper.returnChoice(1, 2);
        handleEnableDisable(contactChoice, user);
        usersFeatures(user.getPhoneNum());
    }

    private static void changeCardPass(SimpleUser user) {
        String password;
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Change Card Password" + Colors.RESET);
        System.out.println("Enter New Card Password: ");
        password = Helper.SCANNER.nextLine();
        while (!cardPassCheck(password)) {
            System.out.println(Colors.RED + "Card password must be a 4-digit number" + Colors.RESET);
            password = Helper.SCANNER.nextLine();
        }
        System.out.println(Colors.GREEN + "Successfully" + Colors.RESET);
        usersFeatures(user.getPhoneNum());
    }

    private static void changePassword(SimpleUser user) {
        String password, newPassword;
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Change Login Password" + Colors.RESET);
        System.out.print("Enter your Current Password: ");
        password = Helper.SCANNER.nextLine();
        if (!LoginMenu.userPasswordIsCorrect(password, user.getPhoneNum())) {
            System.out.println(Colors.RED + "Incorrect Password" + Colors.RESET);
            usersFeatures(user.getPhoneNum());
        }
        System.out.print("Enter new Password: ");
        newPassword = Helper.SCANNER.nextLine();
        while (SignUpMenu.passwordIsWeak(newPassword)) {
            System.out.println(Colors.RED + "Password is weak");
            System.out.println("Password must contain Uppercase, LowerCase, " +
                    "numbers & Special Characters (@,#,$,%,&,_) WITHOUT spaces" + Colors.RESET);
            newPassword = Helper.SCANNER.nextLine();
        }
        user.setPassword(newPassword);
        System.out.println(Colors.GREEN + "Changed" + Colors.RESET);
        usersFeatures(user.getPhoneNum());
    }

    private static void handleEnableDisable(int choice, SimpleUser user) {
        switch (choice) {
            case 1 -> {
                user.setContactsEnabled(!user.isContactsEnabled());
                System.out.println(Colors.GREEN + "Successful" + Colors.RESET);
            }
            case 2 -> usersFeatures(user.getPhoneNum());
            default -> System.out.println();
        }
    }

    private static boolean cardPassCheck(String password) {
        return password.matches("[0-9]{4}");
    }

    private static void printContactsStatus(SimpleUser user) {
        if (user.isContactsEnabled()) {
            System.out.println("Contacts currently: " + Colors.BLUE + "Enabled" + Colors.RESET);
        } else {
            System.out.println("Contacts currently: " + Colors.BLUE + "Disabled" + Colors.RESET);
        }
    }
}