package ir.ac.kntu;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Helper {
    public static final Scanner SCANNER = new Scanner(System.in);

    public static int returnChoice(int from, int tooo) {
        String input;
        int option = 0;
        do {
            System.out.print("Enter your choice (" + from + "-" + tooo + "): ");
            input = Helper.SCANNER.nextLine();
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException ignored) {
            }
        } while (option > tooo || option < from);
        System.out.println();
        return option;
    }

    public static Instant scanDate() {
        String strInstant;
        Instant instant = null;
        boolean flag = true;
        do {
            strInstant = Helper.SCANNER.nextLine();
            strInstant += "T00:00:00.000Z";
            try {
                instant = Instant.parse(strInstant);
                flag = false;
            } catch (DateTimeParseException e) {
                System.out.println(Colors.RED + "entered date should be valid & in yyyy-MM-dd format " + Colors.RESET);
            }
        } while (flag);
        return instant;
    }

    public static String scanPhoneNum() {
        String phoneNum = Helper.SCANNER.nextLine();
        while (!phoneNum.matches("09[0-9]{9}")) {
            System.out.println(Colors.RED + "Enter the phone number in this format: [09*********]" + Colors.RESET);
            phoneNum = Helper.SCANNER.nextLine();
        }
        return phoneNum;
    }

    public static void cls() {
        System.out.print("\033[2J\033[H");
    }
}
