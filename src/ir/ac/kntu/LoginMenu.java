package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginMenu {

    //User:
    public static void userLogIn() {
        String phoneNum, password;
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Login" + Colors.RESET);
        System.out.print("Enter your Phone Number: ");
        phoneNum = Helper.scanPhoneNum();
        if (!BankUsersDataBase.userAlreadySignedUp(phoneNum)) {
            System.out.println(Colors.RED + "No user was found with this phone number" + Colors.RESET);
            UserMenus.userMainMenu();
        }

        System.out.print("Enter your Password: ");
        password = Helper.SCANNER.nextLine();
        while (!userPasswordIsCorrect(password, phoneNum)) {
            System.out.println(Colors.RED + "Incorrect Password, Try Again:" + Colors.RESET);
            password = Helper.SCANNER.nextLine();
            //TODO: forget password
        }
        checkAuthenticationStatus(phoneNum);
        System.out.println(Colors.GREEN + "Logged in successfully!\n" + Colors.RESET);
        UserMenus.usersFeatures(phoneNum);
    }

    public static boolean userPasswordIsCorrect(String password, String phoneNum) {
        return password.equals(BankUsersDataBase.getUser(phoneNum).getPassword());
    }

    private static void checkAuthenticationStatus(String phoneNum) {
        if (BankUsersDataBase.getUser(phoneNum).getAuthenStatus().equals(AuthenticationStatus.OK.getDisplayName())) {
            return;
        } else if (BankUsersDataBase.getUser(phoneNum).getAuthenStatus().equals(AuthenticationStatus.UNCHECKED.getDisplayName())) {
            System.out.println(Colors.RED + "your authentication needs to be verified before you can proceed" + Colors.RESET);
            UserMenus.userMainMenu();
        } else {
            System.out.println(Colors.RED + "Your login request has been rejected by the support due to " + "\"" +
                    BankUsersDataBase.getUser(phoneNum).getAuthenStatus() + "\"" + Colors.RESET);
            SignUpMenu.editSignUp(phoneNum);
        }
    }

    public static List<String> getUncheckedUsers() {
        List<String> uncheckedUsers = new ArrayList<>();
        Map<String, SimpleUser> simpleUsers = BankUsersDataBase.getSimpleUsers();
        for (Map.Entry<String, SimpleUser> user : simpleUsers.entrySet()) {
            if (user.getValue().getAuthenStatus().equals(AuthenticationStatus.UNCHECKED.getDisplayName())) {
                uncheckedUsers.add(user.getValue().getPhoneNum());
            }
        }
        return uncheckedUsers;
    }

    //Support:
    public static void supportLogin() {
        String userName, password;
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Support User Login" + Colors.RESET);
        System.out.print("Enter your Username: ");
        userName = Helper.SCANNER.nextLine();
        if (!BankUsersDataBase.supportExists(userName)) {
            System.out.println(Colors.RED + "No user was found with this username" + Colors.RESET);
            FirstMenu.runFirstMenu();
        }

        System.out.print("Enter your Password: ");
        password = Helper.SCANNER.nextLine();
        while (!supportPasswordIsCorrect(password, userName)) {
            System.out.println(Colors.RED + "Incorrect Password, Try Again:" + Colors.RESET);
            password = Helper.SCANNER.nextLine();
            //TODO: forget password
        }
        System.out.println(Colors.GREEN + "Logged in successfully!\n" + Colors.RESET);
    }

    private static boolean supportPasswordIsCorrect(String password, String username) {
        return password.equals(BankUsersDataBase.getSupportUser(username).getPassword());
    }
}
