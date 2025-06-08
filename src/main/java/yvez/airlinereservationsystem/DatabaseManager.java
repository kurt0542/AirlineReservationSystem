package yvez.airlinereservationsystem;

import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintStream;
import java.io.OutputStream;

public class DatabaseManager {
    private static final String DATABASE_PATH = System.getProperty("user.dir") + File.separator + "db" + File.separator + "airline.accdb";
    
    // Clean connection URL that avoids function loading issues
    private static final String CONNECTION_URL = "jdbc:ucanaccess://" + DATABASE_PATH + 
        ";memory=false;lobScale=1;showWarnings=false;openExclusive=false";
    
    private static Connection connection;

    public static void initializeDatabase() {
        // Temporarily redirect System.err to suppress warnings
        PrintStream originalErr = System.err;
        
        try {
            File dbFile = new File(DATABASE_PATH);
            if (!dbFile.exists()) {
                System.out.println("Creating new MS Access database at: " + DATABASE_PATH);
                dbFile.getParentFile().mkdirs();
            }

            // Suppress all output during connection
            System.setErr(new PrintStream(new OutputStream() {
                public void write(int b) {
                    // Ignore all error output during connection
                }
            }));
            
            // Disable all logging
            java.util.logging.LogManager.getLogManager().reset();
            java.util.logging.Logger.getGlobal().setLevel(java.util.logging.Level.OFF);
            
            // Load driver and connect
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection(CONNECTION_URL);
            
            // Restore System.err
            System.setErr(originalErr);
            System.out.println("Connected to MS Access database successfully!");

            createTables();
            insertSampleData();

        } catch (SQLException e) {
            System.setErr(originalErr);
            System.err.println("Database initialization error: " + e.getMessage());
            if (!e.getMessage().toLowerCase().contains("function") && 
                !e.getMessage().toLowerCase().contains("aggregate")) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.setErr(originalErr);
            System.err.println("UCanAccess driver not found: " + e.getMessage());
            System.err.println("Make sure ucanaccess JAR files are in your classpath");
            e.printStackTrace();
        } catch (Exception e) {
            System.setErr(originalErr);
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTables() throws SQLException {
        Statement stmt = connection.createStatement();
        
        // Create Users table without DEFAULT NOW()
        if (!tableExists("Users")) {
            String createUsersTable = """
                CREATE TABLE Users (
                    id COUNTER PRIMARY KEY,
                    username VARCHAR(50) NOT NULL,
                    password VARCHAR(50) NOT NULL,
                    email VARCHAR(100),
                    full_name VARCHAR(100),
                    phone VARCHAR(20),
                    created_date DATETIME
                )
            """;
            stmt.execute(createUsersTable);
            
            try {
                stmt.execute("CREATE UNIQUE INDEX idx_username ON Users (username)");
            } catch (SQLException e) {
                // Index might already exist
            }
        }

        // Create Flights table
        if (!tableExists("Flights")) {
            String createFlightsTable = """
                CREATE TABLE Flights (
                    flight_id COUNTER PRIMARY KEY,
                    flight_number VARCHAR(10) NOT NULL,
                    origin VARCHAR(50) NOT NULL,
                    destination VARCHAR(50) NOT NULL,
                    departure_time DATETIME NOT NULL,
                    arrival_time DATETIME NOT NULL,
                    aircraft_type VARCHAR(50),
                    total_seats INTEGER,
                    available_seats INTEGER,
                    price_coach DOUBLE,
                    price_business DOUBLE,
                    price_first DOUBLE,
                    status VARCHAR(20)
                )
            """;
            stmt.execute(createFlightsTable);
            
            try {
                stmt.execute("CREATE UNIQUE INDEX idx_flight_number ON Flights (flight_number)");
            } catch (SQLException e) {
                // Index might already exist
            }
        }

        // Create Bookings table without DEFAULT NOW()
        if (!tableExists("Bookings")) {
            String createBookingsTable = """
                CREATE TABLE Bookings (
                    booking_id COUNTER PRIMARY KEY,
                    booking_reference VARCHAR(20) NOT NULL,
                    user_id INTEGER,
                    flight_id INTEGER,
                    passenger_name VARCHAR(100) NOT NULL,
                    passenger_email VARCHAR(100),
                    passenger_phone VARCHAR(20),
                    seat_class VARCHAR(20) NOT NULL,
                    num_passengers INTEGER,
                    total_amount DOUBLE,
                    booking_date DATETIME,
                    status VARCHAR(20),
                    flight_type VARCHAR(20),
                    fare_type VARCHAR(20)
                )
            """;
            stmt.execute(createBookingsTable);
            
            try {
                stmt.execute("CREATE UNIQUE INDEX idx_booking_reference ON Bookings (booking_reference)");
            } catch (SQLException e) {
                // Index might already exist
            }
        }

        // Create Passengers table
        if (!tableExists("Passengers")) {
            String createPassengersTable = """
                CREATE TABLE Passengers (
                    passenger_id COUNTER PRIMARY KEY,
                    booking_id INTEGER NOT NULL,
                    first_name VARCHAR(50) NOT NULL,
                    last_name VARCHAR(50) NOT NULL,
                    date_of_birth DATETIME,
                    gender VARCHAR(10),
                    passport_number VARCHAR(20),
                    seat_number VARCHAR(10)
                )
            """;
            stmt.execute(createPassengersTable);
        }
        
        stmt.close();
    }

    private static boolean tableExists(String tableName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, tableName.toUpperCase(), null);
            boolean exists = rs.next();
            rs.close();
            return exists;
        } catch (SQLException e) {
            return false;
        }
    }

