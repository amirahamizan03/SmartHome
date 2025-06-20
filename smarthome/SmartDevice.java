package smarthome;

interface SmartDevice extends Runnable {
    void activate();
    void deactivate();
}