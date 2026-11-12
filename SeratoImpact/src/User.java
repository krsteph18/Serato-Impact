public class User {

    private String userID;
    private String username;
    private String email;
    private String password;

    public User(String userID, String username, String email, String password) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean login() {
        return true;
    }

    public boolean logout() {
        return true;
    }

}
