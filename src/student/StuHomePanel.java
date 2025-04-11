package student;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class StuHomePanel extends JPanel {

	public StuHomePanel(JPanel cardPanel, CardLayout cardLayout) {
		setLayout(new BorderLayout());
	}
	
	public void addStudentrMenuBar(JMenuBar menuBar, JPanel cardPanel, CardLayout cardLayout) {
	    // Clear existing menus to avoid duplication
	    menuBar.removeAll();

	    // Set menu items for Students
	    JMenu menu1 = new JMenu("Booking");
	    JMenuItem makeBookingItem = new JMenuItem("Make Booking");
	    makeBookingItem.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            cardLayout.show(cardPanel, "Make Booking Appointment Panel");
	        }
	    });
	    menu1.add(makeBookingItem);

	    JMenu menu2 = new JMenu("View Bookings");
	    JMenuItem viewBookings = new JMenuItem("View Bookings");
	    viewBookings.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            cardLayout.show(cardPanel, "Student View Booking Panel");
	        }
	    });
	    menu2.add(viewBookings);
	    
	    JMenu menu3 = new JMenu("History");
	    JMenuItem viewHistory = new JMenuItem("View History");
	    viewHistory.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            cardLayout.show(cardPanel, "Student Provide Feedback Panel");
	        }
	    });
	    menu3.add(viewHistory);
	    
	    JMenu menu4 = new JMenu("Feedback");
	    JMenuItem viewFeedback = new JMenuItem("View Feedback");
	    viewFeedback.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            cardLayout.show(cardPanel, "Student View Feedback Panel");
	        }
	    });
	    menu4.add(viewFeedback);

	    menuBar.add(menu1);
	    menuBar.add(menu2);
	    menuBar.add(menu3);
	    menuBar.add(menu4);

	    // Refresh menu bar
	    menuBar.revalidate();
	    menuBar.repaint();
	}

}
