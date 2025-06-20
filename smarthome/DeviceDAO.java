package smarthome;

import java.sql.*;

public class DeviceDAO {

    public void updateDeviceStatus(Device device) {
        String sql = "UPDATE devices SET status = ? WHERE device_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, device.getStatus());
            stmt.setString(2, device.getDeviceId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to update device: " + e.getMessage());
        }
    }

    public void logDeviceStatus(Device device) {
        String sql = "INSERT INTO device_logs (device_id, status) VALUES (?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, device.getDeviceId());
            stmt.setString(2, device.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to log device status: " + e.getMessage());
        }
    }
}
