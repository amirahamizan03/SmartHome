package smarthome;

import java.util.Scanner;

public class SmartHomeSystem {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Smart Home Device Setup ===");
        System.out.println("Select a device to control:");
        System.out.println("1. Living Room Light");
        System.out.println("2. Kitchen Light");
        System.out.println("3. Bedroom Light");
        System.out.println("4. Hallway Thermostat");
        System.out.println("5. Bedroom Thermostat");
        System.out.println("6. Garage Thermostat");
        System.out.println("7. Front Door Camera");
        System.out.println("8. Backyard Camera");
        System.out.println("9. Garage Camera");

        System.out.print("Enter your choice (1–9): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Device device = null;
        Thread deviceThread;

        // Instantiate the chosen device
        switch (choice) {
            case 1:
                device = new LightSystem("L001", "Living Room Light", "Living Room");
                break;
            case 2:
                device = new LightSystem("L002", "Kitchen Light", "Kitchen");
                break;
            case 3:
                device = new LightSystem("L003", "Bedroom Light", "Bedroom");
                break;
            case 4:
                device = new Thermostat("T001", "Hallway Thermostat", "Hallway");
                break;
            case 5:
                device = new Thermostat("T002", "Bedroom Thermostat", "Bedroom");
                break;
            case 6:
                device = new Thermostat("T003", "Garage Thermostat", "Garage");
                break;
            case 7:
                device = new SecurityCamera("C001", "Front Door Camera", "Front Door");
                break;
            case 8:
                device = new SecurityCamera("C002", "Backyard Camera", "Backyard");
                break;
            case 9:
                device = new SecurityCamera("C003", "Garage Camera", "Garage");
                break;
            default:
                System.out.println("Invalid selection. Exiting...");
                scanner.close();
                return;
        }

        // Start device thread
        deviceThread = new Thread(device, device.getName().replace(" ", "") + "Thread");
        deviceThread.start();

        // Control loop
        boolean running = true;
        while (running) {
            System.out.println("\n--- Device Control Menu ---");
            System.out.println("1. On");
            System.out.println("2. Off");
            System.out.println("3. Exit");
            System.out.print("Enter your command: ");
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    if (device instanceof LightSystem) {
                        LightSystem light = (LightSystem) device;
                        System.out.print("Enter brightness level (0–100): ");
                        int brightness = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        light.setBrightness(brightness);
                        light.turnOn();
                    } else if (device instanceof Thermostat) {
                        ((Thermostat) device).activate();
                    } else if (device instanceof SecurityCamera) {
                        ((SecurityCamera) device).startRecording();
                    }
                    break;

                case "2":
                    if (device instanceof LightSystem) {
                        ((LightSystem) device).turnOff();
                    } else if (device instanceof Thermostat) {
                        ((Thermostat) device).deactivate();
                    } else if (device instanceof SecurityCamera) {
                        ((SecurityCamera) device).stopRecording();
                    }
                    break;

                case "3":
                    running = false;
                    if (device instanceof LightSystem) {
                        ((LightSystem) device).turnOff();
                    } else if (device instanceof Thermostat) {
                        ((Thermostat) device).deactivate();
                    } else if (device instanceof SecurityCamera) {
                        ((SecurityCamera) device).stopRecording();
                    }
                    deviceThread.interrupt();
                    break;

                default:
                    System.out.println("Invalid command. Try again.");
            }
        }

        // Wait for thread to finish
        deviceThread.join();

        // Analyze temperature data if it's a thermostat
        if (device instanceof Thermostat) {
            ((Thermostat) device).analyzeTemperatureData();
        }

        System.out.println("\n[SYSTEM] Smart Home Device shut down.");
        scanner.close();
    }
}
