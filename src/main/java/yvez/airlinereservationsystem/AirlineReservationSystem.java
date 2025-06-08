

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package yvez.airlinereservationsystem;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class AirlineReservationSystem {
    public static void main(String[] args) {
        try {
            // Initialize database connection and create tables
            System.out.println("Initializing Airline Reservation System...");
            DatabaseManager.initializeDatabase();
            
            // Add shutdown hook to close database connection properly
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down application...");
                DatabaseManager.closeConnection();
            }));
            
            // Launch the GUI application
            SwingUtilities.invokeLater(() -> {
                try {
                    new LoginModule();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, 
                        "Error starting application: " + e.getMessage(), 
                        "Application Error", 
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            });
            
        } catch (Exception e) {
            System.err.println("Failed to initialize application: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Failed to initialize database. Please ensure MS Access is supported on your system.\n" +
                "Error: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}