package student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import utils.User;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class StuViewBooking extends JPanel {

    private static final Logger logger = Logger.getLogger(StuViewBooking.class.getName());
    private JTable bookingTable;
    private JButton rescheduleButton;
    private JButton cancelButton;
    private JButton refreshButton;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public StuViewBooking(JPanel cardPanel, CardLayout cardLayout) {
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        initComponents();
    }

    private void initComponents() {
        JLabel jLabel1 = new JLabel("View Your Booking");
        jLabel1.setFont(new java.awt.Font("SimSun", Font.BOLD, 36));

        JScrollPane jScrollPane4 = new JScrollPane();
        bookingTable = new JTable();
        bookingTable.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Lecturer", "Date", "Time", "Status"}
        ));
        jScrollPane4.setViewportView(bookingTable);

        rescheduleButton = new JButton("Reschedule Consultation");
        rescheduleButton.addActionListener(this::rescheduleButtonActionPerformed);

        cancelButton = new JButton("Cancel Booking");
        cancelButton.addActionListener(this::cancelButtonActionPerformed);

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadTableData());

        // Layout Setup
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(176)
                    .addComponent(jLabel1))
                .addGroup(layout.createSequentialGroup()
                    .addGap(20)
                    .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 650, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addGap(70)
                    .addComponent(refreshButton)
                    .addGap(50)
                    .addComponent(rescheduleButton)
                    .addGap(50)
                    .addComponent(cancelButton))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(20)
                    .addComponent(jLabel1)
                    .addGap(30)
                    .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                    .addGap(30)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(refreshButton)
                        .addComponent(rescheduleButton)
                        .addComponent(cancelButton)))
        );
    }

    public void loadTableData() {
        File file = new File("appointments.txt");

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean hasBookingData = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        	String user = User.getCurrentUser();
            String line;
            List<Object[]> rowData = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 5 && parts[0].trim().equals(user)) {
                    String lecturer = parts[1].trim();
                    String date = parts[2].trim();
                    String time = parts[3].trim();
                    String status = parts[4].trim();
                    rowData.add(new Object[]{lecturer, date, time, status});
                    hasBookingData = true;
                }
            }

            Object[][] data = rowData.toArray(new Object[0][]);
            String[] columnNames = {"Lecturer", "Date", "Time", "Status"};
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };
            bookingTable.setModel(model);

            if (hasBookingData) {
            	JOptionPane.showMessageDialog(this, "Successfully updated data.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
            	JOptionPane.showMessageDialog(this, "Currently you don't have any bookings", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading the appointment file.", "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.SEVERE, "Error loading table data", e);
        }
    }

    private void rescheduleButtonActionPerformed(ActionEvent evt) {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to reschedule.");
            return;
        }

        String status = bookingTable.getValueAt(selectedRow, 3).toString();
        if ("Pending".equals(status) || "Cancel".equals(status) ||
    		"Canceled".equals(status) || "Rescheduled".equals(status) || 
    		"Reschedule".equals(status) || "Reject".equals(status) || "Rejected".equals(status)) {
            JOptionPane.showMessageDialog(this, "Cannot reschedule bookings with status: " + status);
            return;
        }
        
//     // Extract details from the selected row
        String selectedLecturer = bookingTable.getValueAt(selectedRow, 0).toString();
        String selectedDate = bookingTable.getValueAt(selectedRow, 1).toString();
        String selectedTime = bookingTable.getValueAt(selectedRow, 2).toString();

        String[] timeParts = selectedTime.split("-");
        if (timeParts.length == 2) { 
            String timeFrom = timeParts[0].trim();
            String timeTo = timeParts[1].trim();
            
            ReScheduleAppt reschedulepage =(ReScheduleAppt) cardPanel.getComponent(7);
            reschedulepage.setDetails(selectedLecturer, selectedDate, timeFrom, timeTo);
//            System.out.println("You are here");
            cardLayout.show(cardPanel, "Student Reschedule Appointment Panel"); // Navigate to the reschedule panel
//            Manage Consultation Panel
//            Student Reschedule Appointment Panel
//            System.out.println("Or here");
        }
    }

    private void cancelButtonActionPerformed(ActionEvent evt) {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.");
            return;
        }
        
        String status = bookingTable.getValueAt(selectedRow, 3).toString();

        if ("Pending".equals(status) || "Canceled".equals(status) || "Reject".equals(status)) {
            JOptionPane.showMessageDialog(this, status + " appointments cannot be canceled.");
            return;
        }

     // Show confirmation dialog
        int confirmed = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to cancel this appointment?", 
            "Confirm Cancellation", 
            JOptionPane.YES_NO_OPTION);

        if (confirmed != JOptionPane.YES_OPTION) {
            // User chose "No" or closed the dialog
            return;
        }

        // Proceed with cancellation
        bookingTable.setValueAt("Canceled", selectedRow, 3);
        updateBookingFile(); // Update the file with the changes
        JOptionPane.showMessageDialog(this, "Appointment canceled successfully.");

    }
    
    private void updateBookingFile() {
    	String user = User.getCurrentUser();
        File inputFile = new File("appointments.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            // Read all lines into a list
            StringBuilder updatedContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length < 5) continue; // Skip invalid lines

                if (parts[0].equals(user)) {
                	String lecturer = parts[1];
                    String date = parts[2];
                    String time = parts[3];

                    // Check if the line corresponds to the selected table row
                    if (bookingTable.getSelectedRow() != -1 &&
                            lecturer.equals(bookingTable.getValueAt(bookingTable.getSelectedRow(), 0).toString()) &&
                            date.equals(bookingTable.getValueAt(bookingTable.getSelectedRow(), 1).toString()) &&
                            time.equals(bookingTable.getValueAt(bookingTable.getSelectedRow(), 2).toString())){
                        parts[4] = "Canceled"; // Update status to "Canceled"
                    }
                }
                // Append the modified or original line to the updated content
                updatedContent.append(String.join(", ", parts)).append(System.lineSeparator());
            }

            // Write updated content back to the same file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
                writer.write(updatedContent.toString());
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error updating the booking file", e);
            JOptionPane.showMessageDialog(this, "Error updating the booking file.");
        }
    }
}
