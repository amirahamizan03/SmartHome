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
        new DeviceDAO().updateDeviceStatus(this);
        new DeviceDAO().logDeviceStatus(this);
        System.out.println(name + " turned ON.");
    }

    public void turnOff() {
        status = "OFF";
        new DeviceDAO().updateDeviceStatus(this);
        new DeviceDAO().logDeviceStatus(this);
        System.out.println(name + " turned OFF.");
    }

    public void updateStatus(String newStatus) {
        status = newStatus;
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
        return type; // This is what fixes the issue
    }
    public abstract void run();
}
