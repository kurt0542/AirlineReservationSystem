/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package yvez.airlinereservationsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;

public class LoginModule extends JFrame {
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color LIGHT_BLUE = new Color(174, 214, 241);
    private static final Color DARK_BLUE = new Color(23, 32, 42);
    private static final Color ACCENT_COLOR = new Color(39, 174, 96);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color BORDER_COLOR = new Color(220, 221, 225);
    private static final Color TEXT_COLOR = new Color(52, 58, 64);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private static int currentUserId = -1;
    private static String currentUsername = "";

    public LoginModule() {
        setTitle("BestFriend Travel - Login");
        setSize(500, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, LIGHT_BLUE, 0, getHeight(), Color.WHITE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContainer.setLayout(new GridBagLayout());
        add(mainContainer);

        JPanel loginCard = createLoginCard();
        mainContainer.add(loginCard);

        setVisible(true);
    }

    private JPanel createLoginCard() {
        RoundedPanel card = new RoundedPanel(22);
        card.setPreferredSize(new Dimension(380, 480));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        JPanel headerPanel = createHeaderPanel();
        card.add(headerPanel);
        card.add(Box.createVerticalStrut(28));

        JPanel iconPanel = createIconPanel();
        card.add(iconPanel);
        card.add(Box.createVerticalStrut(24));

        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(DARK_BLUE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(welcomeLabel);

        JLabel subtitleLabel = new JLabel("Please sign in to your account");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(18));

        JPanel formPanel = createFormPanel();
        card.add(formPanel);

        return card;
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel logoLabel = new JLabel("✈ BESTFRIEND");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        logoLabel.setForeground(DARK_BLUE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel taglineLabel = new JLabel("TOUR & TRAVEL AGENCY");
        taglineLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        taglineLabel.setForeground(TEXT_COLOR);
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(logoLabel);
        header.add(Box.createVerticalStrut(4));
        header.add(taglineLabel);

        return header;
    }

    private JPanel createIconPanel() {
        JPanel iconContainer = new JPanel();
        iconContainer.setOpaque(false);
        iconContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JPanel iconCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_BLUE, getWidth(), getHeight(), DARK_BLUE);
                g2.setPaint(gradient);
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
            @Override
            public boolean isOpaque() { return false; }
        };
        iconCircle.setPreferredSize(new Dimension(75, 75));
        iconCircle.setLayout(new GridBagLayout());
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Arial", Font.BOLD, 32));
        userIcon.setForeground(Color.WHITE);
        iconCircle.add(userIcon);
        iconContainer.add(iconCircle);
        return iconContainer;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_COLOR);

        usernameField = createStyledTextField("Enter your username");

