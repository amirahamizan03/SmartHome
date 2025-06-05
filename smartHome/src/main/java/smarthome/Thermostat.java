package smarthome;

class Thermostat implements SmartDevice {
    private boolean isOn = false;
    private int temperature = 25;

    public void activate() {
        isOn = true;
        System.out.println("Thermostat is ON.");
    }

    public void deactivate() {
        isOn = false;
        System.out.println("Thermostat is OFF.");
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (isOn) {
                    System.out.println("Maintaining temperature at " + temperature + "°C");
                }
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            System.out.println("Thermostat interrupted.");
        }
    }
}

