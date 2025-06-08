/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package yvez.airlinereservationsystem;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Flight {
    private int flightId;
    private String flightNumber;
    private String origin;
    private String destination;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private String aircraftType;
    private int totalSeats;
    private int availableSeats;
    private double coachPrice;
    private double businessPrice;
    private double firstPrice;
    private String status;
    
    // Constructors
    public Flight() {}
    
    public Flight(String flightNumber, String origin, String destination, 
                 Timestamp departureTime, Timestamp arrivalTime) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
    
    // Getters and Setters
    public int getFlightId() { return flightId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }
    
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public Timestamp getDepartureTime() { return departureTime; }
    public void setDepartureTime(Timestamp departureTime) { this.departureTime = departureTime; }
    
    public Timestamp getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(Timestamp arrivalTime) { this.arrivalTime = arrivalTime; }
    
    public String getAircraftType() { return aircraftType; }
    public void setAircraftType(String aircraftType) { this.aircraftType = aircraftType; }
    
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    
    public double getCoachPrice() { return coachPrice; }
    public void setCoachPrice(double coachPrice) { this.coachPrice = coachPrice; }
    
    public double getBusinessPrice() { return businessPrice; }
    public void setBusinessPrice(double businessPrice) { this.businessPrice = businessPrice; }
    
    public double getFirstPrice() { return firstPrice; }
    public void setFirstPrice(double firstPrice) { this.firstPrice = firstPrice; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    // Utility methods
    public String getFormattedDepartureTime() {
        if (departureTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            return sdf.format(departureTime);
        }
        return "";
    }
    
    public String getFormattedArrivalTime() {
        if (arrivalTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            return sdf.format(arrivalTime);
        }
        return "";
    }
    
    public double getPriceByClass(String seatClass) {
        switch (seatClass.toLowerCase()) {
            case "business":
                return businessPrice;
            case "first class":
            case "first":
                return firstPrice;
            case "coach":
            default:
                return coachPrice;
        }
    }
    
    public String getDuration() {
        if (departureTime != null && arrivalTime != null) {
            long diffInMillis = arrivalTime.getTime() - departureTime.getTime();
            long hours = diffInMillis / (60 * 60 * 1000);
            long minutes = (diffInMillis % (60 * 60 * 1000)) / (60 * 1000);
            return String.format("%dh %dm", hours, minutes);
        }
        return "";
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s → %s (%s - %s) - %s seats available", 
                           flightNumber, origin, destination, 
                           getFormattedDepartureTime(), getFormattedArrivalTime(), 
                           availableSeats);
    }
}