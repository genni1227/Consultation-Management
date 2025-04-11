package lecturer;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import utils.User;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.event.ActionEvent;

public class upcomingAppt extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;

    public upcomingAppt(JPanel cardPanel, CardLayout cardLayout) {
    	initialize();
    }
    
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
    	setLayout(null);

        JLabel lblNewLabel = new JLabel("Upcoming Appointments");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel.setBounds(218, 28, 274, 64);
        add(lblNewLabel);

        // Define column names for the table
        String[] columnNames = {
                "Student Name",
                "Date",
                "Time"
        };

        // Create table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };

        // Create the table
        table = new JTable(tableModel);

        // Customize table
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.setRowHeight(25);

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
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 112, 600, 352);
        add(scrollPane);

        // Refresh button
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> refreshTableData());
        btnRefresh.setBounds(300, 490, 100, 30);
        add(btnRefresh);
    }

    /**
     * Refresh table data with accepted appointments.
     */
    private void refreshTableData() {
    	String user = User.getCurrentUser();
        tableModel.setRowCount(0); // Clear existing rows

        File appointmentFile = new File("appointments.txt");

        if (!appointmentFile.exists()) {
            JOptionPane.showMessageDialog(this, "File appointments.txt not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(appointmentFile))) {
            String line;
            boolean hasAcceptedAppointments = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");

                // Ensure the line has at least 5 elements (stuName, lecName, slotDate, slotTime, status)
                if (parts.length >=5) {
                	 if (parts[1].equals(user) && "Accept".equalsIgnoreCase(parts[4].trim())) {
                         String stuName = parts[0].trim();  // Student Name
                         String slotDate = parts[2].trim(); // Slot Date
                         String slotTime = parts[3].trim(); // Slot Time

                         try {
                        	    // Extract only the date part before the space
                        	    String dateOnly = slotDate.split(" ")[0].trim();

                        	    // Parse dateOnly into LocalDate
                        	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        	    LocalDate appointmentDate = LocalDate.parse(dateOnly, formatter);

                        	    // Get today's date
                        	    LocalDate today = LocalDate.now();

                        	    // Check if the appointment is today or in the future
                        	    if (!appointmentDate.isBefore(today)) {
                        	        // Add these values to the table
                        	        tableModel.addRow(new Object[]{stuName, slotDate, slotTime});
                        	        hasAcceptedAppointments = true;
                        	    }
                        	} catch (DateTimeParseException e) {
                        	    System.err.println("Invalid date format for slot date: " + slotDate);
                        	}
                     }
                }
            }

            if (!hasAcceptedAppointments) {
                JOptionPane.showMessageDialog(this, "No accepted appointments found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Table updated with accepted appointments!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
