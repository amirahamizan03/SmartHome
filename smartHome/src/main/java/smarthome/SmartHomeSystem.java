package smarthome;

public class SmartHomeSystem {
    public static void main(String[] args) throws InterruptedException {

        // 1. Create instances of each device with ID, name, and location
        LightSystem light = new LightSystem("L001", "Living Room Light", "Living Room");
        Thermostat thermostat = new Thermostat("T001", "Main Thermostat", "Hallway");
        SecurityCamera camera = new SecurityCamera("C001", "Front Door Camera", "Entrance");

        // 2. Wrap each device in a thread
        Thread lightThread = new Thread(light, "LightThread");
        Thread thermostatThread = new Thread(thermostat, "ThermostatThread");
        Thread cameraThread = new Thread(camera, "CameraThread");

        // 3. Start all device threads
        lightThread.start();
        thermostatThread.start();
        cameraThread.start();

        // 4. Activate devices
        light.turnOn();           //  use method from Device
        thermostat.activate();    // still uses activate() for additional behavior
        camera.startRecording();  //  for SecurityCamera

        // 5. Let them run for 5 seconds
        Thread.sleep(5000);

        // 6. Deactivate devices
        light.turnOff();          //  from Device
        thermostat.deactivate();  //
        camera.stopRecording();   //


        // 7. Interrupt threads
        lightThread.interrupt();
        thermostatThread.interrupt();
        cameraThread.interrupt();

        // 8. Join threads
        lightThread.join();
        thermostatThread.join();
        cameraThread.join();

        // 9. Analyze temperature data
        thermostat.analyzeTemperatureData();

        // 10. Final shutdown
        System.out.println("\n[SYSTEM] Smart Home System shutting down.");
    }
}