        JPanel usernameFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        usernameFieldPanel.setOpaque(false);
        usernameFieldPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_COLOR);

        passwordField = createStyledPasswordField("Enter your password");

        JPanel passwordFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        passwordFieldPanel.setOpaque(false);
        passwordFieldPanel.add(passwordField);

        loginButton = createStyledButton("LOGIN");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);

        // Create Register button
        JButton registerButton = createStyledButton("REGISTER");
        registerButton.setBackground(ACCENT_COLOR);
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                registerButton.setBackground(new Color(46, 204, 113));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                registerButton.setBackground(ACCENT_COLOR);
            }
        });
        registerButton.addActionListener(e -> showRegistrationDialog());
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(registerButton);

        JLabel forgotPasswordLabel = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotPasswordLabel.setForeground(PRIMARY_BLUE);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        forgotPanel.setOpaque(false);
        forgotPanel.add(forgotPasswordLabel);

        loginButton.addActionListener(e -> performLogin());

        // Add Enter key listener for login
        usernameField.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());

        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameFieldPanel);
        formPanel.add(Box.createVerticalStrut(18));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordFieldPanel);
        formPanel.add(Box.createVerticalStrut(22));
        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(forgotPanel);

        formPanel.add(Box.createVerticalGlue());

        return formPanel;
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || username.equals("Enter your username")) {
            showErrorMessage("Please enter your username");
            return;
        }
        if (password.isEmpty() || password.equals("Enter your password")) {
            showErrorMessage("Please enter your password");
            return;
        }

        // Show loading state
        loginButton.setEnabled(false);
        loginButton.setText("LOGGING IN...");

        // Perform login in background thread
        SwingWorker<Boolean, Void> loginWorker = new SwingWorker<Boolean, Void>() {
            private int userId = -1;
            private String fullName = "";

            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    Connection conn = DatabaseManager.getConnection();
                    if (conn == null) {
                        return false;
                    }

                    String query = "SELECT id, full_name FROM Users WHERE username = ? AND password = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        userId = rs.getInt("id");
                        fullName = rs.getString("full_name");
                        stmt.close();
                        return true;
                    }
                    stmt.close();
                    return false;
                } catch (SQLException e) {
                    System.err.println("Login error: " + e.getMessage());
                    return false;
                }
            }

            @Override
            protected void done() {
                loginButton.setEnabled(true);
                loginButton.setText("LOGIN");

                try {
                    if (get()) {
                        currentUserId = userId;
                        currentUsername = username;
                        showSuccessMessage("Welcome back, " + (fullName != null ? fullName : username) + "!");
                        dispose();
                        SwingUtilities.invokeLater(() -> new BookingModule());
                    } else {
                        showErrorMessage("Invalid username or password!");
                        passwordField.setText("");
                        passwordField.requestFocus();
                    }
                } catch (Exception e) {
                    showErrorMessage("Login failed. Please try again.");
                }
            }
        };

        loginWorker.execute();
    }

    private void showRegistrationDialog() {
        JDialog regDialog = new JDialog(this, "Register New Account", true);
        regDialog.setSize(400, 500);
        regDialog.setLocationRelativeTo(this);

        JPanel regPanel = new JPanel();
        regPanel.setLayout(new BoxLayout(regPanel, BoxLayout.Y_AXIS));
        regPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField regUsernameField = createStyledTextField("Username");
        JPasswordField regPasswordField = createStyledPasswordField("Password");
        JPasswordField confirmPasswordField = createStyledPasswordField("Confirm Password");
        JTextField fullNameField = createStyledTextField("Full Name");
        JTextField emailField = createStyledTextField("Email");
        JTextField phoneField = createStyledTextField("Phone Number");

        JButton createAccountButton = createStyledButton("CREATE ACCOUNT");
        JButton cancelButton = createStyledButton("CANCEL");
        cancelButton.setBackground(Color.GRAY);

        createAccountButton.addActionListener(e -> {
            String newUsername = regUsernameField.getText().trim();
            String newPassword = new String(regPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (newUsername.isEmpty() || newPassword.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(regDialog, "Please fill in all required fields");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(regDialog, "Passwords do not match");
                return;
            }

            if (registerUser(newUsername, newPassword, fullName, email, phone)) {
                JOptionPane.showMessageDialog(regDialog, "Account created successfully!");
                regDialog.dispose();
                usernameField.setText(newUsername);
                passwordField.setText(newPassword);
            } else {
                JOptionPane.showMessageDialog(regDialog, "Registration failed. Username might already exist.");
            }
        });

        cancelButton.addActionListener(e -> regDialog.dispose());

        regPanel.add(titleLabel);
        regPanel.add(Box.createVerticalStrut(20));
        regPanel.add(new JLabel("Username *"));
        regPanel.add(regUsernameField);
        regPanel.add(Box.createVerticalStrut(10));
        regPanel.add(new JLabel("Password *"));
        regPanel.add(regPasswordField);
        regPanel.add(Box.createVerticalStrut(10));
        regPanel.add(new JLabel("Confirm Password *"));
        regPanel.add(confirmPasswordField);
        regPanel.add(Box.createVerticalStrut(10));
        regPanel.add(new JLabel("Full Name *"));
        regPanel.add(fullNameField);
        regPanel.add(Box.createVerticalStrut(10));
        regPanel.add(new JLabel("Email"));
        regPanel.add(emailField);
        regPanel.add(Box.createVerticalStrut(10));
        regPanel.add(new JLabel("Phone"));
        regPanel.add(phoneField);
        regPanel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(createAccountButton);
        buttonPanel.add(cancelButton);
        regPanel.add(buttonPanel);

        regDialog.add(regPanel);
        regDialog.setVisible(true);
    }

    private boolean registerUser(String username, String password, String fullName, String email, String phone) {
        try {
            Connection conn = DatabaseManager.getConnection();
            if (conn == null) return false;

            String insertQuery = "INSERT INTO Users (username, password, full_name, email, phone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, email);
            stmt.setString(5, phone);

            int result = stmt.executeUpdate();
            stmt.close();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(300, 43));
        field.setPreferredSize(new Dimension(300, 43));
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });

        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(300, 43));
        field.setPreferredSize(new Dimension(300, 43));
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0); 
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String pwd = String.valueOf(field.getPassword());
                if (pwd.equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                    field.setEchoChar('●');
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                String pwd = String.valueOf(field.getPassword());
                if (pwd.isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char) 0);
                }
            }
        });

        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(300, 43));
        button.setPreferredSize(new Dimension(300, 43));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(DARK_BLUE);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_BLUE);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(DARK_BLUE);
            }
        });

        return button;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Static getters for current user info
    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }
}