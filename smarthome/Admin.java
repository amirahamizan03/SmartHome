package smarthome;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class Admin extends user {
    private List<Device> managedDevices = new ArrayList<>();

    public Admin(String username, String password) {
        super(username, password);
    }

    public void addDevice(Device d) {
        managedDevices.add(d);
        System.out.println("Device added: " + d.getName());

        // Save to database
        try (Connection conn = DatabaseConnector.getConnection()) {
            String sql = "INSERT INTO devices (device_id, device_name, device_type, location, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, d.getDeviceId());
            stmt.setString(2, d.getName());
            stmt.setString(3, d.getType());
            stmt.setString(4, d.getLocation());
            stmt.setString(5, d.getStatus());
            stmt.executeUpdate();
            System.out.println("Device saved to database.");
        } catch (Exception e) {
            System.out.println("Failed to save device to database: " + e.getMessage());
        }
    }

    public void removeDevice(String deviceId) {
        managedDevices.removeIf(d -> d.getDeviceId().equals(deviceId));
        System.out.println("Device removed: " + deviceId);

        // Optional: Remove from DB
        try (Connection conn = DatabaseConnector.getConnection()) {
            String sql = "DELETE FROM devices WHERE device_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, deviceId);
            stmt.executeUpdate();
            System.out.println("Device removed from database.");
        } catch (Exception e) {
            System.out.println("Failed to remove from DB: " + e.getMessage());
        }
    }

    public void listDevices() {
        System.out.println("Managed devices for " + username + ":");
        for (Device d : managedDevices) {
            System.out.println("- " + d.getDeviceId() + ": " + d.getName() + " (" + d.getType() + ")");
        }
    }

    public List<Device> getManagedDevices() {
        return managedDevices;
    }
}
