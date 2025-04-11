package student;
import utils.User;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class makeBooking extends JPanel {

    private JButton refreshButton;
    private JButton selectButton;
    private JTable consultationTable;
    private JLabel jlabel;

    public makeBooking(JPanel cardPanel, CardLayout cardLayout) {
        // Initialize components
        initComponents();


        // Set action for the Refresh button
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        // Set action for the Select button
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                selectButtonActionPerformed(evt, cardPanel, cardLayout);
            }
        });

    }

    private void initComponents() {
        jlabel = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        selectButton = new javax.swing.JButton();
        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        consultationTable = new javax.swing.JTable();

        jlabel.setFont(new java.awt.Font("NSimSun", 1, 36));
        jlabel.setForeground(new java.awt.Color(255, 102, 102));
        jlabel.setText("Make Consultation");

        refreshButton.setText("Refresh");
        selectButton.setText("Select");

        consultationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Lecturer Name", "Consultation Slots 1", "Time", "Consultation Slots 2", "Time"
            }
        ) {
        	@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
        	}
        });

        // Set column widths
        consultationTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Lecturer Name
        consultationTable.getColumnModel().getColumn(0).setMinWidth(100);
        consultationTable.getColumnModel().getColumn(0).setMaxWidth(150);
        
        jScrollPane1.setViewportView(consultationTable);

        // Layout setup code 
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(refreshButton)
                        .addGap(18, 18, 18)
                        .addComponent(selectButton))
                    .addComponent(jlabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jlabel)
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectButton)
                    .addComponent(refreshButton))
                .addContainerGap(145, Short.MAX_VALUE))
        );
    }
    
    // Refresh button action: loads the lecturer's data
    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
        DefaultTableModel model = (DefaultTableModel) consultationTable.getModel();
        model.setRowCount(0);
        boolean hasLecSlot = false;

        File file = new File("lec_consultation_slot.txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "File lecturers.txt not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                
                // Replace "null" with an empty string in all parts of the array
                for (int i = 0; i < parts.length; i++) {
                    if ("null".equals(parts[i].trim())) {
                        parts[i] = "";  // Set "null" to empty string
                    }
                }

                // Now add the row, using the updated parts with empty strings instead of "null"
                // Add row based on the availability of the slots
                if (!parts[1].isEmpty() && !parts[2].isEmpty() && !parts[3].isEmpty() && !parts[4].isEmpty()) {
                    model.addRow(new Object[]{parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim()});
                    hasLecSlot = true;
                }
                // Only Slot 1 is non-empty (slot 2 is empty)
                else if (!parts[1].isEmpty() && !parts[2].isEmpty()) {
                    model.addRow(new Object[]{parts[0].trim(), parts[1].trim(), parts[2].trim(), "", ""});
                    hasLecSlot = true;
                }
                // Only Slot 2 is non-empty (slot 1 is empty)
                else if (!parts[3].isEmpty() && !parts[4].isEmpty()) {
                    model.addRow(new Object[]{parts[0].trim(), parts[3].trim(), parts[4].trim(), "", ""});
                    hasLecSlot = true;
                }
                // Both slots are empty (skip adding row)
                else {
                    continue;
                }
            }
            removeConfirmedBookings();
            
            if(hasLecSlot) {
            	JOptionPane.showMessageDialog(this, "Table refreshed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
            	JOptionPane.showMessageDialog(this, "No available consultation slots as this moment", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Select button action: allows selecting a consultation slot and switching to another panel (if needed)
    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt, JPanel cardPanel, CardLayout cardLayout) {
    	String user = User.getCurrentUser();
    	
    	int selectedRow = consultationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) consultationTable.getModel();
        String lecturer = model.getValueAt(selectedRow, 0).toString();
        String slot1 = model.getValueAt(selectedRow, 1).toString();
        String time1 = model.getValueAt(selectedRow, 2).toString();
        String slot2 = model.getValueAt(selectedRow, 3).toString();
        String time2 = model.getValueAt(selectedRow, 4).toString();

     // Dynamically construct the options array based on availability
        List<String> availableSlots = new ArrayList<>();
        if (!slot1.isEmpty() && !time1.isEmpty()) {
            availableSlots.add(slot1 + " " + time1);
        }
        if (!slot2.isEmpty() && !time2.isEmpty()) {
            availableSlots.add(slot2 + " " + time2);
        }

        if (availableSlots.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No available slots to book.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] options = availableSlots.toArray(new String[0]); // Convert to array for JOptionPane
        
//        String[] options = {slot1 + " " + time1, slot2 + " " + time2};
        int choice = JOptionPane.showOptionDialog(this, "Select a consultation slot:", "Book Consultation",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == -1) {
            JOptionPane.showMessageDialog(this, "No slot selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedSlot = options[choice];
        // Split into 2 parts: date and time range
        String[] splitSelectedSlot = selectedSlot.split(" ", 3); 
        String datePart = splitSelectedSlot[0];
        String dayPart = splitSelectedSlot[1];
        String timePart = splitSelectedSlot[2];
        
        File file = new File("appointments.txt");
        
        // Check if the booking is already made
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 5 && 
                    parts[0].equals(user) && 
                    parts[1].equals(lecturer) && 
                    parts[2].equals(datePart + " " + dayPart) && 
                    parts[3].equals(timePart)) {
                    JOptionPane.showMessageDialog(this, "You have already booked this consultation slot.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading bookings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file, true))) {
            bf.write(user + ", " + lecturer + ", " + datePart + " " + dayPart + ", " + timePart  + ", Pending" +"\n");
            JOptionPane.showMessageDialog(this, "Consultation booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Switch to View Booking Panel after booked successfully
//            cardLayout.show(cardPanel, "Student View Booking Panel"); 
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving booking: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removeConfirmedBookings() {
        File slotFile = new File("lec_consultation_slot.txt");
        File appointmentFile = new File("appointments.txt");

        if (!slotFile.exists()) {
            JOptionPane.showMessageDialog(this, "File lec_consultation_slot.txt not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!appointmentFile.exists()) {
            JOptionPane.showMessageDialog(this, "File appointments.txt not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 1: Read confirmed bookings from appointments.txt
        List<String[]> confirmedBookings = new ArrayList<>();
        try (BufferedReader appointmentReader = new BufferedReader(new FileReader(appointmentFile))) {
            String line;
            while ((line = appointmentReader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 5 && "Accept".equalsIgnoreCase(parts[4].trim())) {
                    // Collect confirmed booking details: [lecName, slotDate, slotTime]
                    confirmedBookings.add(new String[]{parts[1].trim(), parts[2].trim(), parts[3].trim()});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading appointments file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 2: Read lec_consultation_slot.txt and filter out confirmed bookings
        List<String> updatedSlots = new ArrayList<>();
        try (BufferedReader slotReader = new BufferedReader(new FileReader(slotFile))) {
            String line;
            while ((line = slotReader.readLine()) != null) {
                String[] parts = line.split(", ");

                // Check if either slot matches a confirmed booking
                boolean slot1Confirmed = false;
                boolean slot2Confirmed = false;

                for (String[] booking : confirmedBookings) {
                    String lecName = booking[0];
                    String slotDate = booking[1];
                    String slotTime = booking[2];

                    if (parts[0].equals(lecName)) {
                        if (parts[1].trim().equals(slotDate) && parts[2].trim().equals(slotTime)) {
                            slot1Confirmed = true;
                        }
                        if (parts[3].trim().equals(slotDate) && parts[4].trim().equals(slotTime)) {
                            slot2Confirmed = true;
                        }
                    }
                }

                // Modify the line to exclude confirmed slots
                if (slot1Confirmed) {
                    parts[1] = "null";
                    parts[2] = "null";
                }
                if (slot2Confirmed) {
                    parts[3] = "null";
                    parts[4] = "null";
                }

                // Check if at least one slot remains
                if (!"null".equals(parts[1]) || !"null".equals(parts[3])) {
                    updatedSlots.add(String.join(", ", parts));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading consultation slots file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 3: Write the updated slots back to lec_consultation_slot.txt
        try (BufferedWriter slotWriter = new BufferedWriter(new FileWriter(slotFile))) {
            for (String updatedSlot : updatedSlots) {
                slotWriter.write(updatedSlot);
                slotWriter.newLine();
            }
//            JOptionPane.showMessageDialog(this, "Confirmed bookings removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to consultation slots file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
