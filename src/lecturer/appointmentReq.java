package lecturer;
import utils.User;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class appointmentReq extends JPanel {

    private JTable consultationTable;
    private DefaultTableModel tableModel;
    
    public appointmentReq(JPanel cardPanel, CardLayout cardLayout){
    	initialize();
    }
    
    private void initialize() {
        setLayout(null);

        // Title label with clear font and positioning
        JLabel lblPendingConsultationRequest = new JLabel("Consultation Request");
        lblPendingConsultationRequest.setHorizontalAlignment(SwingConstants.CENTER);
        lblPendingConsultationRequest.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblPendingConsultationRequest.setBounds(185, 42, 312, 30); // Adjusted position
        add(lblPendingConsultationRequest);

        // Define column names for the table
        String[] columnNames = { "Student Name", "Date", "Time", "Status" };

        // Create table model with column names
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable by default
            }
        };
        
        // Create the table using the model
        consultationTable = new JTable(tableModel);

        // Customize table header
        JTableHeader tableHeader = consultationTable.getTableHeader();
        tableHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
        consultationTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        consultationTable.setRowHeight(25);

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

        // Apply the custom renderer to all columns
        for (int i = 0; i < consultationTable.getColumnCount(); i++) {
            consultationTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Add the table to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(consultationTable);
        scrollPane.setBounds(36, 95, 600, 186); // Set position and size of the table
        add(scrollPane);

        // Refresh button
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(evt -> refreshTableData());
        btnRefresh.setBounds(237, 322, 85, 21);
        add(btnRefresh);
        
        
       //Edit button
        JButton btnEdit = new JButton("Edit");
        btnEdit.addActionListener(evt -> selectButtonActionPerformed());
        btnEdit.setBounds(359, 322, 85, 21);
        add(btnEdit);
    }

    private void refreshTableData() {
    	String user = User.getCurrentUser();
        tableModel.setRowCount(0); // Clear existing rows
//        boolean foundStuName;
        boolean foundAppointments = false;
        
        // File handling for appointments
        File file = new File("appointments.txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "File appointments.txt not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
        	String line;
//        	foundStuName = false;
        	while((line = reader.readLine()) != null) {
        		String[] parts = line.split(", ");
        		if (parts[1].equals(user) && parts[4].equals("Pending")) {
        		    tableModel.addRow(new Object[]{parts[0].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim()});
        		    foundAppointments = true;
        		}
        	}
        }
        catch(IOException e) {
        	
        }

        if (foundAppointments) {
            JOptionPane.showMessageDialog(this, "Table refreshed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Currently, there are no appointment requests for you.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void selectButtonActionPerformed() {
    	String user = User.getCurrentUser();
    	
    	int selectedRow = consultationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) consultationTable.getModel();
        String stuname = model.getValueAt(selectedRow, 0).toString();
        String date = model.getValueAt(selectedRow, 1).toString();
        String time = model.getValueAt(selectedRow, 2).toString();
        String status = model.getValueAt(selectedRow, 3).toString();

        if (!status.equals("Pending")) {
        	JOptionPane.showMessageDialog(this, "Cannot change status that has already been decided.", "Error", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        
		String[] options = {"Accept", "Reject"};
		int choice = JOptionPane.showOptionDialog(this, "Select a consultation slot:", "Book Consultation",
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
                        && parts[4].equals("Pending")) {
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

            JOptionPane.showMessageDialog(this, "Consultation updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Optionally refresh the table
            refreshTableData();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error update booking: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
