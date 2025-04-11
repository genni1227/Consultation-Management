package student;
import java.util.List; // Correct import statement for List
import java.util.ArrayList; // Import ArrayList for implementing List

public class ConsultationData {
    private static List<Consultation> consultations = new ArrayList<>();

    // Update or add consultation
    public static void updateConsultation(String lecturer, String date, String time) {
        Consultation newConsultation = new Consultation(lecturer, date, time);
        // Assuming we replace or add a new consultation here
        consultations.add(newConsultation);
    }

    // Method to get all consultations
    public static List<Consultation> getConsultations() {
        return consultations;
    }
}

class Consultation {
    private String lecturer;
    private String date;
    private String time;

    public Consultation(String lecturer, String date, String time) {
        this.lecturer = lecturer;
        this.date = date;
        this.time = time;
    }

    // Getter methods
    public String getLecturer() { return lecturer; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}
