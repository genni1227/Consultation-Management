package auth;
import utils.FileHandler;
import utils.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class validateCredentials {

    private Object username;
    private Object password;
    private Object role;
    private Object email;

    //not used?
    public boolean validateCredentials(String username, String email, String password, String role) {
        // First, check username and password; if email is provided, also check that
        if (this.username.equals(username) && this.password.equals(password) && this.role.equals(role)) {
            // If email is required and provided, check it
            return email == null || this.email.equals(email);
        }
        return false;
    }

    public static boolean validateLogin(String username, String password, String role) throws IOException {
        try (BufferedReader bf = new BufferedReader(new FileReader("credentials.txt"))) {
            String line;
            while ((line = bf.readLine()) != null) {
                try {
                    User userFromFile = User.fromFile(line);

                    // Validate with username, password, and role
                    if (userFromFile.validateCredentials(username, null, password, role)) {
                        return true;
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        }
        return false;
    }

    public static boolean validateLogin(String username, String email, String password, String role) throws IOException {
        try (BufferedReader bf = new BufferedReader(new FileReader("credentials.txt"))) {
            String line;
            while ((line = bf.readLine()) != null) {
                try {
                    User userFromFile = User.fromFile(line);

                    // Validate with username, email, password, and role
                    if (userFromFile.validateCredentials(username, email, password, role)) {
                        return true;
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        }
        return false;
    }

    public static User validateAndGetData(
        JTextField usernameField, JTextField emailField, JTextField fullnameField,
        JTextField passwordField, JTextField repasswordField, ButtonGroup userTypeGroup, String panelType) {

        String username = usernameField != null ? usernameField.getText().trim() : null;
        String email = emailField != null ? emailField.getText().trim() : null;
        String fullname = fullnameField != null ? fullnameField.getText().trim() : null;
        String password = passwordField != null ? passwordField.getText().trim() : null;
        String repassword = repasswordField != null ? repasswordField.getText().trim() : null;
        String userType = getSelectedUserType(userTypeGroup); // Used for register panel

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (username.contains(" ") || password.contains(" ")) {
            JOptionPane.showMessageDialog(null, "All fields cannot contain spaces!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        switch (panelType) {
            case "Student Login Panel":
            	User.setCurrentUser(fullname);
                return new User(username, null, password, "Student");

            case "Lecturer Login Panel":
                if (email == null || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                User.setCurrentUser(fullname);
                return new User(username, email, password, "Lecturer");

            case "Register Panel":
            	
            	//------Repeated-------------------------------------------------------------
            	if (fullname == null || fullname.isEmpty()) {
            		JOptionPane.showMessageDialog(null, "All fields are required!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return null;
            	}
            	///////////////////////////////////////////////////////////////////
            	
            	if (fullname.matches(".*\\d.*")) {
            		JOptionPane.showMessageDialog(null, "Full name cannot contain digits!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return null;
            	}
            	
                if (email == null || email.isEmpty() || password == null || password.isEmpty() || fullname == null || fullname.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                if (!email.matches(".+@\\w+\\.com$")) {
                    JOptionPane.showMessageDialog(null, "Invalid email format!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                if (email.contains(" ") || password.contains(" ") || username.contains(" ")) {
                    JOptionPane.showMessageDialog(null, "All fields cannot contain spaces!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                
                if(!password.equals(repassword)) {
                	JOptionPane.showMessageDialog(null, "Password and Re Password must be the same!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                
                if (userType == null || userType.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "User role selection is required!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                
                if (FileHandler.isUsernameTaken(username)) {
                    JOptionPane.showMessageDialog(null, "This username is already taken. Please choose another one.", "ERROR", 
                    		JOptionPane.ERROR_MESSAGE);
                    return null;
                
                }
                
//                User.setCurrentUser(fullname);
                return new User(username, email, fullname, password, userType);
                

            default:
                JOptionPane.showMessageDialog(null, "Invalid panel type!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return null;
        }
    }

    private static String getSelectedUserType(ButtonGroup group) {
        if (group == null) {
//        	 JOptionPane.showMessageDialog(null, "User type selection is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        for (var button : java.util.Collections.list(group.getElements())) {
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }
    
    
}
