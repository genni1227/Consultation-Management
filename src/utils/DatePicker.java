package utils;
import com.toedter.calendar.JCalendar;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePicker extends JFrame {
    private JCalendar calendar;
    private JLabel selectedDateLabel;
    private JButton okButton;
    private DateSelectedListener listener;

    public DatePicker(DateSelectedListener listener) {
        this.listener = listener; // Initialize the listener
        Locale.setDefault(Locale.US); // Set locale to ensure correct day names

        // Set up frame
        setTitle("Select a Date");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create calendar
        JPanel panel = new JPanel(new BorderLayout());
        calendar = new JCalendar();
        panel.add(calendar, BorderLayout.CENTER);
        

        // Get the current date and disable previous dates
        calendar.setSelectableDateRange(Calendar.getInstance().getTime(), null);  // Correct usage of Calendar.getInstance()
        panel.add(calendar, BorderLayout.CENTER);

        // Label for showing the selected date
        selectedDateLabel = new JLabel("Selected Date: ");
        panel.add(selectedDateLabel, BorderLayout.NORTH);

        // OK button to confirm date selection
        okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            // Get the selected date and format it
            Date selectedDate = calendar.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd (EEEE)");
            String formattedDate = sdf.format(selectedDate);

            // Pass the formatted date to the listener
            listener.onDateSelected(formattedDate);
            dispose(); // Close the calendar window
        });

        // Add the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to frame and display
        add(panel);
        setVisible(true);
    }

    // Callback interface
    public interface DateSelectedListener {
        void onDateSelected(String formattedDate);
    }
}
