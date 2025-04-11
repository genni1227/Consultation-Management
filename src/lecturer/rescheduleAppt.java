package lecturer;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import utils.User;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class rescheduleAppt extends JPanel {

    private JFrame frame;
    private DefaultTableModel tableModel;
    private JTable rescheduleTable;

    public rescheduleAppt(JPanel cardPanel, CardLayout cardLayout) {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
    	setLayout(null);

        JLabel lblNewLabel = new JLabel("Reschedule Appointment");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel.setBounds(199, 33, 296, 47);
        add(lblNewLabel);

        // Define column names for the table
        String[] columnNames = {"Student Name", "Date", "Time", "Status"};

        // Create table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };

        // Create the table
        rescheduleTable = new JTable(tableModel);

        // Customize table
        JTableHeader tableHeader = rescheduleTable.getTableHeader();
        tableHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
        rescheduleTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        rescheduleTable.setRowHeight(25);

        // Add padding to table cells
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                }
                return c;
            }
        };
        for (int i = 0; i < rescheduleTable.getColumnCount(); i++) {
        	rescheduleTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(rescheduleTable);
        scrollPane.setBounds(50, 112, 600, 352);
        add(scrollPane);

        // Refresh button
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e ->refreshTableData());
        btnRefresh.setBounds(234, 490, 100, 30);
        add(btnRefresh);
        
        // Edit button (Update Status)
        JButton btnEdit = new JButton("Edit");
        btnEdit.setBounds(374, 490, 100, 30);
        btnEdit.addActionListener(evt -> selectButtonActionPerformed());
        add(btnEdit);
    }
    
    public void refreshTableData() {
        String user = User.getCurrentUser();
        tableModel.setRowCount(0); // Clear existing rows

        File appointmentFile = new File("appointments.txt");

        if (!appointmentFile.exists()) {
            JOptionPane.showMessageDialog(this, "File appointments.txt not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean hasRescheduleRequests = false; // Flag to track reschedule requests

        try (BufferedReader reader = new BufferedReader(new FileReader(appointmentFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");

                // Ensure the line has at least 5 elements (stuName, lecName, slotDate, slotTime, status)
                if (parts.length >= 5) {
                    if (parts[1].equals(user) && "Rescheduled".equalsIgnoreCase(parts[4].trim())) {
                        String stuName = parts[0].trim();  // Student Name
                        String slotDate = parts[2].trim(); // Slot Date
                        String slotTime = parts[3].trim(); // Slot Time
                        String status = parts[4].trim();   // Rescheduled word

                        // Add these values to the table
                        tableModel.addRow(new Object[]{stuName, slotDate, slotTime, status});
                        hasRescheduleRequests = true; // Set flag to true
                    }
                }
            }

            // Show message if no reschedule requests found
            if (!hasRescheduleRequests) {
                JOptionPane.showMessageDialog(this, "Currently, there are no reschedule requests.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
            	JOptionPane.showMessageDialog(this, "Table updated with rescheduled request!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void selectButtonActionPerformed() {
    	String user = User.getCurrentUser();
    	
    	int selectedRow = rescheduleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) rescheduleTable.getModel();
        String stuname = model.getValueAt(selectedRow, 0).toString();
        String date = model.getValueAt(selectedRow, 1).toString();
        String time = model.getValueAt(selectedRow, 2).toString();
        String status = model.getValueAt(selectedRow, 3).toString();

        if (!status.equals("Rescheduled")) {
        	JOptionPane.showMessageDialog(this, "Cannot change status that has already been decided.", "Error", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        
		String[] options = {"Accept", "Reject"};
		int choice = JOptionPane.showOptionDialog(this, "Accept or Reject reschedule slot:", "Reschedule Consultation",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		
		if (choice == -1) {
		    JOptionPane.showMessageDialog(this, "No slot selected.", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		
		String updatedStatus = options[choice];
		String file = "appointments.txt";
        
        try {
            // Read the file content
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean updated = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 4 
                        && parts[0].equals(stuname) 
                        && parts[1].equals(user) 
                        && parts[2].equals(date) 
                        && parts[3].equals(time) 
                        && parts[4].equals("Rescheduled")) {
                    // Update the line with the new status
                    line = stuname + ", " + user + ", " + date + ", " + time + ", " + updatedStatus;
                    updated = true;
                }
                lines.add(line);
            }
            reader.close();

            if (!updated) {
                JOptionPane.showMessageDialog(this, "No matching pending request found to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Write the updated content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();

            JOptionPane.showMessageDialog(this, "Reschedule slot updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Optionally refresh the table
            refreshTableData();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error update reschedule: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

