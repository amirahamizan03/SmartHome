package smarthome;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeviceDAO {

    /*Update the current ON/OFF status in 'devices' table
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
    }*/

    /*Log device ON/OFF event to 'device_logs' table
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
    }*/

    // ✅ Log detailed user input/action to 'device_actions' table
    public void logAction(Device device, String action, String value) {
        String sql = "INSERT INTO device_actions (device_id, device_name, device_type, action, value) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, device.getDeviceId());
            stmt.setString(2, device.getName());
            stmt.setString(3, device.getType());
            stmt.setString(4, action);
            stmt.setString(5, value);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to log device action: " + e.getMessage());
        }
    }
}
