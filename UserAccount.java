package smartpark.model;

// Stores the information entered on the register form.
public class UserAccount {
    private String name;
    private String username;
    private String password;
    private String contact;

    public UserAccount(String name, String username,
                       String password, String contact) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getContact() {
        return contact;
    }
}
