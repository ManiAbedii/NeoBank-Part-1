package ir.ac.kntu;

enum TicketState {
    SUBMITTED("Submitted"),
    IN_PROGRESS("In progress"),
    CLOSED("Closed");

    private String displayName;

    TicketState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

enum Section {
    REPORTS("Reports"),
    CONTACTS("Contacts"),
    TRANSFER("Transfer"),
    SETTINGS("Settings");

    private String displayName;

    Section(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

public class Ticket {

    private String subject;
    private SimpleUser applicantUser;
    private String explanation;
    private Section section;
    private String response;
    private TicketState state;

    public Ticket() {
        this.response = "";
        this.state = TicketState.SUBMITTED;
    }

    public Ticket(SimpleUser user) {
        this.applicantUser = user;
        this.response = "";
        this.state = TicketState.SUBMITTED;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        while (subject.length() > 25) {
            System.out.println(Colors.RED + "Entered subject is too long" + Colors.RESET);
            System.out.println("try again: ");
            subject = Helper.SCANNER.nextLine();
        }
        this.subject = subject;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public SimpleUser getApplicantUser() {
        return applicantUser;
    }

    public TicketState getState() {
        return state;
    }

    public void setState(TicketState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        String output;
        output = "Ticket Explanation:\n" + Colors.BLUE + this.getExplanation() + Colors.RESET +
                "\nApplicant User:\n" + Colors.BLUE + this.getApplicantUser() + Colors.RESET +
                "\nTicket State: " + Colors.BLUE + this.getState().getDisplayName() + Colors.RESET;
        if (this.getResponse().isEmpty()) {
            output += "\nNo Responses to this query yet\n";
        } else {
            output += "\nSupport User's Response: " + Colors.BLUE + this.getResponse() + Colors.RESET + "\n\n";
        }
        return output;
    }

    public String showTicketToUserHimSelf() {
        String output;
        output = "Ticket Explanation:\n" + Colors.BLUE + this.getExplanation() + Colors.RESET +
                "\nTicket State: " + Colors.BLUE + this.getState().getDisplayName() + Colors.RESET;
        if (this.getResponse().isEmpty()) {
            output += "\nNo Responses to this query yet\n";
        } else {
            output += "\nSupport User's Response: " + Colors.BLUE + this.getResponse() + Colors.RESET + "\n\n";
        }
        return output;
    }
}
