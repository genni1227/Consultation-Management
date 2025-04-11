package utils;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeValidate {

    private String timeFrom;
    private String timeTo;

    // Constructor to initialize timeFrom and timeTo
    public TimeValidate(String timeFrom, String timeTo) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    // Method to validate time range
    public boolean isValidTimeRange() {
        // Define the 12-hour format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        try {
            // Parse the time strings into LocalTime objects
            LocalTime timeFromParsed = LocalTime.parse(timeFrom, formatter);
            LocalTime timeToParsed = LocalTime.parse(timeTo, formatter);

            // Check if 'timeTo' is after 'timeFrom'
            return timeToParsed.isAfter(timeFromParsed);
        } catch (Exception e) {
            // In case of parsing error, the times are invalid
            System.out.println("Invalid time format.");
            return false;
        }
    }

    // Getter methods for timeFrom and timeTo
    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }
}
