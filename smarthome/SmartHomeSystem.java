package smarthome;

import java.sql.*;
import java.util.*;

public class SmartHomeSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, Device> devices = new LinkedHashMap<>();
    private static final Map<String, Thread> threads = new LinkedHashMap<>();

    public static void main(String[] args) {
        System.out.println("***** WELCOME TO SMART HOME AUTOMATION SYSTEM *****");
        System.out.println("\nCHOOSE YOUR ROLE:");
        System.out.println("1) USER");
        System.out.println("2) ADMIN");
        System.out.print("Enter your choice (1 or 2): ");
        String role = scanner.nextLine();

        if (role.equals("1")) {
            userLogin();
        } else if (role.equals("2")) {
            adminPanel();
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void userLogin() {
        System.out.print("Enter username: ");
        String uname = scanner.nextLine();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine();

        user user = new user(uname, pass);
        if (!user.login(pass)) return;

        List<Device> deviceList = loadDevicesFromDatabase();

        for (Device device : deviceList) {
            Thread thread = new Thread(device);
            thread.start();
            devices.put(device.getDeviceId(), device);
            threads.put(device.getDeviceId(), thread);
        }

        DeviceDAO actionDAO = new DeviceDAO();

        while (true) {
            System.out.println("\n--- User Control Panel ---");
            for (Device d : devices.values()) {
                System.out.println(d.getDeviceId() + ": " + d.getName() + " (" + d.getStatus() + ")");
            }

            System.out.print("Enter device ID to control (or type 'exit'): ");
            String selectedId = scanner.nextLine();
            if (selectedId.equalsIgnoreCase("exit")) break;

            Device selectedDevice = devices.get(selectedId);
            if (selectedDevice == null) {
                System.out.println("Device not found.");
                continue;
            }

            System.out.println("\n--- " + selectedDevice.getName() + " Control ---");
            System.out.println("1. Turn ON");
            System.out.println("2. Turn OFF");
            System.out.print("Enter your command: ");
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    if (selectedDevice instanceof LightSystem) {
                        LightSystem light = (LightSystem) selectedDevice;
                        System.out.print("Brightness (0–100): ");
                        int brightness = scanner.nextInt();
                        scanner.nextLine();
                        light.setBrightness(brightness);
                        light.turnOn();
                        actionDAO.logAction(light, "Turn On", brightness + "%");
                    } else if (selectedDevice instanceof Thermostat) {
                        Thermostat thermostat = (Thermostat) selectedDevice;
                        thermostat.activate();
                        actionDAO.logAction(thermostat, "Activate", "-");
                    } else if (selectedDevice instanceof SecurityCamera) {
                        SecurityCamera cam = (SecurityCamera) selectedDevice;
                        cam.startRecording();
                        actionDAO.logAction(cam, "Start Recording", "-");
                    }
                    break;
                case "2":
                    selectedDevice.turnOff();
                    actionDAO.logAction(selectedDevice, "Turn Off", "-");
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }

        for (Thread t : threads.values()) t.interrupt();
        for (Device d : devices.values()) {
            if (d instanceof Thermostat) ((Thermostat) d).analyzeTemperatureData();
        }

        user.logout();
        System.out.println("[SYSTEM] Devices shut down.");
    }

    private static void adminPanel() {
        System.out.print("Enter admin username: ");
        String uname = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String pass = scanner.nextLine();

        Admin admin = new Admin(uname, pass);
        if (!admin.login(pass)) return;

        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. Add Device");
            System.out.println("2. Remove Device");
            System.out.println("3. List Devices");
            System.out.println("4. Exit Admin Panel");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    Device device = createDevicePrompt();
                    if (device != null) {
                        admin.addDevice(device);
                        insertDeviceToDatabase(device); // Persist to database so user can see it
                    }
                    break;
                case "2":
                    System.out.print("Enter device ID to remove: ");
                    String id = scanner.nextLine();
                    admin.removeDevice(id);
                    deleteDeviceFromDatabase(id); // Remove from DB too
                    break;
                case "3":
                    admin.listDevices();
                    break;
                case "4":
                    admin.logout();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static Device createDevicePrompt() {
        System.out.println("Choose device type to add:");
        System.out.println("1. Light");
        System.out.println("2. Thermostat");
        System.out.println("3. Camera");
        System.out.print("Choice: ");
        String type = scanner.nextLine();

        System.out.print("Device ID: ");
        String id = scanner.nextLine();
        System.out.print("Device Name: ");
        String name = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();

        Device device = null;
        switch (type) {
            case "1":
                device = new LightSystem(id, name, location);
                break;
            case "2":
                device = new Thermostat(id, name, location);
                break;
            case "3":
                device = new SecurityCamera(id, name, location);
                break;
            default:
                System.out.println("Invalid type.");
        }

        return device;
    }

    private static List<Device> loadDevicesFromDatabase() {
        List<Device> list = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection()) {
            String sql = "SELECT * FROM devices";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("device_id");
                String name = rs.getString("device_name");
                String type = rs.getString("device_type");
                String location = rs.getString("location");

                Device d = null;
                switch (type.toLowerCase()) {
                    case "light":
                        d = new LightSystem(id, name, location);
                        break;
                    case "thermostat":
                        d = new Thermostat(id, name, location);
                        break;
                    case "camera":
                        d = new SecurityCamera(id, name, location);
                        break;
                }

                if (d != null) list.add(d);
            }
        } catch (Exception e) {
            System.out.println("Error loading devices: " + e.getMessage());
        }
        return list;
    }

    private static void insertDeviceToDatabase(Device device) {
        String sql = "INSERT INTO devices (device_id, device_name, device_type, location, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, device.getDeviceId());
            stmt.setString(2, device.getName());
            stmt.setString(3, device.getType());
            stmt.setString(4, device.getLocation());
            stmt.setString(5, device.getStatus());

            stmt.executeUpdate();
            System.out.println("Device added to database.");
        } catch (Exception e) {
            System.out.println("Failed to insert device: " + e.getMessage());
        }
    }

    private static void deleteDeviceFromDatabase(String deviceId) {
        String sql = "DELETE FROM devices WHERE device_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, deviceId);
            stmt.executeUpdate();
            System.out.println("Device removed from database.");
        } catch (Exception e) {
            System.out.println("Failed to delete device: " + e.getMessage());
        }
    }
}
