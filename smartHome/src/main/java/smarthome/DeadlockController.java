package smarthome;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockController {

        // Declare locks
        private final ReentrantLock lightLock = new ReentrantLock();
        private final ReentrantLock thermoLock = new ReentrantLock();
        private final ReentrantLock cameraLock = new ReentrantLock();

        public void startDeadlock() {
            System.out.println("\n--- Starting Deadlock Simulation (with timeout to avoid freeze) ---");

            // Thread A: Light → Thermostat
            Thread a = new Thread(() -> {
                try {
                    if (lightLock.tryLock(100, TimeUnit.MILLISECONDS)) {
                        System.out.println("A locked Light");
                        Thread.sleep(100);
                        if (thermoLock.tryLock(100, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("A locked Thermostat");
                            } finally {
                                thermoLock.unlock();
                            }
                        } else {
                            System.out.println("A failed to lock Thermostat");
                        }
                    } else {
                        System.out.println("A failed to lock Light");
                    }
                } catch (InterruptedException e) {
                    System.out.println("A interrupted");
                } finally {
                    if (lightLock.isHeldByCurrentThread()) lightLock.unlock();
                }
            }, "Deadlock-A");

            // Thread B: Thermostat → Camera
            Thread b = new Thread(() -> {
                try {
                    if (thermoLock.tryLock(100, TimeUnit.MILLISECONDS)) {
                        System.out.println("B locked Thermostat");
                        Thread.sleep(100);
                        if (cameraLock.tryLock(100, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("B locked Camera");
                            } finally {
                                cameraLock.unlock();
                            }
                        } else {
                            System.out.println("B failed to lock Camera");
                        }
                    } else {
                        System.out.println("B failed to lock Thermostat");
                    }
                } catch (InterruptedException e) {
                    System.out.println("B interrupted");
                } finally {
                    if (thermoLock.isHeldByCurrentThread()) thermoLock.unlock();
                }
            }, "Deadlock-B");

            // Thread C: Camera → Light
            Thread c = new Thread(() -> {
                try {
                    if (cameraLock.tryLock(100, TimeUnit.MILLISECONDS)) {
                        System.out.println("C locked Camera");
                        Thread.sleep(100);
                        if (lightLock.tryLock(100, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("C locked Light");
                            } finally {
                                lightLock.unlock();
                            }
                        } else {
                            System.out.println("C failed to lock Light");
                        }
                    } else {
                        System.out.println("C failed to lock Camera");
                    }
                } catch (InterruptedException e) {
                    System.out.println("C interrupted");
                } finally {
                    if (cameraLock.isHeldByCurrentThread()) cameraLock.unlock();
                }
            }, "Deadlock-C");

            // Start all threads
            a.start();
            b.start();
            c.start();
        }
    }

