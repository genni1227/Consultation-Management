package auth;
import utils.User;
import main.Main;
import utils.FileHandler;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class StudentLoginPanel extends JPanel {
    private Main main;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public StudentLoginPanel(Main main, JPanel cardPanel, CardLayout cardLayout) {
        this.main = main;
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel loginLabel = new JLabel("Login (Student)");
        loginLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        loginLabel.setBounds(174, 57, 189, 37);
        add(loginLabel);

        JLabel usernameLabel = new JLabel("Username :");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        usernameLabel.setBounds(122, 120, 119, 21);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(251, 120, 155, 29);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password  :");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        passwordLabel.setBounds(122, 180, 119, 21);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(251, 172, 155, 29);
        add(passwordField);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
        loginButton.setBounds(221, 224, 105, 29);
        add(loginButton);

        loginButton.addActionListener(e -> {
            User user = validateCredentials.validateAndGetData(usernameField, null, null, 
            		passwordField, null, null, "Student Login Panel");

            if (user != null) {
                String username = user.getUsername();
                String password = user.getPassword();
                String role = user.getRole();

                try {
                    if (validateCredentials.validateLogin(username, password, role)) {
                        JOptionPane.showMessageDialog(StudentLoginPanel.this, "Login successful!", 
                        		"Success", JOptionPane.INFORMATION_MESSAGE);
                        
                        String fullname = FileHandler.getFullNameFromFile(username);
                        User.setCurrentUser(fullname);
                        
                        cardLayout.show(cardPanel, "Student Home Panel");
                        main.resizeFrameAfterLogin();
                        main.showMenuBar(user); // showMenuBar according to user role
                        
                    } else {
                        JOptionPane.showMessageDialog(StudentLoginPanel.this, 
                        		"Invalid login credentials", "Error", 
                        		JOptionPane.ERROR_MESSAGE);
                    }
                } catch (HeadlessException | IOException e1) {
                    e1.printStackTrace();
                }
            } 
            // repeated
//            else {
//                JOptionPane.showMessageDialog(StudentLoginPanel.this, "Please fill in all fields", 
//                		"Error", JOptionPane.ERROR_MESSAGE);
//            }
        });

        JLabel RegisterLabel = new JLabel("<html>Don't have an account? <a href>Sign up here</a></html>");

        RegisterLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "Register Panel");
            }
        });

        RegisterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        RegisterLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        RegisterLabel.setBounds(140, 265, 255, 29);
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
