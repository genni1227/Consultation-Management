package utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
    private static final String file_path = "credentials.txt";

    // Successfully register account (both student/lecturer)
     public static void saveUserData(String email, String username, String fullname, String password, String userType) {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file_path, true))) {
            bf.write(email + ", " + username + ", " + fullname + ", " + password + ", " + userType + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
     public static boolean isUsernameTaken(String username) {
    	 File file = new File("credentials.txt");
    	    if (!file.exists()) {
    	        return false; 
    	    }

    	    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    	        String line;
    	        while ((line = br.readLine()) != null) {
    	            String[] parts = line.split(", ");
    	            if (parts.length > 1 && parts[1].trim().equalsIgnoreCase(username)) {
    	                return true; // Username found
    	            }
    	        }
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	    return false; // Username not found
    	}
     
     public static String getFullNameFromFile(String username) {
    	    String fullname = null;
    	    try (BufferedReader bf = new BufferedReader(new FileReader("credentials.txt"))) {
    	        String line;
    	        while ((line = bf.readLine()) != null) {
    	            String[] parts = line.split(", ");
    	            if (parts[1].trim().equals(username.trim())) { // Compare usernames
    	                fullname = parts[2].trim(); 
    	                break; // Exit loop once the username is found
    	            }
    	        }
    	    } catch (FileNotFoundException e) {
    	        System.err.println("File not found: " + e.getMessage());
    	    } catch (IOException e) {
    	        System.err.println("Error reading file: " + e.getMessage());
    	    }
    	    return fullname;
    	}

}
