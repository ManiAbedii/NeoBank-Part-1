package ir.ac.kntu;

public class FirstMenu {

    public static void runFirstMenu() {
        disMenuOpts();
        int choice = Helper.returnChoice(1, 3);
        handleChoice(choice);
    }

    private static void disMenuOpts() {
        Helper.cls();
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Login" + Colors.RESET);
        System.out.println("1. " + Colors.BLUE + "Login As Simple User" + Colors.RESET);
        System.out.println("2. " + Colors.BLUE + "Login As Support User" + Colors.RESET);
        System.out.println("3. " + Colors.RED + "Exit\n" + Colors.RESET);
    }

    private static void handleChoice(int choice) {
        switch (choice) {
            case 1 -> {
                System.out.println(Colors.GREEN + "Logging in as User...\n" + Colors.RESET);
                UserMenus.userMainMenu();
            }
            case 2 -> {
                System.out.println(Colors.GREEN + "Logging in as Support User...\n" + Colors.RESET);
                SupportMenus.supportMainMenu();
            }
            case 3 -> {
                System.out.println(Colors.RED + "Exiting..." + Colors.RESET);
                System.exit(0);
            }
            default -> System.out.println();
        }
    }
}