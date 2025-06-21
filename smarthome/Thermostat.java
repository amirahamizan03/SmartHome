package smarthome;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.IntSummaryStatistics;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Thermostat extends Device {
    private final Lock lock = new ReentrantLock();
    private final List<Integer> recordedTemps = Collections.synchronizedList(new ArrayList<>());

    public Thermostat(String deviceId, String name, String location) {
        super(deviceId, name, "Thermostat", location);
    }

    public void activate() {
        lock.lock();
        try {
            turnOn(); // Use method from Device class
            new DeviceDAO().logAction(this, "Activate Thermostat", "-");
            System.out.println(name + " is ON.");
        } finally {
            lock.unlock();
        }
    }

    public void deactivate() {
        lock.lock();
        try {
            turnOff(); // Use method from Device class
            new DeviceDAO().logAction(this, "Deactivate Thermostat", "-");
            System.out.println(name + " is OFF.");
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

    public List<Integer> getTemperatureData() {
        return recordedTemps;
    }

    public void analyzeTemperatureData() {
        System.out.println("\n--- Thermostat: Temperature Analysis Using Parallel Streams ---");

        long startTime = System.nanoTime();

        IntSummaryStatistics stats = recordedTemps.parallelStream()             // Decomposition
                .filter(t -> t >= 24)                                            // Pipeline: Filter
                .collect(Collectors.summarizingInt(Integer::intValue));         // Reduction & Merge

        long endTime = System.nanoTime();

        System.out.println("Count (>=24°C): " + stats.getCount());
        System.out.println("Average Temp: " + stats.getAverage());
        System.out.println("Max Temp: " + stats.getMax());
        System.out.println("Min Temp: " + stats.getMin());
        System.out.println("Sum of Temps: " + stats.getSum());
        System.out.printf("Analysis took %.2f ms\n", (endTime - startTime) / 1_000_000.0);
    }

    @Override
    public void run() {
        Random rand = new Random();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (isRunning()) {
                    int temp = 22 + rand.nextInt(8); // Range: 22–29°C
                    System.out.println("[Thermostat] Maintaining temperature at " + temp + "°C");
                    recordedTemps.add(temp);
                }
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            System.out.println("[Thermostat] Interrupted.");
            Thread.currentThread().interrupt(); // preserve interrupt flag
        } finally {
            System.out.println("[Thermostat] Thermostat stopped.");
        }
    }
}
