package ir.ac.kntu;

import java.util.Objects;

public class Contact {

    private String firstName;
    private String lastName;
    private String phoneNum;

    public Contact() {
    }

    public Contact(String firstName, String lastName, String phoneNum) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
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

    @Override
    public String toString() {
        return "\nName: " + Colors.BLUE + getFirstName() + " " + getLastName() + Colors.RESET +
                "\nPhone Number: " + Colors.BLUE + getPhoneNum() + Colors.RESET +
                "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Contact contact)) {
            return false;
        }
        return Objects.equals(phoneNum, contact.phoneNum);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(phoneNum);
    }
}
