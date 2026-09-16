package com.collegelab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton clearButton;

    // Modern fonts
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 15);
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 15);

    public Login() {

        // Window settings
        setTitle("College Lab Equipment Maintenance System");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // ================= LEFT PANEL =================

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(330, 520));
        leftPanel.setBackground(new Color(35, 47, 62));
        leftPanel.setLayout(new GridBagLayout());

        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.gridx = 0;
        leftGbc.insets = new Insets(8, 25, 8, 25);

        JLabel iconLabel = new JLabel("⚙");
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 65));
        iconLabel.setForeground(Color.WHITE);

        JLabel systemLabel = new JLabel(
                "<html><center>COLLEGE LAB<br>EQUIPMENT<br>MAINTENANCE</center></html>"
        );

        systemLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        systemLabel.setForeground(Color.WHITE);
        systemLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subLabel = new JLabel(
                "<html><center>Smart • Simple • Organized<br>Laboratory Management</center></html>"
        );

        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subLabel.setForeground(new Color(210, 215, 220));
        subLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftGbc.gridy = 0;
        leftPanel.add(iconLabel, leftGbc);

        leftGbc.gridy = 1;
        leftPanel.add(systemLabel, leftGbc);

        leftGbc.gridy = 2;
        leftPanel.add(subLabel, leftGbc);

        // ================= RIGHT PANEL =================

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 45, 7, 45);

        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(titleFont);
        welcomeLabel.setForeground(new Color(35, 47, 62));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel loginLabel = new JLabel("Login to manage laboratory equipment");
        loginLabel.setFont(normalFont);
        loginLabel.setForeground(Color.GRAY);
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(normalFont);

        usernameField = new JTextField();
        usernameField.setFont(normalFont);
        usernameField.setPreferredSize(new Dimension(300, 42));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(normalFont);

        passwordField = new JPasswordField();
        passwordField.setFont(normalFont);
        passwordField.setPreferredSize(new Dimension(300, 42));

        // Buttons
        loginButton = new JButton("LOGIN");
        loginButton.setFont(buttonFont);
        loginButton.setPreferredSize(new Dimension(140, 42));
        loginButton.setBackground(new Color(35, 47, 62));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);

        clearButton = new JButton("CLEAR");
        clearButton.setFont(buttonFont);
        clearButton.setPreferredSize(new Dimension(140, 42));
        clearButton.setFocusPainted(false);

        // Add components
        gbc.gridy = 0;
        rightPanel.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        rightPanel.add(loginLabel, gbc);

        gbc.gridy = 2;
        rightPanel.add(Box.createVerticalStrut(15), gbc);

        gbc.gridy = 3;
        rightPanel.add(usernameLabel, gbc);

        gbc.gridy = 4;
        rightPanel.add(usernameField, gbc);

        gbc.gridy = 5;
        rightPanel.add(passwordLabel, gbc);

        gbc.gridy = 6;
        rightPanel.add(passwordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.add(loginButton);
        buttonPanel.add(clearButton);

        gbc.gridy = 7;
        rightPanel.add(buttonPanel, gbc);

        JLabel footerLabel = new JLabel(
                "© 2026 College Lab Maintenance System"
        );

        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 8;
        rightPanel.add(footerLabel, gbc);

     // ================= BUTTON ACTIONS =================

        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            Login.this,
                            "Please enter username and password.",
                            "Missing Information",
                            JOptionPane.WARNING_MESSAGE
                    );

                } else {

                    String sql =
                            "SELECT * FROM users WHERE username = ? AND password = ?";

                    try (Connection con = DBConnection.getConnection();
                         PreparedStatement pst = con.prepareStatement(sql)) {

                        pst.setString(1, username);
                        pst.setString(2, password);

                        ResultSet rs = pst.executeQuery();

                        if (rs.next()) {

                            JOptionPane.showMessageDialog(
                                    Login.this,
                                    "Login Successful!",
                                    "Welcome",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                            Dashboard dashboard = new Dashboard();
                            dashboard.setVisible(true);

                            dispose();

                        } else {

                            JOptionPane.showMessageDialog(
                                    Login.this,
                                    "Invalid username or password.",
                                    "Login Failed",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }

                    } catch (SQLException ex) {

                        JOptionPane.showMessageDialog(
                                Login.this,
                                "Database connection error!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        ex.printStackTrace();
                    }
                }
            }
        });

        clearButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            usernameField.requestFocus();
        });

        // Add panels
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);
        }
 // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }

    }