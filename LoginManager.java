package smartpark.manager;

import smartpark.model.UserAccount;

import java.util.ArrayList;

public class LoginManager {
    private ArrayList<UserAccount> users;
    private String loggedInName;

    public LoginManager() {
        users = new ArrayList<UserAccount>();
        loggedInName = "";
    }

    public boolean register(String name, String username,
                            String password, String contact) {
        if (name.trim().equals("") || username.trim().equals("")
                || password.equals("") || contact.trim().equals("")) {
            return false;
        }

        if (usernameExists(username)) {
            return false;
        }

        UserAccount account = new UserAccount(
                name.trim(), username.trim(), password, contact.trim());
        users.add(account);
        return true;
    }

    public boolean usernameExists(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean login(String username, String password) {
        if (username.trim().equals("") || password.equals("")) {
            return false;
        }

        for (int i = 0; i < users.size(); i++) {
            UserAccount account = users.get(i);

            if (account.getUsername().equals(username.trim())
                    && account.getPassword().equals(password)) {
                loggedInName = account.getName();
                return true;
            }
        }
        return false;
    }

    public String getLoggedInName() {
        return loggedInName;
    }
}
