package smarthome;

public class LightSystem extends Device {
    private int brightness = 100;
    private boolean isOn = false;

    public LightSystem(String deviceId, String name, String location) {
        super(deviceId, name, "Light", location);
    }

    public synchronized void turnOn() {
        isOn = true;
        status = "ON";
        System.out.println(name + " is running at " + brightness + "% brightness.");
    }

    public synchronized void turnOff() {
        isOn = false;
        status = "OFF";
        System.out.println(name + " stopped.");
    }

    public synchronized void setBrightness(int brightness) {
        if (brightness < 0 || brightness > 100) {
            System.out.println("Invalid brightness. Must be between 0 and 100.");
            return;
        }
        this.brightness = brightness;
        System.out.println(name + " brightness set to " + brightness + "%.");
    }

    private synchronized boolean isRunning() {
        return isOn;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (isRunning()) {
                    System.out.println(name + " is running at " + brightness + "% brightness.");
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(name + " interrupted.");
            Thread.currentThread().interrupt();
        } finally {
            System.out.println(name + " stopped.");
        }
    }
}
