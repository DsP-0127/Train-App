import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class TradingApp extends JFrame implements ActionListener {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/TradingDB";
    private static final String USER = "root";
    private static final String PASSWORD = "jagruti@1979"; //durgesh@112233
    private JButton addClientButton;
    private JButton viewClientsButton;
    private JButton deleteClientButton;
    private JButton updateClientInfoButton;
    private JTextField clientIdField;
    private JTextField clientNameField;
    private JTextField clientEmailField;

    public TradingApp() {
        setTitle("Trading App");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        addClientButton = new JButton("Add Client");
        viewClientsButton = new JButton("View Clients");
        deleteClientButton = new JButton("Delete Client");
        updateClientInfoButton = new JButton("Update Client Info");
        clientIdField = new JTextField(10);
        clientNameField = new JTextField(10);
        clientEmailField = new JTextField(10);

        addClientButton.addActionListener(this);
        viewClientsButton.addActionListener(this);
        deleteClientButton.addActionListener(this);
        updateClientInfoButton.addActionListener(this);

        add(new JLabel("Client ID: "));
        add(clientIdField);
        add(new JLabel("Name: "));
        add(clientNameField);
        add(new JLabel("Email: "));
        add(clientEmailField);
        add(addClientButton);
        add(viewClientsButton);
        add(deleteClientButton);
        add(updateClientInfoButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addClientButton) {
            addClient();
        } else if (e.getSource() == viewClientsButton) {
            viewClients();
        } else if (e.getSource() == deleteClientButton) {
            deleteClient();
        } else if (e.getSource() == updateClientInfoButton) {
            updateClientInfo();
        }
    }

    private void addClient() {
        String name = clientNameField.getText();
        String email = clientEmailField.getText();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Clients (name, email) VALUES (?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Client added successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add client");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void viewClients() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Clients")) {
            StringBuilder sb = new StringBuilder("Clients:\n");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                sb.append("ID: ").append(id).append(", Name: ").append(name).append(", Email: ").append(email).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteClient() {
        int clientId = Integer.parseInt(clientIdField.getText());
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Clients WHERE id = ?")) {
            stmt.setInt(1, clientId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Client deleted successfully");
            } else {
                JOptionPane.showMessageDialog(this, "No client found with ID: " + clientId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateClientInfo() {
        int clientId = Integer.parseInt(clientIdField.getText());
        String newName = clientNameField.getText();
        String newEmail = clientEmailField.getText();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("UPDATE Clients SET name = ?, email = ? WHERE id = ?")) {
            stmt.setString(1, newName);
            stmt.setString(2, newEmail);
            stmt.setInt(3, clientId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Client information updated successfully");
            } else {
                JOptionPane.showMessageDialog(this, "No client found with ID: " + clientId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        TradingApp app = new TradingApp();
        app.setVisible(true);
    }
}
