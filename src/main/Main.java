package main;
import java.awt.BorderLayout;
import auth.LecturerLoginPanel;
import auth.StudentLoginPanel;
import auth.RegisterPanel;
import utils.User;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import student.*;
import lecturer.*;

public class Main {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JMenuBar menuBar; 
    private StuHomePanel stuhomepanel;
    private LecHomePanel lechomepanel;
    private viewStuFeedback viewStuFeedbackInstance;
    private boolean isDarkMode = false; // Set this to true when dark mode is activated

    public Main() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("APU Psychology Consultation Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 553, 379);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.WHITE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        frame.getContentPane().add(cardPanel);
        cardPanel.setBackground(Color.WHITE);

        // Create panels and add to card layout
        ManagementSystem managementSystem = new ManagementSystem(cardPanel, cardLayout);
        cardPanel.add(managementSystem, "Management Panel");

        //------------Authentication Panel-----------------------------
        StudentLoginPanel studentLoginPanel = new StudentLoginPanel(this, cardPanel, cardLayout);
        cardPanel.add(studentLoginPanel, "Student Login Panel");

        LecturerLoginPanel lecturerLoginPanel = new LecturerLoginPanel(this, cardPanel, cardLayout);
        cardPanel.add(lecturerLoginPanel, "Lecturer Login Panel");
        
        RegisterPanel registerpanel = new RegisterPanel(this, cardPanel, cardLayout);
        cardPanel.add(registerpanel, "Register Panel");

        //----------Student Panel-----------------------------------
        stuhomepanel = new StuHomePanel(cardPanel, cardLayout);
        cardPanel.add(stuhomepanel, "Student Home Panel");
        
        makeBooking makebooking = new makeBooking(cardPanel, cardLayout);
        cardPanel.add(makebooking, "Make Booking Appointment Panel");
        
        StuViewBooking stuviewBooking = new StuViewBooking(cardPanel, cardLayout);
        cardPanel.add(stuviewBooking, "Student View Booking Panel");
        
        ReScheduleAppt sturescheduleappt = new ReScheduleAppt(cardPanel, cardLayout);
        cardPanel.add(sturescheduleappt, "Student Reschedule Appointment Panel");
        
        provideLecFeedback providelecfeedback = new provideLecFeedback (cardPanel, cardLayout);
        cardPanel.add(providelecfeedback, "Student Provide Feedback Panel");
        
        viewLecFeedback viewlecfeedback = new viewLecFeedback(cardPanel, cardLayout);
        cardPanel.add(viewlecfeedback, "Student View Feedback Panel");
        
        //----------Lecturer Panel -------------------------------------
        lechomepanel = new LecHomePanel(cardPanel, cardLayout);
        cardPanel.add(lechomepanel, "Lecturer Home Panel");
        
        manageConsultation manageconsultation = new manageConsultation(cardPanel, cardLayout);
        cardPanel.add(manageconsultation, "Manage Consultation Panel");
        
        appointmentReq appointmentreq = new appointmentReq(cardPanel, cardLayout);
        cardPanel.add(appointmentreq, "Manage Appointment Request Panel");
        
        rescheduleAppt rescheduleappt = new rescheduleAppt(cardPanel, cardLayout);
        cardPanel.add(rescheduleappt, "Reschedule Slot Request Panel");
        
        upcomingAppt upcomingappt = new upcomingAppt(cardPanel, cardLayout);
        cardPanel.add(upcomingappt, "View Upcoming Appointments Panel");
        
        pastAppointments pastappt = new pastAppointments(cardPanel, cardLayout);
        cardPanel.add(pastappt, "View Past Appointments Panel");
       
        viewStuFeedback viewstufeedback = new viewStuFeedback(cardPanel, cardLayout);
        cardPanel.add(viewstufeedback, "View Student Feedback Panel");
        
        viewStuFeedbackInstance = new viewStuFeedback(cardPanel, cardLayout);
        cardPanel.add(viewStuFeedbackInstance, "View Student Feedback Panel");

        ///////////////////////////////////////////////////////////////////////
        frame.setVisible(true);

    }
    
