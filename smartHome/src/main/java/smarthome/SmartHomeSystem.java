package smarthome;

public class SmartHomeSystem {
    public static void main(String[] args) throws InterruptedException {
        // 1. Create instances of each device
        LightSystem light = new LightSystem();
        Thermostat thermostat = new Thermostat();
        SecurityCamera camera = new SecurityCamera();

        // 2. Wrap each device in a thread
        Thread lightThread = new Thread(light);
        Thread thermostatThread = new Thread(thermostat);
        Thread cameraThread = new Thread(camera);

        // 3. Start all device threads (concurrently)
        lightThread.start();
        thermostatThread.start();
        cameraThread.start();

        // 4. Activate each device
        light.activate();         // Light thread will start printing "Light is running..."
        thermostat.activate();    // Thermostat thread will print temperature status
        camera.activate();        // Camera will print monitoring status

        // 5. Let them run for 5 seconds
        Thread.sleep(5000);  // Main thread waits — other threads keep running

        // 6. Deactivate the devices
        light.deactivate();
        thermostat.deactivate();
        camera.deactivate();

        // 7. Interrupt the threads (influence them to stop)
        lightThread.interrupt();
        thermostatThread.interrupt();
        cameraThread.interrupt();

        // 8. Wait for each thread to finish before ending main program
        lightThread.join();
        thermostatThread.join();
        cameraThread.join();

        System.out.println("Smart Home System shutting down.");
    }
}
