package smarthome;

public class Sensor {
    private String sensorId;
    private String type;
    private String value;

    public Sensor(String sensorId, String type) {
        this.sensorId = sensorId;
        this.type = type;
    }

    public String readValue() {
        // Simulated sensor value
        this.value = Math.random() > 0.5 ? "active" : "inactive";
        System.out.println("Sensor [" + sensorId + "] reads: " + value);
        return value;
    }

    public void notifyDevice(Device device) {
        if (value.equals("active")) {
            System.out.println("Sensor triggered. Notifying device " + device.name);
            device.turnOn();
        }
    }
}
