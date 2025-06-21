package smarthome;

public abstract class Device implements Runnable {
    protected String deviceId;
    protected String name;
    protected String status;
    protected String type;
    protected String location;

    public Device(String deviceId, String name, String type, String location) {
        this.deviceId = deviceId;
        this.name = name;
        this.type = type;
        this.location = location;
        this.status = "OFF";
    }

    public void turnOn() {
        status = "ON";
        try {
            // Removed updateDeviceStatus and logDeviceStatus
            System.out.println(name + " turned ON.");
        } catch (Exception e) {
            System.out.println("Failed to turn on device: " + e.getMessage());
        }
    }

    public void turnOff() {
        status = "OFF";
        try {
            // Removed updateDeviceStatus and logDeviceStatus
            System.out.println(name + " turned OFF.");
        } catch (Exception e) {
            System.out.println("Failed to turn off device: " + e.getMessage());
        }
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
        System.out.println(name + " status updated to " + newStatus);
    }

    public String getStatus() {
        return status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public abstract void run(); // must be implemented in subclass
}
