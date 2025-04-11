package main;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ManagementSystem extends JPanel {
    public ManagementSystem(JPanel cardPanel, CardLayout cardLayout) {
        setLayout(null);
        setBackground(new Color(243, 119, 98));

        JLabel title1Label = new JLabel("Welcome to APU Psychology Consultation");
        title1Label.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        title1Label.setBounds(51, 32, 458, 40);
        add(title1Label);

        JLabel title2Label = new JLabel("Management System");
        title2Label.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        title2Label.setBounds(176, 72, 226, 28);
        add(title2Label);

        JLabel roleLabel = new JLabel("Are you a student or a lecturer?");
        roleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        roleLabel.setBounds(119, 143, 365, 57);
        add(roleLabel);

        JToggleButton studentButton = new JToggleButton("Student");
        studentButton.setFont(new Font("Gadugi", Font.BOLD, 14));
        studentButton.setBounds(108, 251, 120, 28);
        add(studentButton);
        studentButton.addActionListener(e -> cardLayout.show(cardPanel, "Student Login Panel"));

        JToggleButton lecturerButton = new JToggleButton("Lecturer");
        lecturerButton.setFont(new Font("Gadugi", Font.BOLD, 14));
        lecturerButton.setBounds(325, 251, 120, 28);
        add(lecturerButton);
        lecturerButton.addActionListener(e -> cardLayout.show(cardPanel, "Lecturer Login Panel"));
    }
}
