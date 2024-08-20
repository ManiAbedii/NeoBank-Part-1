package ir.ac.kntu;

public class Main {

    public static void main(String[] args) {
        Helper.cls();
        System.out.println(Colors.WHITE_BOLD + "  Welcome To Mani NeoBank");
        System.out.println("···Glad To Have You Here···\n" + Colors.RESET);

        BankUsersDataBase.addManiUser();
        BankUsersDataBase.addArminUser();
        BankUsersDataBase.addManiSupport();

        FirstMenu.runFirstMenu();
    }
}
