package smarthome;

public class LightSystem extends Device {
    private int brightness;

    public LightSystem(String deviceId, String name, String location) {
        super(deviceId, name, "Light", location);
        this.brightness = 100;
    }

    public synchronized void dim() {
        brightness -= 10;
        if (brightness < 0) brightness = 0;
        System.out.println(name + " dimmed to " + brightness + "% brightness.");
    }

    private synchronized boolean isRunning() {
        return status.equals("ON");
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (isRunning()) {
                    synchronized (this) {
                        System.out.println(name + " is running at " + brightness + "% brightness.");
                    }
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " interrupted.");
        } finally {
            System.out.println(name + " stopped.");
        }
    }
}
