package smarthome;

class LightSystem implements SmartDevice {
    private boolean isOn = false;

    public void activate() {
        isOn = true;
        System.out.println("Light is ON.");
    }

    public void deactivate() {
        isOn = false;
        System.out.println("Light is OFF.");
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (isOn) {
                    System.out.println("Light is running...");
                }
                Thread.sleep(1000); // simulate work
            }
        } catch (InterruptedException e) {
            System.out.println("Light System interrupted.");
        }
    }
}