    private static void insertSampleData() throws SQLException {
        insertSampleUser("admin", "admin", "admin@bestfriend.com", "System Administrator", "+63-123-456-7890");
        insertSampleUser("user1", "password", "user1@email.com", "John Doe", "+63-987-654-3210");

        insertSampleFlight("BF001", "Manila", "Cebu", "2025-06-15 08:00:00", "2025-06-15 10:30:00", "Boeing 737", 5000.00, 7500.00, 12000.00);
        insertSampleFlight("BF002", "Cebu", "Manila", "2025-06-15 14:00:00", "2025-06-15 16:30:00", "Boeing 737", 5000.00, 7500.00, 12000.00);
        insertSampleFlight("BF003", "Manila", "Davao", "2025-06-16 09:00:00", "2025-06-16 11:45:00", "Airbus A320", 5500.00, 8000.00, 13000.00);
        insertSampleFlight("BF004", "Manila", "Iloilo", "2025-06-17 07:30:00", "2025-06-17 09:15:00", "Boeing 737", 4800.00, 7200.00, 11500.00);
    }

    private static void insertSampleUser(String username, String password, String email, String fullName, String phone) {
        try {
            String checkUser = "SELECT COUNT(*) FROM Users WHERE username = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkUser);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {
                String insertUser = "INSERT INTO Users (username, password, email, full_name, phone, created_date) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(insertUser);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, email);
                stmt.setString(4, fullName);
                stmt.setString(5, phone);
                stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                stmt.executeUpdate();
                stmt.close();
            }
            rs.close();
            checkStmt.close();
        } catch (SQLException e) {
            System.err.println("Error inserting sample user: " + e.getMessage());
        }
    }

    private static void insertSampleFlight(String flightNumber, String origin, String destination,
                                           String departureTime, String arrivalTime, String aircraftType,
                                           double coachPrice, double businessPrice, double firstPrice) {
        try {
            String checkFlight = "SELECT COUNT(*) FROM Flights WHERE flight_number = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkFlight);
            checkStmt.setString(1, flightNumber);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {
                String insertFlight = """
                    INSERT INTO Flights (flight_number, origin, destination, departure_time, arrival_time,
                                         aircraft_type, total_seats, available_seats, price_coach, price_business, price_first, status)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
                PreparedStatement stmt = connection.prepareStatement(insertFlight);
                stmt.setString(1, flightNumber);
                stmt.setString(2, origin);
                stmt.setString(3, destination);
                stmt.setTimestamp(4, Timestamp.valueOf(departureTime));
                stmt.setTimestamp(5, Timestamp.valueOf(arrivalTime));
                stmt.setString(6, aircraftType);
                stmt.setInt(7, 150);
                stmt.setInt(8, 150);
                stmt.setDouble(9, coachPrice);
                stmt.setDouble(10, businessPrice);
                stmt.setDouble(11, firstPrice);
                stmt.setString(12, "ACTIVE");
                stmt.executeUpdate();
                stmt.close();
            }
            rs.close();
            checkStmt.close();
        } catch (SQLException e) {
            System.err.println("Error inserting sample flight: " + e.getMessage());
        }
    }

    // Rest of the methods remain the same...
    public static boolean validateUser(String username, String password) {
        try {
            String query = "SELECT id FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            boolean isValid = rs.next();
            rs.close();
            stmt.close();
            return isValid;
        } catch (SQLException e) {
            System.err.println("Error validating user: " + e.getMessage());
            return false;
        }
    }

    public static int getUserId(String username) {
        try {
            String query = "SELECT id FROM Users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                rs.close();
                stmt.close();
                return userId;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting user ID: " + e.getMessage());
        }
        return -1;
    }

    public static List<Flight> searchFlights(String origin, String destination) {
        List<Flight> flights = new ArrayList<>();
        try {
            String query = """
                SELECT flight_id, flight_number, origin, destination, departure_time,
                       arrival_time, aircraft_type, available_seats, price_coach,
                       price_business, price_first, status
                FROM Flights
                WHERE UCase(origin) LIKE UCase(?) AND UCase(destination) LIKE UCase(?)
                AND status = 'ACTIVE' AND available_seats > 0
                ORDER BY departure_time
            """;
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + origin + "%");
            stmt.setString(2, "%" + destination + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Flight flight = new Flight();
                flight.setFlightId(rs.getInt("flight_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setOrigin(rs.getString("origin"));
                flight.setDestination(rs.getString("destination"));
                flight.setDepartureTime(rs.getTimestamp("departure_time"));
                flight.setArrivalTime(rs.getTimestamp("arrival_time"));
                flight.setAircraftType(rs.getString("aircraft_type"));
                flight.setAvailableSeats(rs.getInt("available_seats"));
                flight.setCoachPrice(rs.getDouble("price_coach"));
                flight.setBusinessPrice(rs.getDouble("price_business"));
                flight.setFirstPrice(rs.getDouble("price_first"));
                flight.setStatus(rs.getString("status"));
                flights.add(flight);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error searching flights: " + e.getMessage());
        }
        return flights;
    }

    public static String createBooking(BookingData bookingData) {
        try {
            String bookingRef = "BF" + System.currentTimeMillis();

            String insertBooking = """
                INSERT INTO Bookings (booking_reference, user_id, flight_id, passenger_name, passenger_email,
                                      passenger_phone, seat_class, num_passengers, flight_type,
                                      fare_type, total_amount, booking_date, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement stmt = connection.prepareStatement(insertBooking);
            stmt.setString(1, bookingRef);
            stmt.setInt(2, bookingData.getUserId());
            stmt.setInt(3, bookingData.getFlightId());
            stmt.setString(4, bookingData.getPassengerName());
            stmt.setString(5, bookingData.getPassengerEmail());
            stmt.setString(6, bookingData.getPassengerPhone());
            stmt.setString(7, bookingData.getSeatClass());
            stmt.setInt(8, bookingData.getNumPassengers());
            stmt.setString(9, bookingData.getFlightType());
            stmt.setString(10, bookingData.getFareType());
            stmt.setDouble(11, bookingData.getTotalAmount());
            stmt.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
            stmt.setString(13, "CONFIRMED");

            int result = stmt.executeUpdate();
            stmt.close();

            if (result > 0) {
                updateAvailableSeats(bookingData.getFlightId(), bookingData.getNumPassengers());
                return bookingRef;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            return null;
        }
    }

    private static void updateAvailableSeats(int flightId, int numPassengers) {
        try {
            String updateSeats = "UPDATE Flights SET available_seats = available_seats - ? WHERE flight_id = ?";
            PreparedStatement stmt = connection.prepareStatement(updateSeats);
            stmt.setInt(1, numPassengers);
            stmt.setInt(2, flightId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error updating available seats: " + e.getMessage());
        }
    }

    public static List<BookingData> getUserBookings(int userId) {
        List<BookingData> bookings = new ArrayList<>();
        try {
            String query = """
                SELECT b.booking_id, b.booking_reference, b.user_id, b.flight_id,
                       b.passenger_name, b.passenger_email, b.passenger_phone,
                       b.seat_class, b.num_passengers, b.total_amount, b.booking_date,
                       b.status, b.flight_type, b.fare_type,
                       f.origin, f.destination, f.departure_time, f.flight_number
                FROM Bookings b
                LEFT JOIN Flights f ON b.flight_id = f.flight_id
                WHERE b.user_id = ?
                ORDER BY b.booking_date DESC
            """;
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BookingData booking = new BookingData();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setBookingReference(rs.getString("booking_reference"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setPassengerName(rs.getString("passenger_name"));
                booking.setPassengerEmail(rs.getString("passenger_email"));
                booking.setPassengerPhone(rs.getString("passenger_phone"));
                booking.setSeatClass(rs.getString("seat_class"));
                booking.setNumPassengers(rs.getInt("num_passengers"));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setBookingDate(rs.getTimestamp("booking_date"));
                booking.setStatus(rs.getString("status"));
                booking.setFlightType(rs.getString("flight_type"));
                booking.setFareType(rs.getString("fare_type"));
                booking.setOrigin(rs.getString("origin"));
                booking.setDestination(rs.getString("destination"));
                
                Timestamp depTime = rs.getTimestamp("departure_time");
                if (depTime != null) {
                    booking.setDepartureDate(depTime.toString());
                }
                
                bookings.add(booking);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting user bookings: " + e.getMessage());
        }
        return bookings;
    }

    public static BookingData getBookingByReference(String bookingReference) {
        try {
            String query = """
                SELECT b.booking_id, b.booking_reference, b.user_id, b.flight_id,
                       b.passenger_name, b.passenger_email, b.passenger_phone,
                       b.seat_class, b.num_passengers, b.total_amount, b.booking_date,
                       b.status, b.flight_type, b.fare_type,
                       f.origin, f.destination, f.departure_time, f.flight_number
                FROM Bookings b
                LEFT JOIN Flights f ON b.flight_id = f.flight_id
                WHERE b.booking_reference = ?
            """;
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, bookingReference);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                BookingData booking = new BookingData();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setBookingReference(rs.getString("booking_reference"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setFlightId(rs.getInt("flight_id"));
                booking.setPassengerName(rs.getString("passenger_name"));
                booking.setPassengerEmail(rs.getString("passenger_email"));
                booking.setPassengerPhone(rs.getString("passenger_phone"));
                booking.setSeatClass(rs.getString("seat_class"));
                booking.setNumPassengers(rs.getInt("num_passengers"));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setBookingDate(rs.getTimestamp("booking_date"));
                booking.setStatus(rs.getString("status"));
                booking.setFlightType(rs.getString("flight_type"));
                booking.setFareType(rs.getString("fare_type"));
                booking.setOrigin(rs.getString("origin"));
                booking.setDestination(rs.getString("destination"));
                
                Timestamp depTime = rs.getTimestamp("departure_time");
                if (depTime != null) {
                    booking.setDepartureDate(depTime.toString());
                }
                
                rs.close();
                stmt.close();
                return booking;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting booking by reference: " + e.getMessage());
        }
        return null;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}