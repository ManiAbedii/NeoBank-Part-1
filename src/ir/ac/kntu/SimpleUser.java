package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

enum AuthenticationStatus {
    OK("ok"),
    UNCHECKED("unchecked");

    private String displayName;
    AuthenticationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

public class SimpleUser {

    //Personal Records:
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String socialNum;
    private String password;
    private String authenStatus;
    //this field is for Account part (below)
    private Account account;

    public SimpleUser(String firstName, String lastName, String phoneNum, String socialNum, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.socialNum = socialNum;
        this.password = password;
        this.contactsEnabled = true;
        this.authenStatus = AuthenticationStatus.UNCHECKED.getDisplayName(); //"unchecked"
        this.account = new Account(phoneNum);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSocialNum() {
        return socialNum;
    }

    public void setSocialNum(String socialNum) {
        this.socialNum = this.socialNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthenStatus() {
        return authenStatus;
    }

    public void setAuthenStatus(String authenStatus) {
        this.authenStatus = authenStatus;
    }

    public String showUserInfo() {
        String output = this.toString() +
                "Account Number: " + Colors.BLUE + this.getAccount().getAccountNumber() + Colors.RESET;
        if (this.getAccount().getrecentTransacs().isEmpty()) {
            output += "\nThis user has not performed any transactions recently\n";
        } else if (this.getAccount().getrecentTransacs().size() >= 3) {
            output += "\nRecent 3 Transactions:\n ";
            for (int i = this.getAccount().getrecentTransacs().size() - 3; i < this.getAccount().getrecentTransacs().size(); i++) {
                output += this.getAccount().getrecentTransacs().get(i) + "\n";
            }
        } else {
            output += "\nRecent Transactions:\n ";
            for (Transaction transaction : this.getAccount().getrecentTransacs()) {
                if (transaction.getType().equals(Type.RECEIVE)) {
                    output += transaction.disReceive();
                } else if (transaction.getType().equals(Type.TRANSFER)) {
                    output += transaction.disTransfer();
                } else {
                    output += transaction.disDeposit();
                }
            }
        }
        return output;
    }

    //Overrides:
    @Override
    public String toString() {
        return Colors.RESET + "Full Name: " + Colors.BLUE + getFirstName() + " " + getLastName() + Colors.RESET
                + "\nPhone Number: " + Colors.BLUE + getPhoneNum() + Colors.RESET
                + "\nSSN: " + Colors.BLUE + getSocialNum() + Colors.RESET
                + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof SimpleUser userObj) {
            return userObj.getPhoneNum().equals(this.getPhoneNum())
                    || userObj.getSocialNum().equals(this.getSocialNum());
        }
        return false;
    }

    //Account:
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    //Contacts:
    private List<Contact> contacts = new ArrayList<>();
    private boolean contactsEnabled;

    public List<Contact> getContacts() {
        return new ArrayList<>(contacts);
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public boolean isContactsEnabled() {
        return contactsEnabled;
    }

    public void setContactsEnabled(boolean contactsEnabled) {
        this.contactsEnabled = contactsEnabled;
    }

    public void removeContact(int index) {
        contacts.remove(index);
    }

    public void editContact(int index) {
        String firstName, lastName;
        Contact contact = contacts.get(index);
        System.out.println("\n" + Colors.BLUE_BACKGROUND + "Edit Contact" + Colors.RESET);
        System.out.print("Enter new First Name: ");
        firstName = Helper.SCANNER.nextLine();
        System.out.print("Enter new Last Name: ");
        lastName = Helper.SCANNER.nextLine();

        contact.setFirstName(firstName);
        contact.setLastName(lastName);

        changeReceiptsNames(contact, firstName, lastName);

        System.out.println(Colors.GREEN + "Contact Info was edited successfully\n" + Colors.RESET);
    }

    private void changeReceiptsNames(Contact contact, String firstName, String lastName) {
        SimpleUser contactUser = null;
        for (Map.Entry<String, SimpleUser> entry : BankUsersDataBase.getSimpleUsers().entrySet()) {
            if (entry.getValue().getPhoneNum().equals(contact.getPhoneNum())) {
                contactUser = entry.getValue();
                break;
            }
        }

        for (Transaction transaction : this.account.getrecentTransacs()) {
            if (transaction.getType().equals(Type.TRANSFER)) {
                if (transaction.getReceiverAccNum().equals(contactUser.getAccount().getAccountNumber())) {
                    transaction.setReceiverName(firstName + " " + lastName);
                }
            }
        }
    }

}