    public void resizeFrameAfterLogin() {
        // Resize the JFrame to a larger size after successful login
    	frame.setBounds(100, 100, 700, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void resizeFrameAfterLogout() {
        // Resize the JFrame to a smaller size after successful logout
        frame.setBounds(100, 100, 553, 379); 
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void showMenuBar(User user) {
    	JMenuBar menuBar = new JMenuBar();
    	
    	// Set different menu bar for different role
	    if (user != null) {
	        String role = user.getRole();

	        if (role.equals("Student")) {
	        	stuhomepanel.addStudentrMenuBar(menuBar, cardPanel, cardLayout);
	        } 
	    
	        else if (role.equals("Lecturer")) {
	             lechomepanel.addLecturerMenuBar(menuBar, cardPanel, cardLayout);
	        }

	    };
	    
	    JButton logoutButton = new JButton("Logout");
	    logoutButton.addActionListener(e -> {
	        int confirm = JOptionPane.showConfirmDialog(
	            frame, 
	            "Are you sure you want to log out?", 
	            "Logout Confirmation", 
	            JOptionPane.YES_NO_OPTION
	        );

	        if (confirm == JOptionPane.YES_OPTION) {
	        	 // Reset background color to light mode before switching
	            lightMode();
	            cardPanel.setBackground(Color.WHITE);
	            frame.revalidate();
	            frame.repaint();
	            // Switch to the Management Panel after logging out
	            cardLayout.show(cardPanel, "Management Panel");

	            hideMenuBar();
	            resizeFrameAfterLogout();
	            viewStuFeedbackInstance.clearFeedback();
	        }
	    });

        // Create a dark mode/light mode toggle button
        JToggleButton modeToggleButton = new JToggleButton("Dark Mode");
        modeToggleButton.addActionListener(e -> {
            if (modeToggleButton.isSelected()) {
                modeToggleButton.setText("Light Mode");
                darkMode();
            } else {
                modeToggleButton.setText("Dark Mode");
                lightMode();
            }
        });
	    
	    // Add the logout button to a right-aligned panel
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
	    logoutPanel.add(modeToggleButton); 
	    logoutPanel.add(logoutButton, BorderLayout.EAST);
	    logoutPanel.setOpaque(false); // Make it blend with the menu bar

	    menuBar.add(logoutPanel);
	    frame.setJMenuBar(menuBar);
	    frame.revalidate();
	    frame.repaint();
    }
    
    public void hideMenuBar() {
    	frame.setJMenuBar(null);
    }

    private void updateMode(Color backgroundColor, Color foregroundColor, Color menuBackground, Color menuForeground) {
        // Set the background for the frame and card panel
        frame.getContentPane().setBackground(backgroundColor);
        cardPanel.setBackground(backgroundColor);

        // Update JOptionPane UI for the selected mode
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("OptionPane.messageForeground", foregroundColor);

        // Set the menu bar background and font color
        if (frame.getJMenuBar() != null) {
            frame.getJMenuBar().setBackground(menuBackground);
            frame.getJMenuBar().setForeground(menuForeground);

            // Iterate through each menu and menu item to apply the colors
            for (int i = 0; i < frame.getJMenuBar().getMenuCount(); i++) {
                JMenu menu = frame.getJMenuBar().getMenu(i);
                if (menu != null) {
                    menu.setBackground(menuBackground);
                    menu.setForeground(menuForeground);

                    for (int j = 0; j < menu.getItemCount(); j++) {
                        JMenuItem menuItem = menu.getItem(j);
                        if (menuItem != null) {
                            menuItem.setBackground(menuBackground);
                            menuItem.setForeground(menuForeground);
                        }
                    }
                }
            }
        }

        // Update other components in the cardPanel if needed (if using JPanel etc.)
        for (Component component : cardPanel.getComponents()) {
            updateComponentColors(component, foregroundColor, backgroundColor);
            if (component instanceof JPanel) {
                component.setBackground(backgroundColor);
                component.setForeground(foregroundColor);
            }
        }
    }
    
    private void updateComponentColors(Component component, Color foreground, Color background) {
        if (component instanceof JPanel) {
            component.setBackground(background);
            component.setForeground(foreground);
            for (Component child : ((JPanel) component).getComponents()) {
                updateComponentColors(child, foreground, background);
            }
        } else if (component instanceof JLabel || component instanceof JTextArea) {
            component.setForeground(foreground);
            component.setBackground(background); // Optional, for JTextArea or similar
        } else if (component instanceof JButton) {
            JButton button = (JButton) component;
            if (isDarkMode) { // This variable should be set to true when dark mode is active
                button.setBackground(new Color(50, 50, 50)); // Dark mode button color
                button.setForeground(Color.WHITE); // Dark mode button text color
            } else {
                // Use the default color for light mode
                button.setBackground(UIManager.getColor("Button.background"));
                button.setForeground(UIManager.getColor("Button.foreground"));
            }
        } else if (component instanceof JScrollPane) {
            component.setBackground(background);
            JScrollPane scrollPane = (JScrollPane) component;
            scrollPane.getViewport().setBackground(background); // Update viewport background
        }
    }

    public void darkMode() {
        isDarkMode = true;
        updateMode(new Color(28, 30, 33), Color.WHITE, Color.DARK_GRAY, Color.WHITE);
    }

    public void lightMode() {
        isDarkMode = false;
        updateMode(Color.WHITE, Color.BLACK, Color.LIGHT_GRAY, Color.BLACK);
    }

    public static void main(String[] args) throws IOException {
       new Main();
    }
}
