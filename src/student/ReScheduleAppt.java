package student;
import utils.TimeValidate;
import javax.swing.*;
import utils.DatePicker;
import utils.TimePicker;
import utils.User;

import java.awt.CardLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Font;
import java.awt.Color;

public class ReScheduleAppt extends JPanel {

    private String lecturer;
    private String date;
    private String timeFrom;
    private String timeTo;
    private String originalDate;
    private String originalTimeFrom;
    private String originalTimeTo;

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JButton chooseDateButton;
    private JButton chooseTimeFromButton;
    private JButton chooseTimeToButton;
    private JButton confirmButton;
    private JButton backButton;
    private JTextField lecturerField;
    private JTextField dateField;
    private JTextField timeFromField;
    private JTextField timeToField;

    public ReScheduleAppt(JPanel cardPanel, CardLayout cardLayout) {
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        initComponents();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("Reschedule Consultation");
        titleLabel.setBounds(104, 39, 448, 42);
        titleLabel.setFont(new java.awt.Font("SimSun", 1, 36));

        JLabel lecturerLabel = new JLabel("Lecturer:");
        lecturerLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
        lecturerLabel.setBounds(120, 115, 87, 17);
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        dateLabel.setBounds(120, 176, 66, 17);
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        timeLabel.setBounds(120, 234, 68, 17);

        chooseDateButton = new JButton("Choose Date");
        chooseDateButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        chooseDateButton.setBounds(401, 172, 151, 25);
        chooseDateButton.addActionListener(evt -> openDatePicker());

        chooseTimeFromButton = new JButton("Choose Time From");
        chooseTimeFromButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        chooseTimeFromButton.setBounds(401, 234, 165, 25);
        chooseTimeFromButton.addActionListener(evt -> openTimePickerFrom());

        chooseTimeToButton = new JButton("Choose Time To");
        chooseTimeToButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        chooseTimeToButton.setBounds(401, 282, 165, 25);
        chooseTimeToButton.addActionListener(evt -> openTimePickerTo());

        confirmButton = new JButton("Confirm");
        confirmButton.setBackground(new Color(0, 255, 64));
        confirmButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        confirmButton.setBounds(434, 342, 102, 29);
        confirmButton.addActionListener(evt -> confirmReschedule());

        backButton = new JButton("Back");
        backButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        backButton.setBounds(151, 342, 92, 29);
        backButton.addActionListener(e ->
        	cardLayout.show(cardPanel, "Student View Booking Panel"));
        setLayout(null);
        
        add(backButton);
        add(titleLabel);
        add(lecturerLabel);
        add(dateLabel);
        add(timeLabel);
        add(chooseDateButton);
        add(chooseTimeFromButton);
        add(chooseTimeToButton);
        add(confirmButton);
        
        lecturerField = new JTextField();
        lecturerField.setBounds(227, 116, 165, 19);
        add(lecturerField);
        lecturerField.setColumns(10);
        
        dateField = new JTextField();
        dateField.setColumns(10);
        dateField.setBounds(227, 174, 164, 19);
        add(dateField);
        
        timeFromField = new JTextField();
        timeFromField.setColumns(10);
        timeFromField.setBounds(227, 235, 164, 19);
        add(timeFromField);
        
        timeToField = new JTextField();
        timeToField.setColumns(10);
        timeToField.setBounds(227, 287, 164, 19);
        add(timeToField);
    }

    private void openDatePicker() {
    	new DatePicker(date -> dateField.setText(date));
    }

    private void openTimePickerFrom() {
    	new TimePicker(time -> timeFromField.setText(time));
    }
    
    private void openTimePickerTo() {
    	new TimePicker(time -> timeToField.setText(time));
    }

    private void confirmReschedule() {
    	String newLecturer = lecturerField.getText();
        String newDate = dateField.getText();
        String newTimefrom = timeFromField.getText();
        String newTimeto = timeToField.getText();

        // Check if time range is valid
        TimeValidate validator = new TimeValidate(newTimefrom, newTimeto);
        if (newTimefrom.equals(newTimeto)) {
        	JOptionPane.showMessageDialog(this, "Time 'From' must be after time 'to'.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if it is empty
        if(newDate.isEmpty() || newTimefrom.isEmpty() || newTimeto.isEmpty()){
        	JOptionPane.showMessageDialog(this, "The Date, Time from, and Time to section cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if the reschedule slot is the same as the original
        if (getOriginalDate().equals(newDate) && getOriginalTimeFrom().equals(newTimefrom) && getOriginalTimeTo().equals(newTimeto)) {
        	JOptionPane.showMessageDialog(this, "The reschedule slot cannot be the same as the original.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String newTime = newTimefrom + " - " + newTimeto; 

        // Update the file
        updateAppointmentFile(newLecturer, newDate, newTime);
    }

    

    private void updateAppointmentFile(String newLecturer, String newDate, String newTime) {
        String user = User.getCurrentUser();
        File file = new File("appointments.txt");

        // Check if the file exists before proceeding
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "The appointment file does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder updatedContent = new StringBuilder();
        boolean linematch = false; 
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // Iterate through each line in the file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                // Ensure the line is correctly formatted before accessing array elements
                if (parts.length >= 5 &&
                    parts[0].equals(user) &&
                    parts[1].equals(lecturer) &&
                    parts[2].equals(originalDate) &&
                    parts[3].equals(originalTimeFrom + " - " + originalTimeTo) &&
                    parts[4].equals("Accept")) {
                    
                    // Update the line with new information
                    String updatedRow = user + ", " + newLecturer + ", " + newDate + ", " + newTime + ", Rescheduled";
                    updatedContent.append(updatedRow).append("\n");
                    linematch = true;
                    
                } else {
                    // If it doesn't match, keep the original line
                    updatedContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading the appointment file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // For debugging purposes
            return;
        }

        // Write the updated content back to the file
        if (linematch) {
            try {
                // Create the BufferedWriter and write the updated content
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                writer.write(updatedContent.toString());
                writer.close();  // Make sure to close the writer after writing

                JOptionPane.showMessageDialog(this, "Appointment has been successfully rescheduled!", "Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(cardPanel, "Student View Booking Panel");

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error writing to the appointment file.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace(); // For debugging purposes
            }
        }
        else {
        	JOptionPane.showMessageDialog(this, "Appointment is not found in text file", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }


    public void setDetails(String lecturer, String date, String timeFrom, String timeTo) {
        this.lecturer = lecturer;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.originalDate = date;
        this.originalTimeFrom = timeFrom;
        this.originalTimeTo = timeTo;
//        System.out.println(lecturer);
        
        lecturerField.setText(lecturer);
        dateField.setText(date);
        timeFromField.setText(timeFrom);
        timeToField.setText(timeTo);
    }
    
    public String getOriginalDate() {
        return this.originalDate;
    }
    
    public String getOriginalTimeFrom() {
        return this.originalTimeFrom;
    }
    
    public String getOriginalTimeTo() {
        return this.originalTimeTo;
    }
}
