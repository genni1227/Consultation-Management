package lecturer;

import javax.swing.*;

import utils.User;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class provideStuFeedback extends JPanel {
    private JTextArea feedbackField;
    private String studentName;
    private String appointmentDate;
    private String appointmentTime;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private pastAppointments pastAppointmentsPanel;

    public provideStuFeedback(String studentName, String appointmentDate, String appointmentTime, CardLayout cardLayout, JPanel cardPanel, pastAppointments pastAppointmentsPanel) {
        this.studentName = studentName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.pastAppointmentsPanel = pastAppointmentsPanel;

        initialize();
    }

    private void initialize() {
        setLayout(null);

        JLabel lblTitle = new JLabel("Give Student Feedback");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(155, 24, 370, 50);
        add(lblTitle);

        JLabel lblStudentName = new JLabel("Student Name: " + studentName);
        lblStudentName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblStudentName.setBounds(155, 95, 300, 20);
        add(lblStudentName);

        JLabel lblDate = new JLabel("Date: " + appointmentDate);
        lblDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblDate.setBounds(155, 155, 300, 20);
        add(lblDate);

        JLabel lblTime = new JLabel("Time: " + appointmentTime);
        lblTime.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTime.setBounds(155, 207, 300, 20);
        add(lblTime);

        feedbackField = new JTextArea();
        feedbackField.setBounds(155, 290, 400, 153);
        feedbackField.setLineWrap(true); // Enable line wrapping
        feedbackField.setWrapStyleWord(true); // Wrap at word boundaries
        feedbackField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(feedbackField);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnSubmit.setBounds(284, 465, 119, 39);
        btnSubmit.addActionListener(e -> submitFeedback());
        add(btnSubmit);
    }

    private void submitFeedback() {
    	String user = User.getCurrentUser();
        String feedback = feedbackField.getText().trim();

        if (feedback.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Feedback cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("lec_feedback.txt", true))) {
            writer.write(studentName + " | "+ user + " | " + appointmentDate + " | " + appointmentTime + " | " + feedback);
            writer.newLine();

            JOptionPane.showMessageDialog(this, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            pastAppointmentsPanel.updateFeedback(studentName, appointmentDate, appointmentTime);
            cardLayout.show(cardPanel, "View Past Appointments Panel");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving feedback: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
