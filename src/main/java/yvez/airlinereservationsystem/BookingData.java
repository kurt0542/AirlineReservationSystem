/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package yvez.airlinereservationsystem;

import java.sql.Timestamp;

public class BookingData {
    private int bookingId;
    private String bookingReference;
    private int userId;
    private int flightId;
    private String passengerName;
    private String passengerEmail;
    private String passengerPhone;
    private String seatClass;
    private int numPassengers;
    private double totalAmount;
    private Timestamp bookingDate;
    private String status;
    private String flightType;
    private String fareType;
    private String origin;
    private String destination;
    private String departureDate;
    private String returnDate;
    
    // Constructors
    public BookingData() {
        this.bookingDate = new Timestamp(System.currentTimeMillis());
        this.status = "CONFIRMED";
    }
    
    public BookingData(String passengerName, String origin, String destination, 
                      String seatClass, int numPassengers, String flightType, String fareType) {
        this();
        this.passengerName = passengerName;
        this.origin = origin;
        this.destination = destination;
        this.seatClass = seatClass;
        this.numPassengers = numPassengers;
        this.flightType = flightType;
        this.fareType = fareType;
    }
    
    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    
    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getFlightId() { return flightId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }
    
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    
    public String getPassengerEmail() { return passengerEmail; }
    public void setPassengerEmail(String passengerEmail) { this.passengerEmail = passengerEmail; }
    
    public String getPassengerPhone() { return passengerPhone; }
    public void setPassengerPhone(String passengerPhone) { this.passengerPhone = passengerPhone; }
    
    public String getSeatClass() { return seatClass; }
    public void setSeatClass(String seatClass) { this.seatClass = seatClass; }
    
    public int getNumPassengers() { return numPassengers; }
    public void setNumPassengers(int numPassengers) { this.numPassengers = numPassengers; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public Timestamp getBookingDate() { return bookingDate; }
    public void setBookingDate(Timestamp bookingDate) { this.bookingDate = bookingDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getFlightType() { return flightType; }
    public void setFlightType(String flightType) { this.flightType = flightType; }
    
    public String getFareType() { return fareType; }
    public void setFareType(String fareType) { this.fareType = fareType; }
    
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public String getDepartureDate() { return departureDate; }
    public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }
    
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    
    // Utility methods
    public double calculateDiscountedAmount() {
        double baseAmount = totalAmount;
        if ("Student".equalsIgnoreCase(fareType)) {
            return baseAmount * 0.9; // 10% student discount
        }
        return baseAmount;
    }
    
    public String getFormattedAmount() {
        return String.format("₱%.2f", getTotalAmount());
    }
    
    @Override
    public String toString() {
        return String.format("Booking %s: %s (%d passengers) - %s → %s [%s]", 
                           bookingReference, passengerName, numPassengers, 
                           origin, destination, status);
    }
}