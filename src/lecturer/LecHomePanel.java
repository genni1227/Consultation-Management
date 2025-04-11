package lecturer;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class LecHomePanel extends JPanel {

    public LecHomePanel(JPanel cardPanel, CardLayout cardLayout) {
    	setLayout(new BorderLayout());
    }

    public void addLecturerMenuBar(JMenuBar menuBar, JPanel cardPanel, CardLayout cardLayout) {
    	// Clear existing menus to avoid duplication
    	menuBar.removeAll();
    	// Set menu items for Lecturer
        JMenu menu1 = new JMenu("Manage Consultations");
        
        JMenuItem manageconsultations = new JMenuItem("Manage Consultations");
        manageconsultations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Manage Consultation Panel");
            }
        });
        menu1.add(manageconsultations);
        
        JMenu menu2 = new JMenu("View Appointments");
        
        JMenuItem upcomingAppointments = new JMenuItem("Upcoming Appointment");
        upcomingAppointments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the "Upcoming Appointments" panel
                cardLayout.show(cardPanel, "View Upcoming Appointments Panel");
            }
        });
        menu2.add(upcomingAppointments);
        
        JMenuItem pastAppointments = new JMenuItem("Past Appointment");
        pastAppointments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the "Past Appointments" panel
                cardLayout.show(cardPanel, "View Past Appointments Panel");
            }
        });
        menu2.add(pastAppointments);
        
        JMenu menu3 = new JMenu("Appointment Request");
        
        JMenuItem slotRequest = new JMenuItem("Consultation Slot Request");
        slotRequest.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
			      // Switch to the "View Pending Request Panel"
	              cardLayout.show(cardPanel, "Manage Appointment Request Panel");
        	}
	  	});
        menu3.add(slotRequest);
        
        JMenuItem rescheduleRequest = new JMenuItem("Reschedule Consultation Request");
        rescheduleRequest.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
			      // Switch to the "View Reschedule Request Panel"
	              cardLayout.show(cardPanel, "Reschedule Slot Request Panel");
        	}
	  	});
        menu3.add(rescheduleRequest);
        
        JMenu menu4 = new JMenu("View Feedback");

        JMenuItem viewFeedback = new JMenuItem("View Student Feedback");
        viewFeedback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the "View Student Feedback Panel"
                cardLayout.show(cardPanel, "View Student Feedback Panel");
            }
        });
        menu4.add(viewFeedback); // Add the item to the "View Feedback" menu

        // Add all menus to the menu bar
        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);
        menuBar.add(menu4);
    }
}
