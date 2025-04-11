package utils;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeListener;

public class TimePicker extends JFrame {
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private JComboBox<String> amPmComboBox;
    private JButton okButton;
    private TimeSelectedListener timeSelectedListener;

    // Listener interface
    public interface TimeSelectedListener {
        void onTimeSelected(String time);
    }

    public TimePicker(TimeSelectedListener listener) {
        timeSelectedListener = listener;  // Store listener

        setTitle("12-hour Time Picker");
        setSize(380, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        SpinnerModel hourModel = new SpinnerNumberModel(1, 1, 12, 1);
        hourSpinner = new JSpinner(hourModel);
        hourSpinner.setPreferredSize(new Dimension(80, 40));
        hourSpinner.setFont(new Font("Tahoma", Font.BOLD, 20));

        // Custom minute spinner allowing only 0 and 30
        Integer[] minutes = {0, 30};
        SpinnerModel minuteModel = new SpinnerListModel(minutes);
        minuteSpinner = new JSpinner(minuteModel);
        minuteSpinner.setPreferredSize(new Dimension(80, 40));
        minuteSpinner.setFont(new Font("Tahoma", Font.BOLD, 20));

        String[] amPmOptions = {"AM", "PM"};
        amPmComboBox = new JComboBox<>(amPmOptions);
        amPmComboBox.setPreferredSize(new Dimension(80, 40));
        amPmComboBox.setFont(new Font("Tahoma", Font.BOLD, 20));

        okButton = new JButton("OK");
        okButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        okButton.setPreferredSize(new Dimension(120, 40));
        okButton.addActionListener(e -> validateAndRetrieveTime());

        panel.add(hourSpinner);
        panel.add(new JLabel(":"));
        panel.add(minuteSpinner);
        panel.add(amPmComboBox);
        panel.add(okButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void validateAndRetrieveTime() {
        int hour = (int) hourSpinner.getValue();
        int minute = (int) minuteSpinner.getValue();
        String amPm = (String) amPmComboBox.getSelectedItem();

        if (hour < 1 || hour > 12 || minute < 0 || minute > 59) {
            JOptionPane.showMessageDialog(this, "Invalid time. Please select a valid hour and minute.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String formattedTime = String.format("%02d:%02d %s", hour, minute, amPm);
        if (timeSelectedListener != null) {
            timeSelectedListener.onTimeSelected(formattedTime);
        }
        dispose();
    }
}
//tme