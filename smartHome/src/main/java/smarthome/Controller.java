package smarthome;

import java.util.List;

public class Controller {
    private String controllerId;
    private user user;
    private List<Device> devices;
    private List<AutomationRule> rules;

    public Controller(String controllerId, user user, List<Device> devices, List<AutomationRule> rules) {
        this.controllerId = controllerId;
        this.user = user;
        this.devices = devices;
        this.rules = rules;
    }

    public void executeCommand(String deviceId, String action) {
        for (Device device : devices) {
            if (device.getDeviceId().equals(deviceId)) {
                if (action.equals("on")) {
                    device.turnOn();
                } else if (action.equals("off")) {
                    device.turnOff();
                }
            }
        }
    }

    public void monitorDevices() {
        for (Device device : devices) {
            System.out.println("Device: " + device.name + " | Status: " + device.getStatus());
        }
    }
}
