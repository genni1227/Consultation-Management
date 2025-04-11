package utils;
public class User {
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String role;
    private static String currentUser;

    public User(String username, String email, String fullname, String password, String role) {
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.role = role;
    }
    
    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static void setCurrentUser(String fullname) {
        currentUser = fullname;
    }

    public static  String getCurrentUser() {
        return currentUser;
    }
    
    public static User fromFile(String line) {
        String[] parts = line.split(", ");
        if (parts.length != 5) { // Updated to expect only 4 parts (no fullname)
            throw new IllegalArgumentException("Invalid line format for User data");
        }
        return new User(
            parts[1].trim(), // username
            parts[0].trim(), // email
            parts[2].trim(), // fullname
            parts[3].trim(), // password
            parts[4].trim()  // role
        );
    }

    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", role='" + role + '\'' +
            '}';
    }

    // Consolidated validate method
    public boolean validateCredentials(String username, String email, String password, String role) {
        return this.username.equals(username) &&
            this.password.equals(password) &&
            this.role.equals(role) &&
            (email == null || this.email.equals(email));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
