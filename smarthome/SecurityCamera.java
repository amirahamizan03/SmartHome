package smarthome;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SecurityCamera extends Device {
    private boolean recording;
    private final Lock lock = new ReentrantLock();

    public SecurityCamera(String deviceId, String name, String location) {
        super(deviceId, name, "Camera", location);
        this.recording = false;
    }

    public void startRecording() {
        lock.lock();
        try {
            recording = true;
            turnOn(); // inherited from Device
            System.out.println(name + " started recording.");
        } finally {
            lock.unlock();
        }
    }

    public void stopRecording() {
        lock.lock();
        try {
            recording = false;
            turnOff(); // inherited from Device
            System.out.println(name + " stopped recording.");
        } finally {
            lock.unlock();
        }
    }

    private boolean isRunning() {
        lock.lock();
        try {
            return status.equals("ON");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (isRunning()) {
                    System.out.println("[SecurityCamera] " + name + " monitoring area...");
                }
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // reset the interrupt flag
            System.out.println("[SecurityCamera] " + name + " interrupted.");
        } finally {
            System.out.println("[SecurityCamera] " + name + " system stopped.");
        }
    }
}
