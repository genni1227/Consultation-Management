package auth;
import utils.FileHandler;
import main.Main;
import utils.User;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;
import java.awt.event.ActionEvent;

public class LecturerLoginPanel extends JPanel {
	private JTextField usernameField;
	private JTextField emailField;
	private JTextField passwordField;
	private Main main;
	
    public LecturerLoginPanel(Main main, JPanel cardPanel, CardLayout cardLayout) {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel loginLabel = new JLabel("Login (Lecturer)");
        loginLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        loginLabel.setBounds(175, 40, 189, 37);
        add(loginLabel);

        JLabel usernameLabel = new JLabel("Username :");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        usernameLabel.setBounds(128, 103, 119, 21);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(257, 103, 155, 29);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password  :");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        passwordLabel.setBounds(128, 194, 119, 21);
        add(passwordLabel);

        passwordField= new JPasswordField();
        passwordField.setBounds(257, 194, 155, 29);
        add(passwordField);
        

        JLabel emailLabel = new JLabel("Email        :");
        emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        emailLabel.setBounds(128, 150, 119, 21);
        add(emailLabel);
        
        emailField = new JTextField();
        emailField.setBounds(257, 142, 155, 29);
        add(emailField);
        
        JButton loginButton = new JButton("LOGIN");
        loginButton.addActionListener(e -> {
            // Call validateAndGetData to retrieve the User object based on the entered credentials
            User user = validateCredentials.validateAndGetData(usernameField, emailField, null, 
            		passwordField, null, null, "Lecturer Login Panel");

            if (user != null) {
                String username = user.getUsername();
                String email = user.getEmail();
                String password = user.getPassword();
                String role = user.getRole();

                try {
                    // Validate login credentials
                    if (validateCredentials.validateLogin(username, email, password, role)) {
                        JOptionPane.showMessageDialog(LecturerLoginPanel.this, 
                        		"Login successful!", 
                        		"Success", 
                        		JOptionPane.INFORMATION_MESSAGE);
                        // Switch to Lecturer Home Panel
                        String fullname = FileHandler.getFullNameFromFile(username);
                        User.setCurrentUser(fullname);
                        
                        cardLayout.show(cardPanel, "Lecturer Home Panel");
                        main.resizeFrameAfterLogin();
                        main.showMenuBar(user); // showMenuBar according user role
                        
                    } else {
                        JOptionPane.showMessageDialog(LecturerLoginPanel.this, 
                        		"Invalid login credentials", 
                        		"Error", 
                        		JOptionPane.ERROR_MESSAGE);
                    }
                } catch (HeadlessException | IOException e1) {
                    e1.printStackTrace();
                }
            } 
//            else {
//                JOptionPane.showMessageDialog(LecturerLoginPanel.this, 
//                		"Please fill in all fields", 
//                		"Error", 
//                		JOptionPane.ERROR_MESSAGE);
//            }
        });

        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
        loginButton.setBounds(223, 240, 105, 29);
        add(loginButton);
        
        JLabel RegisterLabel = new JLabel("<html>Don't have an account? <a href>Sign up here</a></html>");

        RegisterLabel.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		cardLayout.show(cardPanel, "Register Panel");
    		}
        });
        
        RegisterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        RegisterLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        RegisterLabel.setBounds(139, 276, 255, 29);
        RegisterLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set Mouse Hover
        add(RegisterLabel);
        
        JButton btnReturn = new JButton("Return");
        btnReturn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		cardLayout.show(cardPanel, "Management Panel");
        	}
        });
        btnReturn.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnReturn.setBounds(10, 10, 85, 29);
        add(btnReturn);
    }
}
