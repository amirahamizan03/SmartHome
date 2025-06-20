package smarthome;

import java.util.concurrent.locks.ReentrantLock;

public class DeadlockController {

    private final ReentrantLock light  = new ReentrantLock();
    private final ReentrantLock thermo = new ReentrantLock();
    private final ReentrantLock cam    = new ReentrantLock();

    private void p(String t, String msg) {
        System.out.printf("[Deadlock‑%s] %s%n", t, msg);
    }

    public void startDeadlock() {
        System.out.println("\n========== Deadlock Demo ==========");

        Thread A = new Thread(() -> {
            System.out.println("[A] lock Light\n");
            light.lock();
            sleep(100);
            System.out.println("[A] lock Thermostat\n");
            thermo.lock();  // never gets here
        }, "A");

        Thread B = new Thread(() -> {
            System.out.println("[B] lock Thermostat\n");
            thermo.lock();
            sleep(100);
            System.out.println("[B] lock Camera\n");
            cam.lock();
        }, "B");

        Thread C = new Thread(() -> {
            System.out.println("[C] lock Camera\n");
            cam.lock();
            sleep(100);
            System.out.println("[C] lock Light\n");
            light.lock();
        }, "C");

        A.start();
        B.start();
        C.start();
        try {
            A.join();
            B.join();
            C.join();
        } catch (InterruptedException ignored) {

        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms);
        } catch (InterruptedException ignored) {

        } }
}