package lecturer;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class pastAppointments extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public pastAppointments(JPanel cardPanel, CardLayout cardLayout) {
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;

        initialize();
    }

    private void initialize() {
        setLayout(null);

        JLabel lblNewLabel = new JLabel("Past Appointments");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel.setBounds(218, 28, 274, 64);
        add(lblNewLabel);

        String[] columnNames = {"Student Name", "Date", "Time", "Feedback"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.setRowHeight(25);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 87, 600, 352);
        add(scrollPane);

        JButton btnAddFeedback = new JButton("Add Feedback");
        btnAddFeedback.setBounds(361, 465, 144, 30);
        btnAddFeedback.addActionListener(e -> showFeedbackPage());
        add(btnAddFeedback);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(251, 465, 100, 30);
        btnRefresh.addActionListener(e -> refreshTableData());
        add(btnRefresh);
    }

    private void refreshTableData() {
        tableModel.setRowCount(0); // Clear existing rows

        File appointmentFile = new File("appointments.txt");
        File feedbackFile = new File("lec_feedback.txt");
        String user = User.getCurrentUser(); // Current lecturer name
        boolean hasPastAppt = false;

        if (!appointmentFile.exists()) {
            JOptionPane.showMessageDialog(this, "File appointments.txt not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader appointmentReader = new BufferedReader(new FileReader(appointmentFile))) {
            String line;
            while ((line = appointmentReader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 5 && parts[1].equals(user) && "Accept".equalsIgnoreCase(parts[4].trim())) {
                    String stuName = parts[0].trim();
                    String slotDate = parts[2].trim();
                    String slotTime = parts[3].trim();
                    String feedback = null; // Default to null

                    try {
                        String dateOnly = slotDate.split(" ")[0].trim();
                        LocalDate appointmentDate = LocalDate.parse(dateOnly, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        if (appointmentDate.isBefore(LocalDate.now())) {
                            // Check feedback file for corresponding feedback
                            if (feedbackFile.exists()) {
                                try (BufferedReader feedbackReader = new BufferedReader(new FileReader(feedbackFile))) {
                                    String feedbackLine;
                                    while ((feedbackLine = feedbackReader.readLine()) != null) {
                                        String[] feedbackParts = feedbackLine.split(" \\| ");
                                        if (feedbackParts.length >= 5
                                                && feedbackParts[0].trim().equals(stuName)
                                                && feedbackParts[1].trim().equals(user)
                                                && feedbackParts[2].trim().equals(slotDate)
                                                && feedbackParts[3].trim().equals(slotTime)
                                                && !feedbackParts[4].trim().isEmpty()) {
                                            feedback = "Provided";
                                            break;
                                        }
                                    }
                                } catch (IOException e) {
                                    JOptionPane.showMessageDialog(this, "Error reading feedback file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            hasPastAppt = true;
                            tableModel.addRow(new Object[]{stuName, slotDate, slotTime, feedback});
                        }
                    } catch (DateTimeParseException e) {
                        System.err.println("Invalid date format: " + slotDate);
                    }
                     
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (hasPastAppt) {
        	 JOptionPane.showMessageDialog(this, "Table updated with past appointments!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
        	 JOptionPane.showMessageDialog(this, "Currently there are no past appointments for " + user , "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void showFeedbackPage() {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to provide feedback.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Retrieve feedback status from the selected row
        String feedbackStatus = (String) tableModel.getValueAt(table.getSelectedRow(), 3);
        
        if ("Provided".equalsIgnoreCase(feedbackStatus)) {
            // Show message if feedback is already provided
            JOptionPane.showMessageDialog(this, "Feedback has already been provided for this appointment.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        } else {
        	String studentName = (String) table.getValueAt(table.getSelectedRow(), 0);
            String appointmentDate = (String) table.getValueAt(table.getSelectedRow(), 1);
            String appointmentTime = (String) table.getValueAt(table.getSelectedRow(), 2);

            provideStuFeedback feedbackPage = new provideStuFeedback(studentName, appointmentDate, appointmentTime, cardLayout, cardPanel, this);
            cardPanel.add(feedbackPage, "Provide Feedback Page");
            cardLayout.show(cardPanel, "Provide Feedback Page");
        }
    }

    public void updateFeedback(String studentName, String date, String time) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(studentName) &&
                tableModel.getValueAt(i, 1).equals(date) &&
                tableModel.getValueAt(i, 2).equals(time)) {
                tableModel.setValueAt("Provided", i, 3);
                break;
            }
        }
    }
}
