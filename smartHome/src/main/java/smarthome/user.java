package smarthome;

public class user {
    protected String username;
    protected String password;
    protected boolean loggedIn = false;

    public user(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Attempt login with provided password
    public boolean login(String inputPassword) {
        if (this.password.equals(inputPassword)) {
            loggedIn = true;
            System.out.println(username + " logged in successfully.");
            return true;
        } else {
            System.out.println("Login failed for " + username);
            return false;
        }
    }

    // Log the user out
    public void logout() {
        loggedIn = false;
        System.out.println(username + " logged out.");
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return loggedIn;
    }

    // Get the username
    public String getUsername() {
        return username;
    }
}
