package com.collegelab;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Dashboard extends JFrame {

    private JLabel totalValue;
    private JLabel workingValue;
    private JLabel maintenanceValue;
    private JLabel damagedValue;

    public Dashboard() {

        setTitle("College Lab Equipment Maintenance System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ================= MAIN PANEL =================

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // ================= HEADER =================

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(35, 47, 62));
        header.setPreferredSize(new Dimension(900, 75));

        JLabel title = new JLabel(
                "  COLLEGE LAB EQUIPMENT MAINTENANCE SYSTEM"
        );

        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel welcome = new JLabel("Welcome, Admin  ");

        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        header.add(title, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);

        // ================= CARDS =================

        JPanel cardPanel = new JPanel(
                new GridLayout(2, 2, 20, 20)
        );

        cardPanel.setBackground(
                new Color(245, 247, 250)
        );

        cardPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 50, 30, 50
                )
        );

        cardPanel.add(createCard("TOTAL EQUIPMENT", 1));
        cardPanel.add(createCard("WORKING", 2));
        cardPanel.add(createCard("UNDER MAINTENANCE", 3));
        cardPanel.add(createCard("DAMAGED", 4));

        // ================= BUTTONS =================

        JPanel buttonPanel = new JPanel(
                new GridLayout(2, 3, 10, 10)
        );

        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 30, 15, 30
                )
        );

        JButton addButton = new JButton("ADD EQUIPMENT");
        JButton viewButton = new JButton("VIEW EQUIPMENT");
        JButton searchButton = new JButton("SEARCH");
        JButton maintenanceButton = new JButton("MAINTENANCE");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(maintenanceButton);
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(new JLabel(""));

        // ================= BUTTON ACTIONS =================

        addButton.addActionListener(e -> addEquipment());

        viewButton.addActionListener(e -> viewEquipment());

        searchButton.addActionListener(e -> searchEquipment());

        maintenanceButton.addActionListener(
                e -> maintenanceStatus()
        );

        // ================= ADD PANELS =================

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        updateCounts();
    }

    // =========================================================
    // CREATE CARD
    // =========================================================

    private JPanel createCard(String heading, int type) {

        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 220, 220)
                        ),
                        BorderFactory.createEmptyBorder(
                                15, 15, 15, 15
                        )
                )
        );

        JLabel headingLabel = new JLabel(
                heading,
                SwingConstants.CENTER
        );

        headingLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        JLabel valueLabel = new JLabel(
                "0",
                SwingConstants.CENTER
        );

        valueLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        35
                )
        );

        card.add(
                headingLabel,
                BorderLayout.NORTH
        );

        card.add(
                valueLabel,
                BorderLayout.CENTER
        );

        if (type == 1) {
            totalValue = valueLabel;
        }

        if (type == 2) {
            workingValue = valueLabel;
        }

        if (type == 3) {
            maintenanceValue = valueLabel;
        }

        if (type == 4) {
            damagedValue = valueLabel;
        }

        return card;
    }

    // =========================================================
    // ADD EQUIPMENT
    // =========================================================

    private void addEquipment() {

        JTextField nameField = new JTextField();
        JTextField labField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField dateField = new JTextField();

        JComboBox<String> statusBox =
                new JComboBox<>(
                        new String[]{
                                "Working",
                                "Under Maintenance",
                                "Damaged"
                        }
                );

        JPanel panel = new JPanel(
                new GridLayout(0, 2, 10, 10)
        );

        panel.add(new JLabel("Equipment Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Lab Name:"));
        panel.add(labField);

        panel.add(new JLabel("Category:"));
        panel.add(categoryField);

        panel.add(
                new JLabel(
                        "Purchase Date (YYYY-MM-DD):"
                )
        );

        panel.add(dateField);

        panel.add(new JLabel("Status:"));
        panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Equipment",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String name = nameField.getText().trim();
        String lab = labField.getText().trim();
        String category = categoryField.getText().trim();
        String date = dateField.getText().trim();

        String status =
                (String) statusBox.getSelectedItem();

        // Required fields

        if (
                name.isEmpty()
                || lab.isEmpty()
                || category.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill Equipment Name, Lab Name and Category.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Date validation

        if (!date.isEmpty()) {

            try {

                java.sql.Date.valueOf(date);

            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Date must be in YYYY-MM-DD format.",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        }

        // ================= INSERT =================

        String sql;

        if (date.isEmpty()) {

            sql =
                    "INSERT INTO equipment " +
                    "(equipment_name, lab_name, category, status) " +
                    "VALUES (?, ?, ?, ?)";

        } else {

            sql =
                    "INSERT INTO equipment " +
                    "(equipment_name, lab_name, category, purchase_date, status) " +
                    "VALUES (?, ?, ?, ?, ?)";
        }

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement pst =
                    con.prepareStatement(sql);

            pst.setString(1, name);
            pst.setString(2, lab);
            pst.setString(3, category);

            if (date.isEmpty()) {

                pst.setString(4, status);

            } else {

                pst.setDate(
                        4,
                        java.sql.Date.valueOf(date)
                );

                pst.setString(5, status);
            }

            pst.executeUpdate();

            pst.close();
            con.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Equipment saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            updateCounts();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to save equipment.\n\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // VIEW EQUIPMENT
    // =========================================================

    private void viewEquipment() {

        String sql =
                "SELECT equipment_id, equipment_name, lab_name, " +
                "purchase_date, status, category " +
                "FROM equipment " +
                "ORDER BY equipment_id";

        loadTable(
                sql,
                new String[]{},
                "All Equipment"
        );
    }

    // =========================================================
    // SEARCH EQUIPMENT
    // =========================================================

    private void searchEquipment() {

        String search =
                JOptionPane.showInputDialog(
                        this,
                        "Enter equipment name, lab name or category:"
                );

        if (
                search == null
                || search.trim().isEmpty()
        ) {

            return;
        }

        String sql =
                "SELECT equipment_id, equipment_name, lab_name, " +
                "purchase_date, status, category " +
                "FROM equipment " +
                "WHERE equipment_name LIKE ? " +
                "OR lab_name LIKE ? " +
                "OR category LIKE ? " +
                "ORDER BY equipment_id";

        String value =
                "%" + search.trim() + "%";

        loadTable(
                sql,
                new String[]{
                        value,
                        value,
                        value
                },
                "Search Results"
        );
    }

    // =========================================================
    // LOAD TABLE
    // =========================================================

    private void loadTable(
            String sql,
            String[] parameters,
            String title
    ) {

        DefaultTableModel model =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Equipment Name",
                                "Lab Name",
                                "Purchase Date",
                                "Status",
                                "Category"
                        },
                        0
                );

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement pst =
                    con.prepareStatement(sql);

            for (
                    int i = 0;
                    i < parameters.length;
                    i++
            ) {

                pst.setString(
                        i + 1,
                        parameters[i]
                );
            }

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                model.addRow(
                        new Object[]{
                                rs.getInt("equipment_id"),
                                rs.getString("equipment_name"),
                                rs.getString("lab_name"),
                                rs.getDate("purchase_date"),
                                rs.getString("status"),
                                rs.getString("category")
                        }
                );
            }

            rs.close();
            pst.close();
            con.close();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load equipment.\n\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (model.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No equipment found.",
                    title,
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        JTable table =
                new JTable(model);

        table.setRowHeight(28);

        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setPreferredSize(
                new Dimension(800, 350)
        );

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // MAINTENANCE STATUS
    // =========================================================

    private void maintenanceStatus() {

        String idText =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Equipment ID:"
                );

        if (
                idText == null
                || idText.trim().isEmpty()
        ) {

            return;
        }

        int id;

        try {

            id =
                    Integer.parseInt(
                            idText.trim()
                    );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid numeric ID.",
                    "Invalid ID",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String[] options = {
                "Working",
                "Under Maintenance",
                "Damaged"
        };

        String newStatus =
                (String) JOptionPane.showInputDialog(
                        this,
                        "Select new status:",
                        "Maintenance",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]
                );

        if (newStatus == null) {
            return;
        }

        String sql =
                "UPDATE equipment " +
                "SET status = ? " +
                "WHERE equipment_id = ?";

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement pst =
                    con.prepareStatement(sql);

            pst.setString(1, newStatus);
            pst.setInt(2, id);

            int rows =
                    pst.executeUpdate();

            pst.close();
            con.close();

            if (rows == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Equipment ID not found.",
                        "Not Found",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Equipment status updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            updateCounts();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update status.\n\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // UPDATE DASHBOARD COUNTS
    // =========================================================

    private void updateCounts() {

        try {

            Connection con =
                    DBConnection.getConnection();

            totalValue.setText(
                    getCount(
                            con,
                            "SELECT COUNT(*) FROM equipment"
                    )
            );

            workingValue.setText(
                    getCount(
                            con,
                            "SELECT COUNT(*) FROM equipment " +
                            "WHERE status = 'Working'"
                    )
            );

            maintenanceValue.setText(
                    getCount(
                            con,
                            "SELECT COUNT(*) FROM equipment " +
                            "WHERE status = 'Under Maintenance'"
                    )
            );

            damagedValue.setText(
                    getCount(
                            con,
                            "SELECT COUNT(*) FROM equipment " +
                            "WHERE status = 'Damaged'"
                    )
            );

            con.close();

        } catch (SQLException ex) {

            System.out.println(
                    "Dashboard count error: "
                            + ex.getMessage()
            );
        }
    }

    // =========================================================
    // GET COUNT
    // =========================================================

    private String getCount(
            Connection con,
            String sql
    ) throws SQLException {

        PreparedStatement pst =
                con.prepareStatement(sql);

        ResultSet rs =
                pst.executeQuery();

        String result = "0";

        if (rs.next()) {

            result =
                    String.valueOf(
                            rs.getInt(1)
                    );
        }

        rs.close();
        pst.close();

        return result;
    }

    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    Dashboard dashboard =
                            new Dashboard();

                    dashboard.setVisible(true);
                }
        );
    }
}