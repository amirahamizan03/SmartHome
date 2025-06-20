package smarthome;

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
    }

    public void removeDevice(String deviceId) {
        managedDevices.removeIf(d -> d.getDeviceId().equals(deviceId));
        System.out.println("Device removed: " + deviceId);
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
