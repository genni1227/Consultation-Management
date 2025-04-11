package lecturer;
import utils.*;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class manageConsultation extends JPanel {

    private JTable consultationTable;
    private DefaultTableModel tableModel;
    private JButton btnDate;
    private JButton btnChooseTimeFrom;
    private JButton btnChooseTimeTo;
    private boolean foundLecName;
    
    public manageConsultation(JPanel cardPanel, CardLayout cardLayout) {
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(null);

        JLabel titleLabel = new JLabel("Manage Consultation Slots");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        titleLabel.setBounds(203, 45, 300, 30);
        add(titleLabel);

        // Define column names
        String[] columnNames = {
                "Date", 
                "Time", 
                "Status", 
                "Student Name"
        };

        // Create table model
        tableModel = new DefaultTableModel(columnNames, 0) {
        	 @Override
        	    public boolean isCellEditable(int row, int column) {
        	        // Make all cells non-editable by default
        	        return false;
        	    }
        };
        consultationTable = new JTable(tableModel);
        
        consultationTable.getColumnModel().getColumn(0).setPreferredWidth(100);

        // Customize table
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
        for (int i = 0; i < consultationTable.getColumnCount(); i++) {
            consultationTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
         // Initially, set no editors, making the columns non-editable
            consultationTable.getColumnModel().getColumn(i).setCellEditor(null);
        }

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(consultationTable);
        scrollPane.setBounds(53, 95, 600, 137);
        add(scrollPane);

        // Refresh button
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(231, 266, 100, 30);
        add(btnRefresh);
        
        // Add action listener for refresh button
        btnRefresh.addActionListener(evt -> refreshTableData());
        
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> {
        	addConsultationSlot();
        });
        btnAdd.setBounds(301, 468, 100, 30);
        add(btnAdd);
        
        JLabel lblNewLabel = new JLabel("Date: ");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel.setBounds(101, 356, 68, 20);
        add(lblNewLabel);
        
        JLabel lblTime = new JLabel("From: ");
        lblTime.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        lblTime.setBounds(101, 400, 68, 20);
        add(lblTime);
        
        btnDate = new JButton("Choose Date");
        btnDate.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		// Open the calendar window and pass this class as the listener
                new DatePicker(new DatePicker.DateSelectedListener() {
                    @Override
                    public void onDateSelected(String formattedDate) {
                        // Update the button text with the selected date
                        btnDate.setText(formattedDate);
                    }
                });
        	}
        });
        btnDate.setBounds(179, 356, 179, 21);
        add(btnDate);
        
        btnChooseTimeFrom = new JButton("Choose Time");
        btnChooseTimeFrom.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 // Open TimePicker and pass the listener
                new TimePicker(new TimePicker.TimeSelectedListener() {
                    @Override
                    public void onTimeSelected(String formattedTime) {
                        // Update the "Choose Time" button with the selected time
                        btnChooseTimeFrom.setText(formattedTime);
                    }
                });
        	}
        });
        btnChooseTimeFrom.setBounds(193, 401, 127, 21);
        add(btnChooseTimeFrom);
        
        JLabel lblTo = new JLabel("To: ");
        lblTo.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        lblTo.setBounds(380, 400, 68, 20);
        add(lblTo);
        
        btnChooseTimeTo = new JButton("Choose Time");
        btnChooseTimeTo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 // Open TimePicker and pass the listener
                new TimePicker(new TimePicker.TimeSelectedListener() {
                    @Override
                    public void onTimeSelected(String formattedTime) {
                        // Update the "Choose Time" button with the selected time
                        btnChooseTimeTo.setText(formattedTime);
                    }
                });
        	}
        });
        btnChooseTimeTo.setBounds(450, 401, 127, 21);
        add(btnChooseTimeTo);
        
     // Delete button
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            deleteSelectedRow(); 
        });
        btnDelete.setBounds(362, 266, 100, 30);
        add(btnDelete);
    }
    
    private void deleteSelectedRow() {
    	String user = User.getCurrentUser();
        int selectedRow = consultationTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the data of the selected row
        String date = (String) tableModel.getValueAt(selectedRow, 0);
        String time = (String) tableModel.getValueAt(selectedRow, 1);

        // Remove the selected row from the table
        tableModel.removeRow(selectedRow);

        // Update the file
        File file = new File("lec_consultation_slot.txt");
        File tempFile = new File("lec_consultation_slot_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(user) ) {
                	String Date1 = parts[1].trim();
                    String Time1 = parts[2].trim();
                    String Date2 = parts[3].trim();
                    String Time2 = parts[4].trim();

                    // Update the slots based on the deletion
                    if (Date1.equals(date) && Time1.equals(time)) {
                        Date1 = "null";
                        Time1 = "null";
                    } else if (Date2.equals(date) && Time2.equals(time)) {
                        Date2 = "null";
                        Time2 = "null";
                    }
                    // Write the updated row back to the file
                    writer.write(parts[0] + ", " + Date1 + ", " + Time1 + ", " + Date2 + ", " + Time2);
                    writer.newLine();
                }
                else {
                    // Write other lines without modification
                    writer.write(line);
                    writer.newLine();
                }
                
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Replace original file with the updated file
        if (file.delete()) {
            tempFile.renameTo(file);
            JOptionPane.showMessageDialog(this, "Row deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting row from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTableData() {
        String user = User.getCurrentUser();
        tableModel.setRowCount(0); // Clear existing rows

        File slotFile = new File("lec_consultation_slot.txt");
        File appointmentFile = new File("appointments.txt");

        if (slotFile.exists()&& appointmentFile.exists()) {
        	updateSlotFile(slotFile, appointmentFile);
        }
        
        if (!slotFile.exists()) {
            JOptionPane.showMessageDialog(this, "File lec_consultation_slot.txt not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader slotReader = new BufferedReader(new FileReader(slotFile))) {

            String line;
            boolean foundLecName = false;

            while ((line = slotReader.readLine()) != null) {
                String[] parts = line.split(", ");

                if (parts[0].equals(user)) {
                    foundLecName = true;

                    // Check for each slot and time
                    for (int i = 1; i <= 4; i += 2) {
                        String slotDate = parts[i].trim();
                        String slotTime = parts[i + 1].trim();

                        if (!"null".equals(slotDate) && !"null".equals(slotTime)) {
                        	tableModel.addRow(new Object[]{slotDate, slotTime, "", ""});
                        }
                    }

                    // If lecturer exists but all slots are null
                    if ("null".equals(parts[1].trim()) && "null".equals(parts[3].trim())) {
                        foundLecName = false;
                    }
                }
            }

            if (!foundLecName) {
                JOptionPane.showMessageDialog(this, "Currently, you haven't set any consultation slots for " + user, "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Table updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks the appointment status for a specific lecturer, date, and time.
     *
     * @param appointmentFile the file containing appointment data
     * @param lecturer        the lecturer's name
     * @param slotDate        the consultation slot date
     * @param slotTime        the consultation slot time
     * @return the appointment status ("Accept", "Pending", "Reject", or empty if no matching record)
     */
//    private String[] checkAppointmentStatus(File appointmentFile, String lecturer, String slotDate, String slotTime) {
//        if (!appointmentFile.exists()) {
//        	 return new String[]{"", ""}; // No status or student name if the appointments file doesn't exist
//        }
//
//        try (BufferedReader appointmentReader = new BufferedReader(new FileReader(appointmentFile))) {
//            String line;
//            while ((line = appointmentReader.readLine()) != null) {
//                String[] parts = line.split(", ");
//                if (parts.length >= 5 &&
//                    parts[1].equals(lecturer) &&
//                    parts[2].equals(slotDate) &&
//                    parts[3].equals(slotTime) &&
//                    "Accept".equalsIgnoreCase(parts[4])) {
//                	String stuName = parts[0];
//                    return new String[] {"Accept", stuName}; // Return both status and student name
//                }
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Error reading appointments file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//
//        return new String[]{"", ""}; // Return empty values if no match found
//    }

    public static void updateSlotFile(File slotFile, File appointmentFile) {
        // Temporary list to hold updated slot data
        List<String> updatedSlots = new ArrayList<>();
        List<String[]> appointmentData = new ArrayList<>();

        // Read the appointments file into memory
        try (BufferedReader apptReader = new BufferedReader(new FileReader(appointmentFile))) {
            String apptLine;
            while ((apptLine = apptReader.readLine()) != null) {
                String[] apptParts = apptLine.split(", ");
                if (apptParts.length >= 5) {
                    appointmentData.add(apptParts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Process the slot file and compare with appointment data
        try (BufferedReader slotReader = new BufferedReader(new FileReader(slotFile))) {
            String slotLine;
            while ((slotLine = slotReader.readLine()) != null) {
                String[] slotParts = slotLine.split(", ");
                if (slotParts.length < 5) {
                    updatedSlots.add(slotLine); // Skip invalid lines
                    continue;
                }

                String lecName = slotParts[0];
                String slotDate1 = slotParts[1];
                String slotTime1 = slotParts[2];
                String slotDate2 = slotParts[3];
                String slotTime2 = slotParts[4];
                boolean slot1Matched = false;
                boolean slot2Matched = false;

                for (String[] apptParts : appointmentData) {
                    String apptLecName = apptParts[1];
                    String apptDate = apptParts[2];
                    String apptTime = apptParts[3];
                    String apptStatus = apptParts[4];

                    // Match slots in slotFile with appointments in appointmentFile
                    if (lecName.equals(apptLecName) && apptStatus != null && !apptStatus.isBlank() && !apptStatus.equals("Pending")) {
                        if (slotDate1.equals(apptDate) && slotTime1.equals(apptTime)) {
                            slot1Matched = true; // Slot 1 matches
                        } else if (slotDate2.equals(apptDate) && slotTime2.equals(apptTime)) {
                            slot2Matched = true; // Slot 2 matches
                        }
                    }
                }

                // Update the slot data based on matches
                if (slot1Matched) {
                    slotParts[1] = "null"; // Nullify slotDate1
                    slotParts[2] = "null"; // Nullify slotTime1
                }
                if (slot2Matched) {
                    slotParts[3] = "null"; // Nullify slotDate2
                    slotParts[4] = "null"; // Nullify slotTime2
                }

                updatedSlots.add(String.join(", ", slotParts));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write the updated slots back to the slotFile
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(slotFile))) {
            for (String updatedLine : updatedSlots) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addConsultationSlot() {
        String user = User.getCurrentUser();
        String selectedDate = btnDate.getText();
        String selectedTimeFrom = btnChooseTimeFrom.getText();
        String selectedTimeTo = btnChooseTimeTo.getText();

        File file = new File("lec_consultation_slot.txt");
        File tempFile = new File("lec_consultation_slot_temp.txt");

        // Validate input fields
        if (selectedDate.equals("Choose Date") || selectedTimeFrom.equals("Choose Time") || selectedTimeTo.equals("Choose Time")) {
            JOptionPane.showMessageDialog(this, "Please select date and time for both slots.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
      //Check if the time range is valid
        TimeValidate validator = new TimeValidate(selectedTimeFrom, selectedTimeTo);
        if (!validator.isValidTimeRange()) {
        	JOptionPane.showMessageDialog(this, "Time 'From' must be after time 'to'.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean userFound = false;
        boolean slotExists = false;
        boolean slotAdded = false; 

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");

                if (parts[0].equals(user)) {
                    userFound = true;

                    // Check for duplicate slots
                    if ((selectedDate.equals(parts[1].trim()) && selectedTimeFrom.equals(parts[2].split(" - ")[0].trim()) &&
                            selectedTimeTo.equals(parts[2].split(" - ")[1].trim())) ||
                        (selectedDate.equals(parts[3].trim()) && selectedTimeFrom.equals(parts[4].split(" - ")[0].trim()) &&
                            selectedTimeTo.equals(parts[4].split(" - ")[1].trim()))) {
                        slotExists = true;
                        break;
                    }
                    
                    // Case 1: Both slots are null
                    if ("null".equals(parts[1].trim()) && "null".equals(parts[3].trim())) {
                        parts[1] = selectedDate;
                        parts[2] = selectedTimeFrom + " - " + selectedTimeTo;
                        slotAdded = true;
                    }
                    // Case 2: Slot1 is null
                    else if ("null".equals(parts[1].trim())) {
                        parts[1] = selectedDate;
                        parts[2] = selectedTimeFrom + " - " + selectedTimeTo;
                        slotAdded = true;
                    }
                    // Case 3: Slot2 is null
                    else if ("null".equals(parts[3].trim())) {
                        parts[3] = selectedDate;
                        parts[4] = selectedTimeFrom + " - " + selectedTimeTo;
                        slotAdded = true;
                        
                    } else {
                        // Both slots are already occupied
                        JOptionPane.showMessageDialog(this, "Lecturer can only put two consultation slots.", "Error", JOptionPane.ERROR_MESSAGE);
                        writer.write(line); // Write the original line as no change is made
                        writer.newLine();
                        continue;
                    }

                    // Write updated line
                    writer.write(String.join(", ", parts));
                    writer.newLine();
                } else {
                    // Write unmodified lines for other users
                    writer.write(line);
                    writer.newLine();
                }
            }

            // If duplicate slot found, show error and exit
            if (slotExists) {
                JOptionPane.showMessageDialog(this, "This consultation slot already exists. Please choose a different date or time.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } 
//                else {
//            	JOptionPane.showMessageDialog(this, "Consultation slot updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
//            }
            
            // If the user's entry was not found, create a new one
            if (!userFound) {
                writer.write(user + ", " + selectedDate + ", " + selectedTimeFrom + " - " + selectedTimeTo + ", null, null");
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while processing the file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Replace the original file with the updated temp file
        if (file.delete()) {
            if (tempFile.renameTo(file)) {
            	if (slotAdded) {
            		JOptionPane.showMessageDialog(this, "Consultation slot added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            		refreshTableData();
            	}
                
            } else {
                JOptionPane.showMessageDialog(this, "Error updating the consultation slot. File renaming failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting the original file.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}