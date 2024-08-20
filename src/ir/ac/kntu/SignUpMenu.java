package ir.ac.kntu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpMenu {

    public static void userSignUp() {
        String firstName, lastName, phoneNum, socialNum, password;
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Sign Up" + Colors.RESET);
        System.out.print("Enter your first name: ");
        firstName = Helper.SCANNER.nextLine();
        System.out.print("Enter your Second name: ");
        lastName = Helper.SCANNER.nextLine();
        System.out.print("Enter your Phone Number: ");
        phoneNum = Helper.scanPhoneNum();
        System.out.print("Enter your Social Number: ");
        socialNum = Helper.SCANNER.nextLine();
        System.out.print("Enter your Password: ");
        password = Helper.SCANNER.nextLine();
        while (passwordIsWeak(password)) {
            System.out.println(Colors.RED + "Password is weak");
            System.out.println("Password must contain Uppercase, LowerCase, " +
                    "numbers & Special Characters (@,#,$,%,&,_) WITHOUT spaces" + Colors.RESET);
            password = Helper.SCANNER.nextLine();
        }

        if (BankUsersDataBase.userAlreadySignedUp(phoneNum)) {
            System.out.println(Colors.RED + "This user has already signed up" + Colors.RESET);
            UserMenus.userMainMenu();
        }

        SimpleUser newUser = new SimpleUser(firstName, lastName, phoneNum, socialNum, password);
        BankUsersDataBase.addUser(phoneNum, newUser);
        System.out.println(Colors.GREEN + "signed up successfully!\n" + Colors.RESET
                + "Your authentication request was sent to Support for verification\n");
        UserMenus.userMainMenu();
    }

    public static boolean passwordIsWeak(String password) {
        Pattern spaces = Pattern.compile("\s");
        Matcher spaceMatcher = spaces.matcher(password);
        if (spaceMatcher.find()) {
            return true;
        }
        Pattern upperCase, lowerCase, numbers, specialCharacters;
        Matcher upperCaseMatch, lowerCaseMatch, numbersMatch, specCharMatch;

        upperCase = Pattern.compile("[A-Z]+");
        lowerCase = Pattern.compile("[a-z]+");
        numbers = Pattern.compile("[0-9]+");
        specialCharacters = Pattern.compile("[@#$%&_]+");

        upperCaseMatch = upperCase.matcher(password);
        if (!upperCaseMatch.find()) {
            return true;
        }
        lowerCaseMatch = lowerCase.matcher(password);
        if (!lowerCaseMatch.find()) {
            return true;
        }
        numbersMatch = numbers.matcher(password);
        if (!numbersMatch.find()) {
            return true;
        }
        specCharMatch = specialCharacters.matcher(password);
        return !specCharMatch.find();
    }

    //Edit SignUp:
    public static void editSignUp(String phoneNum) {
        disEditOpts();
        int choice = Helper.returnChoice(1, 4);
        switch (choice) {
            case 1 -> editName(phoneNum);
            case 2 -> editID(phoneNum);
            case 3 -> editPassword(phoneNum);
            case 4 -> FirstMenu.runFirstMenu();
            default -> System.out.println();
        }
    }

    private static void disEditOpts() {
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Edit User Info" + Colors.RESET);
        System.out.println("What info are you willing to edit?");
        System.out.println("1. " + Colors.BLUE + "Firstname & Lastname" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "Social Number" + Colors.RESET);
        System.out.println("3. " + Colors.BLUE + "Password" + Colors.RESET);
        System.out.println("4. " + Colors.RED + "Exit\n" + Colors.RESET);
    }

    private static void editName(String phoneNum) {
        String firstName, lastName;
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Edit Name" + Colors.RESET);
        System.out.print("Enter your First name: ");
        firstName = Helper.SCANNER.nextLine();
        System.out.print("Enter your Last name: ");
        lastName = Helper.SCANNER.nextLine();
        System.out.println(Colors.GREEN + "Successful!\n" + Colors.RESET);
        BankUsersDataBase.getUser(phoneNum).setFirstName(firstName);
        BankUsersDataBase.getUser(phoneNum).setLastName(lastName);
        BankUsersDataBase.getUser(phoneNum).setAuthenStatus(AuthenticationStatus.UNCHECKED.getDisplayName());
        editSignUp(phoneNum);
    }

    private static void editID(String phoneNum) {
        String socialNum;
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Edit Id" + Colors.RESET);
        System.out.print("Enter your Social Number: ");
        socialNum = Helper.SCANNER.nextLine();
        System.out.println(Colors.GREEN + "Successful!\n" + Colors.RESET);
        BankUsersDataBase.getUser(phoneNum).setSocialNum(socialNum);
        BankUsersDataBase.getUser(phoneNum).setAuthenStatus(AuthenticationStatus.UNCHECKED.getDisplayName());
        editSignUp(phoneNum);
    }

    private static void editPassword(String phoneNum) {
        String password;
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Change Password" + Colors.RESET);
        System.out.print("Enter new Password: ");
        password = Helper.SCANNER.nextLine();
        while (passwordIsWeak(password)) {
            System.out.println(Colors.RED + "Password is weak");
            System.out.println("Password must contain Uppercase, LowerCase, " +
                    "numbers & Special Characters (@,#,$,%,&,_) WITHOUT spaces" + Colors.RESET);
            password = Helper.SCANNER.nextLine();
        }
        System.out.println(Colors.GREEN + "Successful!\n" + Colors.RESET);
        BankUsersDataBase.getUser(phoneNum).setPassword(password);
        BankUsersDataBase.getUser(phoneNum).setAuthenStatus(AuthenticationStatus.UNCHECKED.getDisplayName());
        editSignUp(phoneNum);
    }
}
