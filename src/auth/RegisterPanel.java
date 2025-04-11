package auth;
import main.Main;
import java.awt.CardLayout;
import utils.FileHandler;
import utils.User;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RegisterPanel extends JPanel {
	private JTextField fullname_signup;
	private Main main;
	
    public RegisterPanel(Main main, JPanel cardPanel, CardLayout cardLayout) {
        setLayout(null);
        setBackground(new Color(255, 255, 255));

        JLabel SignupLabel = new JLabel("Sign up ");
        SignupLabel.setHorizontalAlignment(SwingConstants.CENTER);
        SignupLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        SignupLabel.setBounds(158, 10, 218, 42);
        add(SignupLabel);

        JLabel EmailLabel = new JLabel("Email Address :");
        EmailLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        EmailLabel.setBounds(108, 56, 153, 35);
        add(EmailLabel);

        JLabel UsernameLabel = new JLabel("Username       :");
        UsernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        UsernameLabel.setBounds(108, 94, 153, 35);
        add(UsernameLabel);
        
        JLabel Fullnamelb = new JLabel("Full Name       :");
        Fullnamelb.setFont(new Font("Tahoma", Font.PLAIN, 18));
        Fullnamelb.setBounds(108, 139, 153, 35);
        add(Fullnamelb);
        
        JTextField fullname_signup = new JTextField();
        fullname_signup.setColumns(10);
        fullname_signup.setBounds(271, 139, 155, 29);
        add(fullname_signup);

        JLabel PasswordLabel2 = new JLabel("Password        :");
        PasswordLabel2.setFont(new Font("Tahoma", Font.PLAIN, 18));
        PasswordLabel2.setBounds(108, 184, 153, 35);
        add(PasswordLabel2);

        JTextField email_signup = new JTextField();
        email_signup.setBounds(271, 62, 155, 29);
        add(email_signup);
        email_signup.setColumns(10);

        JTextField username_signup = new JTextField();
        username_signup.setColumns(10);
        username_signup.setBounds(271, 100, 155, 29);
        add(username_signup);

        JPasswordField pwd_signup = new JPasswordField();
        pwd_signup.setColumns(10);
        pwd_signup.setBounds(271, 190, 155, 29);
        add(pwd_signup);

        JLabel lblRepassword = new JLabel("Re - Password :");
        lblRepassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblRepassword.setBounds(108, 224, 153, 35);
        add(lblRepassword);

        JPasswordField RePasswdLabel = new JPasswordField();
        RePasswdLabel.setColumns(10);
        RePasswdLabel.setBounds(271, 230, 155, 29);
        add(RePasswdLabel);

        JRadioButton rdbtnStudent = new JRadioButton("Student");
        rdbtnStudent.setFont(new Font("Tahoma", Font.PLAIN, 16));
        rdbtnStudent.setBounds(141, 265, 81, 21);
        add(rdbtnStudent);

        JRadioButton rdbtnLecturer = new JRadioButton("Lecturer");
        rdbtnLecturer.setFont(new Font("Tahoma", Font.PLAIN, 16));
        rdbtnLecturer.setBounds(309, 265, 83, 21);
        add(rdbtnLecturer);

        ButtonGroup userTypeGroup = new ButtonGroup();
        userTypeGroup.add(rdbtnStudent);
        userTypeGroup.add(rdbtnLecturer);
        
        JButton SignUpbtn = new JButton("SIGN UP");
        SignUpbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                User user = validateCredentials.validateAndGetData(username_signup, email_signup, 
                		fullname_signup, pwd_signup, RePasswdLabel, userTypeGroup, "Register Panel");
                if (user == null) {
                    return;
                }
                FileHandler.saveUserData(user.getEmail(), user.getUsername(), user.getFullName(), 
            							user.getPassword(), user.getRole());
                JOptionPane.showMessageDialog(
                		RegisterPanel.this, "Successful Sign Up", 
                		"SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                
                int choice = JOptionPane.showConfirmDialog(
                    RegisterPanel.this,
                    "Do you want to login?",
                    "Redirect to Login",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                    );
                
                if (choice == JOptionPane.YES_OPTION) {
                	if (user.getRole() == "Student") {
                		cardLayout.show(cardPanel, "Student Home Panel"); //show student home panel
                	}
                	else if (user.getRole() == "Lecturer"){
                		cardLayout.show(cardPanel, "Lecturer Home Panel"); //show lec home panel
                	}
                    User.setCurrentUser(user.getFullName());
                    
                	main.resizeFrameAfterLogin();
                	main.showMenuBar(user);
                     
                } else {
                    cardLayout.show(cardPanel, "Management Panel"); // show Management Panel
                }   
            }
        });

        SignUpbtn.setFont(new Font("Tahoma", Font.PLAIN, 19));
        SignUpbtn.setBounds(201, 292, 123, 35);
        add(SignUpbtn);
        
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

    private String getSelectedUserType(ButtonGroup group) {
        for (var button : java.util.Collections.list(group.getElements())) {
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }
}
