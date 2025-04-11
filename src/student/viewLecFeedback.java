package student;
import utils.User;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class viewLecFeedback extends JPanel {

    private JPanel contentPanel; // Main feedback content panel

    public viewLecFeedback(JPanel cardPanel, CardLayout cardLayout) {
        // Create a panel to hold the title label at the top
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel titleLabel = new JLabel("Lecturer's Feedback");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        titlePanel.add(titleLabel);

        // Add titlePanel to the top of the frame
        this.setLayout(new BorderLayout());
        this.add(titlePanel, BorderLayout.NORTH);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		refreshFeedback();
        	}
        });
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 13));
        titlePanel.add(btnRefresh);

        // Create a content panel with GridLayout to display feedback in two columns
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 2 columns, 10px gap between panels
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Wrap the content panel in a scroll pane and disable horizontal scroll
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10); // Smooth scrolling
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrolling
        this.add(scrollPane, BorderLayout.CENTER);

        // Initially, the feedback panel is empty
        clearFeedback();

        cardPanel.add(this, "viewStuFeedback");
    }

    /**
     * Refreshes the feedback content panel with updated feedback data.
     */
    private void refreshFeedback() {
        // Clear the existing content
        contentPanel.removeAll();

        // Read feedback data from the file
        List<String[]> feedbackData = readFeedbackFromFile("lec_feedback.txt");

        String user = User.getCurrentUser();
        boolean hasFeedback = false;

        // Set the layout as GridBagLayout for dynamic positioning
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin between panels
        gbc.fill = GridBagConstraints.NONE; // Prevent stretching
        gbc.anchor = GridBagConstraints.NORTHWEST; // Anchor to the top-left
        gbc.gridx = 0; // Start at the first column (left)
        gbc.gridy = 0; // Start at the first row
        gbc.weightx = 1.0; // Ensure columns stretch evenly
        gbc.weighty = 0.0; // Prevent vertical stretching

        // Loop through each feedback and display it
        for (String[] feedback : feedbackData) {
            String studentName = feedback[0];
            String lecName = feedback[1];
            String comment = feedback[4];

            if (studentName.equals(user)) {
                JPanel feedbackPanel = createFeedbackPanel(lecName, studentName, comment);
                feedbackPanel.setPreferredSize(new Dimension(300, 150)); // Fixed size for each feedback panel

                contentPanel.add(feedbackPanel, gbc); // Add feedback panel to content panel

                hasFeedback = true;

                // Move to the next column for the next feedback
                if (gbc.gridx == 0) {
                    gbc.gridx = 1; // If it's in the left column, move to the right column
                } else {
                    // After placing in the right column, move to the next row
                    gbc.gridx = 0; // Reset back to the left column
                    gbc.gridy++; // Move to the next row
                }
            }
        }

        // If no feedback, add a placeholder
        if (!hasFeedback) {
            JLabel noFeedbackLabel = new JLabel("There are no feedback for you currently");
            noFeedbackLabel.setFont(new Font("Tahoma", Font.ITALIC, 14));
            noFeedbackLabel.setHorizontalAlignment(SwingConstants.LEFT);
            contentPanel.add(noFeedbackLabel, gbc);
            JOptionPane.showMessageDialog(this, "Currently there are no feedback for you, " + user +  ".", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
        	JOptionPane.showMessageDialog(this, "Feedback updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        // Add a filler component to push all content upwards
        GridBagConstraints fillerGbc = new GridBagConstraints();
        fillerGbc.gridx = 0;
        fillerGbc.gridy = gbc.gridy + 1; // Add below the last row
        fillerGbc.weighty = 1.0; // Push everything upwards
        fillerGbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(new JPanel(), fillerGbc);

        // Revalidate and repaint the content panel to reflect changes
        contentPanel.revalidate();
        contentPanel.repaint();
        
        
    }

    /**
     * Clears the feedback panel content.
     */
    public void clearFeedback() {
        contentPanel.removeAll();

        // Display a placeholder message initially
        JLabel placeholderLabel = new JLabel("Click 'Refresh' to view feedback.");
        placeholderLabel.setFont(new Font("Tahoma", Font.ITALIC, 14));
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(placeholderLabel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Helper method to create each student's feedback panel.
     */
    private JPanel createFeedbackPanel(String lecName, String studentName, String feedback) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 150)); // Fixed size for each feedback panel
        panel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Margin inside the panel
        panel.setLayout(new BorderLayout()); // Use BorderLayout for better organization

        // Combine student's name and lecturer's name in a single label
        JLabel lblNames = new JLabel(lecName);
        lblNames.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblNames.setOpaque(true); // Make sure the label's background color is visible
        lblNames.setBackground(new Color(54,57,62));
        lblNames.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        lblNames.setForeground(Color.WHITE);
        lblNames.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblNames, BorderLayout.NORTH);
        
        // Feedback text area
        JTextArea feedbackArea = new JTextArea(3, 20); // 3 rows, 20 columns
        feedbackArea.setText(feedback);
        feedbackArea.setLineWrap(true); // Enable line wrapping
        feedbackArea.setWrapStyleWord(true); // Wrap at word boundaries
        feedbackArea.setEditable(false); // Make it non-editable
        feedbackArea.setBorder(new EmptyBorder(5, 5, 5, 5)); // Padding inside the text area

        // Wrap feedback text area in a scroll pane
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackArea);
        feedbackScrollPane.setPreferredSize(new Dimension(280, 100)); // Fixed size for text area
        panel.add(feedbackScrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Reads feedback data from a file.
     */
    private List<String[]> readFeedbackFromFile(String fileName) {
        List<String[]> feedbackList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split line by " | " separator
                String[] parts = line.split(" \\| ");
                if (parts.length == 5) {
                    feedbackList.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }
}
